/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.tftp;

import com.owon.uppersoft.tftp.TFTPpacket;

public final class TFTPread
extends TFTPpacket {
    private void initialize(String filename, String reqType) {
        this.length = 2 + filename.length() + 1 + reqType.length() + 1;
        this.message = new byte[this.length];
        this.put(0, (short)1);
        this.put(2, filename, (byte)0);
        this.put(2 + filename.length() + 1, reqType, (byte)0);
    }

    protected TFTPread() {
    }

    public TFTPread(String filename) {
        this.initialize(filename, "octet");
    }

    public TFTPread(String filename, String reqType) {
        this.initialize(filename, reqType);
    }

    public String fileName() {
        return this.get(2, (byte)0);
    }

    public String requestType() {
        String fname = this.fileName();
        return this.get(2 + fname.length() + 1, (byte)0);
    }
}

