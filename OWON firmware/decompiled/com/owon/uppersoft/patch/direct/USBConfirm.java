/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.ntb.usb.LibusbJava
 *  ch.ntb.usb.Usb_Bus
 *  ch.ntb.usb.Usb_Device
 *  ch.ntb.usb.Usb_Device_Descriptor
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.patch.direct;

import ch.ntb.usb.LibusbJava;
import ch.ntb.usb.Usb_Bus;
import ch.ntb.usb.Usb_Device;
import ch.ntb.usb.Usb_Device_Descriptor;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import org.eclipse.swt.widgets.Shell;

public class USBConfirm {
    public static boolean updateStatus(short vid, short pid) {
        LibusbJava.usb_init();
        LibusbJava.usb_find_busses();
        LibusbJava.usb_find_devices();
        Usb_Bus bus = LibusbJava.usb_get_busses();
        Usb_Device device = null;
        while (bus != null) {
            device = bus.getDevices();
            while (device != null) {
                Usb_Device_Descriptor devDesc = device.getDescriptor();
                if (devDesc.getIdVendor() == vid && devDesc.getIdProduct() == pid) {
                    return true;
                }
                device = device.getNext();
            }
            bus = bus.getNext();
        }
        return false;
    }

    public static void installUSB() {
        try {
            Process p = Runtime.getRuntime().exec("cmd /c start reinstall.bat", null, new File("USBDRV"));
            p.waitFor();
            Thread.sleep(1000L);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void reinstallUSB(Shell shell, ResourceBundle bundle) {
        try {
            Process p = Runtime.getRuntime().exec("cmd /c start reinstall.bat", null, new File("USBDRV"));
            p.waitFor();
            Thread.sleep(1000L);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

