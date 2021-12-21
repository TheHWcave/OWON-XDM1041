/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.ntb.usb.Usb_Config_Descriptor
 *  ch.ntb.usb.Usb_Device
 *  ch.ntb.usb.Usb_Endpoint_Descriptor
 *  ch.ntb.usb.Usb_Interface
 *  ch.ntb.usb.Usb_Interface_Descriptor
 */
package com.owon.uppersoft.common.comm;

import ch.ntb.usb.Usb_Config_Descriptor;
import ch.ntb.usb.Usb_Device;
import ch.ntb.usb.Usb_Endpoint_Descriptor;
import ch.ntb.usb.Usb_Interface;
import ch.ntb.usb.Usb_Interface_Descriptor;
import com.owon.uppersoft.common.comm.IDevice;

public class ComparableDevice
implements IDevice,
Comparable<ComparableDevice> {
    private Usb_Device usb_Device;
    private String serialNumber;
    private int bConfigurationValue;
    private int bInterfaceNumber;
    private int bAlternateSetting;
    private int WriteEndpoint;
    private int ReadEndpoint;

    @Override
    public int getBAlternateSetting() {
        return this.bAlternateSetting;
    }

    @Override
    public int getBConfigurationValue() {
        return this.bConfigurationValue;
    }

    @Override
    public int getBInterfaceNumber() {
        return this.bInterfaceNumber;
    }

    @Override
    public int getReadEndpoint() {
        return this.ReadEndpoint;
    }

    @Override
    public int getWriteEndpoint() {
        return this.WriteEndpoint;
    }

    private ComparableDevice(Usb_Device usb_Device, String serialNumber) {
        this.usb_Device = usb_Device;
        this.serialNumber = serialNumber;
    }

    public String toString() {
        String msg = String.format("cfg: %s, itf: %s, altitf: %s, in: %s, out: %s\r\n", this.bConfigurationValue, this.bInterfaceNumber, this.bAlternateSetting, Integer.toHexString(this.ReadEndpoint), Integer.toHexString(this.WriteEndpoint));
        return msg;
    }

    public static final ComparableDevice getInstance(Usb_Device usb_Device, String serialNumber) {
        Usb_Config_Descriptor[] ucds = usb_Device.getConfig();
        if (ucds == null || ucds.length == 0) {
            return null;
        }
        Usb_Config_Descriptor ucd = ucds[0];
        Usb_Interface[] uis = ucd.getInterface();
        if (uis == null || uis.length == 0) {
            return null;
        }
        Usb_Interface ui = uis[0];
        Usb_Interface_Descriptor[] uids = ui.getAltsetting();
        if (uids == null || uids.length == 0) {
            return null;
        }
        Usb_Interface_Descriptor uid = uids[0];
        Usb_Endpoint_Descriptor[] ueds = uid.getEndpoint();
        if (ueds == null || ueds.length < 2) {
            return null;
        }
        ComparableDevice cd = new ComparableDevice(usb_Device, serialNumber);
        cd.bConfigurationValue = ucd.getBConfigurationValue();
        cd.bInterfaceNumber = uid.getBInterfaceNumber();
        cd.bAlternateSetting = -1;
        Usb_Endpoint_Descriptor[] usb_Endpoint_DescriptorArray = ueds;
        int n = ueds.length;
        int n2 = 0;
        while (n2 < n) {
            Usb_Endpoint_Descriptor ued = usb_Endpoint_DescriptorArray[n2];
            byte ep = ued.getBEndpointAddress();
            int adr = ued.getBEndpointAddress() & 0xFF;
            if ((ep & 0x80) > 0) {
                cd.ReadEndpoint = adr;
            } else {
                cd.WriteEndpoint = adr;
            }
            ++n2;
        }
        System.err.println(cd);
        return cd;
    }

    @Override
    public Usb_Device getUsb_Device() {
        return this.usb_Device;
    }

    @Override
    public String getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setUsb_Device(Usb_Device usb_Device) {
        this.usb_Device = usb_Device;
    }

    @Override
    public int compareTo(ComparableDevice o) {
        return this.serialNumber.compareTo(o.serialNumber);
    }
}

