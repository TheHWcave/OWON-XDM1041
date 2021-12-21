/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.tftp;

import com.owon.uppersoft.tftp.TFTPpacket;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class TFTPdata
extends TFTPpacket {
    protected TFTPdata() {
    }

    public TFTPdata(int blockNumber, FileInputStream in) throws IOException {
        this.message = new byte[maxTftpPakLen];
        this.put(0, (short)3);
        this.put(2, (short)blockNumber);
        this.length = in.read(this.message, 4, maxTftpData) + 4;
    }

    public int blockNumber() {
        return this.get(2);
    }

    public void data(byte[] buffer) {
        buffer = new byte[this.length - 4];
        int i = 0;
        while (i < this.length - 4) {
            buffer[i] = this.message[i + 4];
            ++i;
        }
    }

    public int write(FileOutputStream out) throws IOException {
        out.write(this.message, 4, this.length - 4);
        return this.length - 4;
    }
}

