/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.ntb.usb.LibusbJava
 *  ch.ntb.usb.USBException
 *  ch.ntb.usb.Usb_Device
 *  ch.ntb.usb.Usb_Device_Descriptor
 */
package com.owon.uppersoft.common.commjob.instance;

import ch.ntb.usb.LibusbJava;
import ch.ntb.usb.USBException;
import ch.ntb.usb.Usb_Device;
import ch.ntb.usb.Usb_Device_Descriptor;
import com.owon.uppersoft.common.commjob.instance.CDevice;
import com.owon.uppersoft.common.commjob.instance.ICommunication;
import com.owon.uppersoft.common.commjob.instance.IDevice;
import java.util.ArrayList;
import java.util.List;

public class USBCommunication
implements ICommunication {
    private short IdVender;
    private short IdProduct;
    private IDevice iDevice;
    private CDevice dev;
    private int WriteEndpoint;
    private int ReadEndpoint;
    private boolean reopenOnTimeout;

    public boolean openDevice() {
        this.reopenOnTimeout = false;
        this.dev = CDevice.getDevice(this.IdVender, this.IdProduct);
        if (this.iDevice == null) {
            this.restoreBackup();
        }
        try {
            if (this.iDevice != null) {
                this.WriteEndpoint = this.iDevice.getWriteEndpoint();
                this.ReadEndpoint = this.iDevice.getReadEndpoint();
                CDevice.simulateOpen(this.dev, this.iDevice);
                return true;
            }
        }
        catch (USBException e) {
            try {
                this.iDevice = null;
                e.printStackTrace();
            }
            catch (USBException e2) {
                this.iDevice = null;
                e2.printStackTrace();
            }
        }
        return false;
    }

    private void restoreBackup() throws USBException {
        List<IDevice> ld = CDevice.scanMatchedDevices(this.IdVender, this.IdProduct);
        if (ld.size() <= 0) {
            return;
        }
        this.iDevice = ld.get(0);
    }

    @Override
    public int read(byte[] arr, int beg, int len) {
        try {
            return this.dev.readBulk(this.ReadEndpoint, arr, len, 1000, this.reopenOnTimeout);
        }
        catch (USBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int write(byte[] arr, int beg, int len) {
        try {
            return this.dev.writeBulk(this.WriteEndpoint, arr, len, 1000, this.reopenOnTimeout);
        }
        catch (USBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean open(Object[] para) {
        this.IdVender = (Short)para[0];
        this.IdProduct = (Short)para[1];
        System.out.println("start");
        try {
            return this.openDevice();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void close() {
        try {
            if (this.dev != null && this.dev.isOpen()) {
                this.dev.close();
                System.out.println("over");
            }
        }
        catch (USBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return this.dev.isOpen();
    }

    public static List<IDevice> loadAvailablePort(short idVender, short idProduct) {
        try {
            List<IDevice> udl = CDevice.scanMatchedDevices(idVender, idProduct);
            for (IDevice id : udl) {
                Usb_Device dev = id.getUsb_Device();
                long handle = LibusbJava.usb_open((Usb_Device)dev);
                Usb_Device_Descriptor devDesc = dev.getDescriptor();
                String iSerialNumber = LibusbJava.usb_get_string_simple((long)handle, (int)devDesc.getISerialNumber());
                id.setSerialNumber(iSerialNumber);
                LibusbJava.usb_close((long)handle);
            }
            return udl;
        }
        catch (USBException e) {
            e.printStackTrace();
            return new ArrayList<IDevice>();
        }
    }
}

