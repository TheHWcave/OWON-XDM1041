/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.communication;

public interface IUSBStatus {
    public void usbStatus(boolean var1);

    public boolean checkStatus();

    public boolean shouldPopupCheck();
}

