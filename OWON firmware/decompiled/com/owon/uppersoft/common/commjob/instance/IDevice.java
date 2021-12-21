/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.ntb.usb.Usb_Device
 */
package com.owon.uppersoft.common.commjob.instance;

import ch.ntb.usb.Usb_Device;

public interface IDevice {
    public Usb_Device getUsb_Device();

    public String getSerialNumber();

    public void setSerialNumber(String var1);

    public int getBAlternateSetting();

    public int getBConfigurationValue();

    public int getBInterfaceNumber();

    public int getReadEndpoint();

    public int getWriteEndpoint();
}

