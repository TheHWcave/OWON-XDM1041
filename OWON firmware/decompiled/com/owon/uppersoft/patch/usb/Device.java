/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.ntb.usb.LibusbJava
 *  ch.ntb.usb.USBException
 *  ch.ntb.usb.USBTimeoutException
 *  ch.ntb.usb.Usb_Bus
 *  ch.ntb.usb.Usb_Config_Descriptor
 *  ch.ntb.usb.Usb_Device
 *  ch.ntb.usb.Usb_Device_Descriptor
 *  ch.ntb.usb.Usb_Endpoint_Descriptor
 *  ch.ntb.usb.Usb_Interface
 *  ch.ntb.usb.Usb_Interface_Descriptor
 *  ch.ntb.usb.logger.LogUtil
 */
package com.owon.uppersoft.patch.usb;

import ch.ntb.usb.LibusbJava;
import ch.ntb.usb.USBException;
import ch.ntb.usb.USBTimeoutException;
import ch.ntb.usb.Usb_Bus;
import ch.ntb.usb.Usb_Config_Descriptor;
import ch.ntb.usb.Usb_Device;
import ch.ntb.usb.Usb_Device_Descriptor;
import ch.ntb.usb.Usb_Endpoint_Descriptor;
import ch.ntb.usb.Usb_Interface;
import ch.ntb.usb.Usb_Interface_Descriptor;
import ch.ntb.usb.logger.LogUtil;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Device {
    private static final Logger logger = LogUtil.getLogger((String)"ch.ntb.usb");
    private int maxPacketSize = -1;
    private int idVendor;
    private int idProduct;
    private int dev_configuration;
    private int dev_interface;
    private int dev_altinterface;
    private long usbDevHandle;
    private boolean resetOnFirstOpen = false;
    private boolean resetDone = false;
    private int resetTimeout = 2000;
    private Usb_Device dev;
    private boolean initUSBDone = false;

    protected Device(short idVendor, short idProduct) {
        this.idVendor = idVendor;
        this.idProduct = idProduct;
    }

    private void initUSB() {
        LibusbJava.usb_init();
        this.initUSBDone = true;
    }

    private Usb_Bus initBus() throws USBException {
        LibusbJava.usb_find_busses();
        LibusbJava.usb_find_devices();
        Usb_Bus bus = LibusbJava.usb_get_busses();
        if (bus == null) {
            throw new USBException("LibusbJava.usb_get_busses(): " + LibusbJava.usb_strerror());
        }
        return bus;
    }

    private void updateMaxPacketSize(Usb_Device device) throws USBException {
        this.maxPacketSize = -1;
        Usb_Config_Descriptor[] confDesc = device.getConfig();
        int i = 0;
        while (i < confDesc.length) {
            Usb_Interface[] int_ = confDesc[i].getInterface();
            int j = 0;
            while (j < int_.length) {
                Usb_Interface_Descriptor[] intDesc = int_[j].getAltsetting();
                int k = 0;
                while (k < intDesc.length) {
                    Usb_Endpoint_Descriptor[] epDesc = intDesc[k].getEndpoint();
                    int l = 0;
                    while (l < epDesc.length) {
                        this.maxPacketSize = Math.max(epDesc[l].getWMaxPacketSize(), this.maxPacketSize);
                        ++l;
                    }
                    ++k;
                }
                ++j;
            }
            ++i;
        }
        if (this.maxPacketSize <= 0) {
            throw new USBException("No USB endpoints found. Check the device configuration");
        }
    }

    private Usb_Device initDevice() throws USBException {
        Usb_Bus bus = this.initBus();
        Usb_Device device = null;
        while (bus != null) {
            device = bus.getDevices();
            while (device != null) {
                Usb_Device_Descriptor devDesc = device.getDescriptor();
                if (devDesc.getIdVendor() == this.idVendor && devDesc.getIdProduct() == this.idProduct) {
                    logger.info("Device found: " + device.getFilename());
                    this.updateMaxPacketSize(device);
                    return device;
                }
                device = device.getNext();
            }
            bus = bus.getNext();
        }
        return null;
    }

    public void updateDescriptors() throws USBException {
        if (!this.initUSBDone) {
            this.initUSB();
        }
        this.dev = this.initDevice();
    }

    public Usb_Device_Descriptor getDeviceDescriptor() {
        if (this.dev == null) {
            return null;
        }
        return this.dev.getDescriptor();
    }

    public Usb_Config_Descriptor[] getConfigDescriptors() {
        if (this.dev == null) {
            return null;
        }
        return this.dev.getConfig();
    }

    public void open(int configuration, int interface_, int altinterface) throws USBException {
        this.dev_configuration = configuration;
        this.dev_interface = interface_;
        this.dev_altinterface = altinterface;
        if (this.usbDevHandle != 0L) {
            throw new USBException("device opened, close or reset first");
        }
        this.initUSB();
        this.dev = this.initDevice();
        if (this.dev != null) {
            long res = LibusbJava.usb_open((Usb_Device)this.dev);
            if (res == 0L) {
                throw new USBException("LibusbJava.usb_open: " + LibusbJava.usb_strerror());
            }
            this.usbDevHandle = res;
        }
        if (this.dev == null || this.usbDevHandle == 0L) {
            throw new USBException("USB device with idVendor 0x" + Integer.toHexString(this.idVendor & 0xFFFF) + " and idProduct 0x" + Integer.toHexString(this.idProduct & 0xFFFF) + " not found");
        }
        this.claim_interface(this.usbDevHandle, configuration, interface_, altinterface);
        if (this.resetOnFirstOpen & !this.resetDone) {
            logger.info("reset on first open");
            this.resetDone = true;
            this.reset();
            try {
                Thread.sleep(this.resetTimeout);
            }
            catch (InterruptedException interruptedException) {}
            this.open(configuration, interface_, altinterface);
        }
    }

    public void close() throws USBException {
        if (this.usbDevHandle == 0L) {
            throw new USBException("invalid device handle");
        }
        this.release_interface(this.usbDevHandle, this.dev_interface);
        if (LibusbJava.usb_close((long)this.usbDevHandle) < 0) {
            this.usbDevHandle = 0L;
            throw new USBException("LibusbJava.usb_close: " + LibusbJava.usb_strerror());
        }
        this.usbDevHandle = 0L;
        this.maxPacketSize = -1;
        logger.info("device closed");
    }

    public void reset() throws USBException {
        if (this.usbDevHandle == 0L) {
            throw new USBException("invalid device handle");
        }
        this.release_interface(this.usbDevHandle, this.dev_interface);
        if (LibusbJava.usb_reset((long)this.usbDevHandle) < 0) {
            this.usbDevHandle = 0L;
            throw new USBException("LibusbJava.usb_reset: " + LibusbJava.usb_strerror());
        }
        this.usbDevHandle = 0L;
        logger.info("device reset");
    }

    public int writeBulk(int out_ep_address, byte[] data, int size, int timeout, boolean reopenOnTimeout) throws USBException {
        if (this.usbDevHandle == 0L) {
            throw new USBException("invalid device handle");
        }
        if (data == null) {
            throw new USBException("data must not be null");
        }
        if (size <= 0 || size > data.length) {
            throw new ArrayIndexOutOfBoundsException("invalid size: " + size);
        }
        int lenWritten = LibusbJava.usb_bulk_write((long)this.usbDevHandle, (int)out_ep_address, (byte[])data, (int)size, (int)timeout);
        if (lenWritten < 0) {
            if (lenWritten == LibusbJava.ERROR_TIMEDOUT) {
                if (reopenOnTimeout) {
                    logger.info("try to reopen");
                    this.reset();
                    this.open(this.dev_configuration, this.dev_interface, this.dev_altinterface);
                    return this.writeBulk(out_ep_address, data, size, timeout, false);
                }
                throw new USBTimeoutException("LibusbJava.usb_bulk_write: " + LibusbJava.usb_strerror());
            }
            throw new USBException("LibusbJava.usb_bulk_write: " + LibusbJava.usb_strerror());
        }
        logger.info("length written: " + lenWritten);
        if (logger.isLoggable(Level.FINEST)) {
            StringBuffer sb = new StringBuffer("bulkwrite, ep 0x" + Integer.toHexString(out_ep_address) + ": " + lenWritten + " Bytes sent: ");
            int i = 0;
            while (i < lenWritten) {
                sb.append("0x" + String.format("%1$02X", data[i]) + " ");
                ++i;
            }
            logger.info(sb.toString());
        }
        return lenWritten;
    }

    public int readBulk(int in_ep_address, byte[] data, int size, int timeout, boolean reopenOnTimeout) throws USBException {
        if (this.usbDevHandle == 0L) {
            throw new USBException("invalid device handle");
        }
        if (data == null) {
            throw new USBException("data must not be null");
        }
        if (size <= 0 || size > data.length) {
            throw new ArrayIndexOutOfBoundsException("invalid size: " + size);
        }
        int lenRead = LibusbJava.usb_bulk_read((long)this.usbDevHandle, (int)in_ep_address, (byte[])data, (int)size, (int)timeout);
        if (lenRead < 0) {
            if (lenRead == LibusbJava.ERROR_TIMEDOUT) {
                if (reopenOnTimeout) {
                    logger.info("try to reopen");
                    this.reset();
                    this.open(this.dev_configuration, this.dev_interface, this.dev_altinterface);
                    return this.readBulk(in_ep_address, data, size, timeout, false);
                }
                throw new USBTimeoutException("LibusbJava.usb_bulk_read: " + LibusbJava.usb_strerror());
            }
            throw new USBException("LibusbJava.usb_bulk_read: " + LibusbJava.usb_strerror());
        }
        logger.info("length read: " + lenRead);
        if (logger.isLoggable(Level.FINEST)) {
            StringBuffer sb = new StringBuffer("bulkread, ep 0x" + Integer.toHexString(in_ep_address) + ": " + lenRead + " Bytes received: ");
            int i = 0;
            while (i < lenRead) {
                sb.append("0x" + String.format("%1$02X", data[i]) + " ");
                ++i;
            }
            logger.info(sb.toString());
        }
        return lenRead;
    }

    public int writeInterrupt(int out_ep_address, byte[] data, int size, int timeout, boolean reopenOnTimeout) throws USBException {
        if (this.usbDevHandle == 0L) {
            throw new USBException("invalid device handle");
        }
        if (data == null) {
            throw new USBException("data must not be null");
        }
        if (size <= 0 || size > data.length) {
            throw new ArrayIndexOutOfBoundsException("invalid size: " + size);
        }
        int lenWritten = LibusbJava.usb_interrupt_write((long)this.usbDevHandle, (int)out_ep_address, (byte[])data, (int)size, (int)timeout);
        if (lenWritten < 0) {
            if (lenWritten == LibusbJava.ERROR_TIMEDOUT) {
                if (reopenOnTimeout) {
                    logger.info("try to reopen");
                    this.reset();
                    this.open(this.dev_configuration, this.dev_interface, this.dev_altinterface);
                    return this.writeInterrupt(out_ep_address, data, size, timeout, false);
                }
                throw new USBTimeoutException("LibusbJava.usb_interrupt_write: " + LibusbJava.usb_strerror());
            }
            throw new USBException("LibusbJava.usb_interrupt_write: " + LibusbJava.usb_strerror());
        }
        logger.info("length written: " + lenWritten);
        if (logger.isLoggable(Level.FINEST)) {
            StringBuffer sb = new StringBuffer("interruptwrite, ep 0x" + Integer.toHexString(out_ep_address) + ": " + lenWritten + " Bytes sent: ");
            int i = 0;
            while (i < lenWritten) {
                sb.append("0x" + String.format("%1$02X", data[i]) + " ");
                ++i;
            }
            logger.info(sb.toString());
        }
        return lenWritten;
    }

    public int readInterrupt(int in_ep_address, byte[] data, int size, int timeout, boolean reopenOnTimeout) throws USBException {
        if (this.usbDevHandle == 0L) {
            throw new USBException("invalid device handle");
        }
        if (data == null) {
            throw new USBException("data must not be null");
        }
        if (size <= 0 || size > data.length) {
            throw new ArrayIndexOutOfBoundsException("invalid size: " + size);
        }
        int lenRead = LibusbJava.usb_interrupt_read((long)this.usbDevHandle, (int)in_ep_address, (byte[])data, (int)size, (int)timeout);
        if (lenRead < 0) {
            if (lenRead == LibusbJava.ERROR_TIMEDOUT) {
                if (reopenOnTimeout) {
                    logger.info("try to reopen");
                    this.reset();
                    this.open(this.dev_configuration, this.dev_interface, this.dev_altinterface);
                    return this.readInterrupt(in_ep_address, data, size, timeout, false);
                }
                throw new USBTimeoutException("LibusbJava.usb_interrupt_read: " + LibusbJava.usb_strerror());
            }
            throw new USBException("LibusbJava.usb_interrupt_read: " + LibusbJava.usb_strerror());
        }
        logger.info("length read: " + lenRead);
        if (logger.isLoggable(Level.FINEST)) {
            StringBuffer sb = new StringBuffer("interrupt, ep 0x" + Integer.toHexString(in_ep_address) + ": " + lenRead + " Bytes received: ");
            int i = 0;
            while (i < lenRead) {
                sb.append("0x" + String.format("%1$02X", data[i]) + " ");
                ++i;
            }
            logger.info(sb.toString());
        }
        return lenRead;
    }

    public int controlMsg(int requestType, int request, int value, int index, byte[] data, int size, int timeout, boolean reopenOnTimeout) throws USBException {
        if (this.usbDevHandle == 0L) {
            throw new USBException("invalid device handle");
        }
        if (data == null) {
            throw new USBException("data must not be null");
        }
        if (size <= 0 || size > data.length) {
            throw new ArrayIndexOutOfBoundsException("invalid size: " + size);
        }
        int len = LibusbJava.usb_control_msg((long)this.usbDevHandle, (int)requestType, (int)request, (int)value, (int)index, (byte[])data, (int)size, (int)timeout);
        if (len < 0) {
            if (len == LibusbJava.ERROR_TIMEDOUT) {
                if (reopenOnTimeout) {
                    logger.info("try to reopen");
                    this.reset();
                    this.open(this.dev_configuration, this.dev_interface, this.dev_altinterface);
                    return this.controlMsg(requestType, request, value, index, data, size, timeout, false);
                }
                throw new USBTimeoutException("LibusbJava.controlMsg: " + LibusbJava.usb_strerror());
            }
            throw new USBException("LibusbJava.controlMsg: " + LibusbJava.usb_strerror());
        }
        logger.info("length read/written: " + len);
        if (logger.isLoggable(Level.FINEST)) {
            StringBuffer sb = new StringBuffer("controlMsg: " + len + " Bytes received(written: ");
            int i = 0;
            while (i < len) {
                sb.append("0x" + String.format("%1$02X", data[i]) + " ");
                ++i;
            }
            logger.info(sb.toString());
        }
        return len;
    }

    private void claim_interface(long usb_dev_handle, int configuration, int interface_, int altinterface) throws USBException {
        if (LibusbJava.usb_set_configuration((long)usb_dev_handle, (int)configuration) < 0) {
            this.usbDevHandle = 0L;
            throw new USBException("LibusbJava.usb_set_configuration: " + LibusbJava.usb_strerror());
        }
        if (LibusbJava.usb_claim_interface((long)usb_dev_handle, (int)interface_) < 0) {
            this.usbDevHandle = 0L;
            throw new USBException("LibusbJava.usb_claim_interface: " + LibusbJava.usb_strerror());
        }
        if (altinterface >= 0 && LibusbJava.usb_set_altinterface((long)usb_dev_handle, (int)altinterface) < 0) {
            try {
                this.release_interface(usb_dev_handle, interface_);
            }
            catch (USBException uSBException) {}
            this.usbDevHandle = 0L;
            throw new USBException("LibusbJava.usb_set_altinterface: " + LibusbJava.usb_strerror());
        }
        logger.info("interface claimed");
    }

    private void release_interface(long dev_handle, int interface_) throws USBException {
        if (LibusbJava.usb_release_interface((long)dev_handle, (int)interface_) < 0) {
            this.usbDevHandle = 0L;
            throw new USBException("LibusbJava.usb_release_interface: " + LibusbJava.usb_strerror());
        }
        logger.info("interface released");
    }

    public int getIdProduct() {
        return this.idProduct;
    }

    public int getIdVendor() {
        return this.idVendor;
    }

    public int getAltinterface() {
        return this.dev_altinterface;
    }

    public int getConfiguration() {
        return this.dev_configuration;
    }

    public int getInterface() {
        return this.dev_interface;
    }

    public int getMaxPacketSize() {
        return this.maxPacketSize;
    }

    public boolean isOpen() {
        return this.usbDevHandle != 0L;
    }

    public void setResetOnFirstOpen(boolean enable, int timeout) {
        this.resetOnFirstOpen = enable;
        this.resetTimeout = timeout;
    }

    public static List<Usb_Device> scanMatchedDevices(short idVendor, short idProduct) throws USBException {
        LibusbJava.usb_init();
        LibusbJava.usb_find_busses();
        LibusbJava.usb_find_devices();
        Usb_Bus bus = LibusbJava.usb_get_busses();
        if (bus == null) {
            throw new USBException("LibusbJava.usb_get_busses(): " + LibusbJava.usb_strerror());
        }
        Usb_Device device = null;
        LinkedList<Usb_Device> devices = new LinkedList<Usb_Device>();
        while (bus != null) {
            device = bus.getDevices();
            while (device != null) {
                Usb_Device_Descriptor devDesc = device.getDescriptor();
                if (devDesc.getIdVendor() == idVendor && devDesc.getIdProduct() == idProduct) {
                    devices.add(device);
                }
                device = device.getNext();
            }
            bus = bus.getNext();
        }
        return devices;
    }

    public static Device getDevice(short idVendor, short idProduct) {
        Device dev = new Device(idVendor, idProduct);
        return dev;
    }

    public static void simulateOpen(Device device, Usb_Device dev, int configuration, int interface_, int altinterface) throws USBException {
        device.dev_configuration = configuration;
        device.dev_interface = interface_;
        device.dev_altinterface = altinterface;
        if (device.usbDevHandle != 0L) {
            throw new USBException("device opened, close or reset first");
        }
        device.initUSB();
        if (dev != null) {
            long res = LibusbJava.usb_open((Usb_Device)dev);
            if (res == 0L) {
                throw new USBException("LibusbJava.usb_open: " + LibusbJava.usb_strerror());
            }
            device.usbDevHandle = res;
        }
        if (dev == null || device.usbDevHandle == 0L) {
            throw new USBException("USB device with idVendor 0x" + Integer.toHexString(device.idVendor & 0xFFFF) + " and idProduct 0x" + Integer.toHexString(device.idProduct & 0xFFFF) + " not found");
        }
        device.claim_interface(device.usbDevHandle, configuration, interface_, altinterface);
        if (device.resetOnFirstOpen & !device.resetDone) {
            logger.info("reset on first open");
            device.resetDone = true;
            device.reset();
            try {
                Thread.sleep(device.resetTimeout);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            Device.simulateOpen(device, dev, configuration, interface_, altinterface);
        }
    }
}

