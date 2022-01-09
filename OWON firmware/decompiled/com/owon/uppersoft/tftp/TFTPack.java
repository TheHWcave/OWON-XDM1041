/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.tftp;

import com.owon.uppersoft.tftp.TFTPpacket;

public final class TFTPack
extends TFTPpacket {
    protected TFTPack() {
    }

    public TFTPack(int blockNumber) {
        this.length = 4;
        this.message = new byte[this.length];
        this.put(0, (short)4);
        this.put(2, (short)blockNumber);
    }

    public int blockNumber() {
        return this.get(2);
    }
}

