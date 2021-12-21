/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.tftp;

import com.owon.uppersoft.tftp.TFTPpacket;

public final class TFTPerror
extends TFTPpacket {
    protected TFTPerror() {
    }

    public TFTPerror(int number, String message) {
        this.length = 4 + message.length() + 1;
        this.message = new byte[this.length];
        this.put(0, (short)5);
        this.put(2, (short)number);
        this.put(4, message, (byte)0);
    }

    public int number() {
        return this.get(2);
    }

    public String message() {
        return this.get(4, (byte)0);
    }
}

