/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.ntb.usb.LibusbJava
 *  ch.ntb.usb.USBException
 *  ch.ntb.usb.Usb_Device
 *  ch.ntb.usb.Usb_Device_Descriptor
 */
package com.owon.uppersoft.patch.usb;

import ch.ntb.usb.LibusbJava;
import ch.ntb.usb.USBException;
import ch.ntb.usb.Usb_Device;
import ch.ntb.usb.Usb_Device_Descriptor;
import com.owon.uppersoft.common.comm.CDevice;
import com.owon.uppersoft.common.comm.IDevice;
import com.owon.uppersoft.common.comm.USBID;
import java.util.List;

public class MultiDeviceUtil {
    public void test() {
        try {
            List<IDevice> devList = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
            for (IDevice id : devList) {
                Usb_Device dev = id.getUsb_Device();
                long handle = LibusbJava.usb_open((Usb_Device)dev);
                Usb_Device_Descriptor devDesc = dev.getDescriptor();
                String iProduct = LibusbJava.usb_get_string_simple((long)handle, (int)devDesc.getIProduct());
                String iManufacturer = LibusbJava.usb_get_string_simple((long)handle, (int)devDesc.getIManufacturer());
                String iSerialNumber = LibusbJava.usb_get_string_simple((long)handle, (int)devDesc.getISerialNumber());
                String Devnum = LibusbJava.usb_get_string_simple((long)handle, (int)dev.getDevnum());
                String msg = String.format("iProduct: %s; iManufacturer: %s; iSerialNumber: %s; Devnum: %s,", iProduct, iManufacturer, iSerialNumber, Devnum);
                System.out.printf(msg, new Object[0]);
                LibusbJava.usb_close((long)handle);
            }
        }
        catch (USBException e) {
            e.printStackTrace();
        }
    }

    public void doConnect() {
        try {
            List<IDevice> devList = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
            for (IDevice id : devList) {
                CDevice device = CDevice.getDevice(USBID.getVid(), USBID.getPid());
                CDevice.simulateOpen(device, id);
                device.close();
            }
        }
        catch (USBException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MultiDeviceUtil mdu = new MultiDeviceUtil();
        mdu.test();
    }
}

