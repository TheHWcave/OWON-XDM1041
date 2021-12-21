/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.patch.direct.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LZSSInputStream
extends FilterInputStream {
    private static final int N = 4096;
    private static final int F = 16;
    private static final int THRESHOLD = 2;
    int state;
    int i;
    int j;
    int k;
    int r;
    int c;
    int flags;
    byte[] text_buf = new byte[4111];

    LZSSInputStream(InputStream i) {
        super(i);
    }

    @Override
    public final int read(byte[] buf, int bufi, int s) throws IOException {
        int size = 0;
        if (this.state == 0) {
            this.r = 4080;
            this.flags = 0;
        }
        while (s > 0) {
            if (this.state == 2 && this.k <= this.j) {
                this.c = this.text_buf[this.i + this.k & 0xFFF];
                this.text_buf[this.r++] = (byte)this.c;
                buf[bufi++] = (byte)this.c;
                this.r &= 0xFFF;
                ++this.k;
                --s;
                ++size;
                continue;
            }
            if (((this.flags >>>= 1) & 0x100) == 0) {
                this.c = this.in.read();
                if (this.c == -1) break;
                this.flags = this.c | 0xFF00;
            }
            if ((this.flags & 1) == 1) {
                this.c = this.in.read();
                if (this.c == -1) break;
                this.text_buf[this.r++] = (byte)this.c;
                this.r &= 0xFFF;
                buf[bufi++] = (byte)this.c;
                ++size;
                --s;
                this.state = 1;
                continue;
            }
            this.i = this.in.read();
            if (this.i == -1 || (this.j = this.in.read()) == -1) break;
            this.i |= (this.j & 0xF0) << 4;
            this.j = (this.j & 0xF) + 2;
            this.k = 0;
            this.state = 2;
        }
        if (size == 0) {
            return -1;
        }
        return size;
    }

    @Override
    public int read() throws IOException {
        byte[] b = new byte[1];
        int rc = this.read(b, 0, 1);
        if (rc == -1) {
            return -1;
        }
        byte z = b[0];
        return z & 0xFF;
    }

    @Override
    public void close() throws IOException {
        this.text_buf = null;
        super.close();
        this.in = null;
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public long skip(long n) {
        return 0L;
    }

    @Override
    public int available() {
        return 0;
    }

    @Override
    public void mark(int readlimit) {
    }

    @Override
    public void reset() throws IOException {
        throw new IOException("Reset is not supported");
    }
}

