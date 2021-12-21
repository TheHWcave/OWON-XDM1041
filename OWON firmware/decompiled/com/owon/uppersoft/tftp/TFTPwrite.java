/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.tftp;

import com.owon.uppersoft.tftp.TFTPpacket;

public final class TFTPwrite
extends TFTPpacket {
    private void initialize(String filename, String reqType) {
        this.length = 2 + filename.length() + 1 + reqType.length() + 1;
        this.message = new byte[this.length];
        this.put(0, (short)2);
        this.put(2, filename, (byte)0);
        this.put(2 + filename.length() + 1, reqType, (byte)0);
    }

    protected TFTPwrite() {
    }

    public TFTPwrite(String filename) {
        this.initialize(filename, "octet");
    }

    public TFTPwrite(String filename, String reqType) {
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

