/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.patch.direct.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LZSSOutputStream
extends FilterOutputStream {
    private static final int N = 4096;
    private static final int F = 16;
    private static final int THRESHOLD = 2;
    int state = 0;
    int i;
    int len;
    int r;
    int s;
    byte c;
    int last_match_length;
    int code_buf_ptr;
    byte mask;
    byte[] code_buf = new byte[17];
    int match_position;
    int match_length;
    int[] lson = new int[4097];
    int[] rson = new int[4353];
    int[] dad = new int[4097];
    byte[] text_buf = new byte[4111];

    public LZSSOutputStream(OutputStream ou) {
        super(ou);
    }

    private final void inittree() {
        int i = 4097;
        while (i <= 4352) {
            this.rson[i] = 4096;
            ++i;
        }
        i = 0;
        while (i < 4096) {
            this.dad[i] = 4096;
            ++i;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private final void pack_insertnode(int r) {
        int cmp = 1;
        int p = 4097 + (this.text_buf[r] & 0xFF);
        this.lson[r] = 4096;
        this.rson[r] = 4096;
        this.match_length = 0;
        while (true) {
            if (cmp >= 0) {
                if (this.rson[p] == 4096) {
                    this.rson[p] = r;
                    this.dad[r] = p;
                    return;
                }
                p = this.rson[p];
            } else {
                if (this.lson[p] == 4096) {
                    this.lson[p] = r;
                    this.dad[r] = p;
                    return;
                }
                p = this.lson[p];
            }
            int i = 1;
            while (i < 16) {
                cmp = (this.text_buf[r + i] & 0xFF) - (this.text_buf[p + i] & 0xFF);
                if (cmp != 0) break;
                ++i;
            }
            if (i <= this.match_length) continue;
            this.match_position = p;
            this.match_length = i;
            if (this.match_length >= 16) break;
        }
        this.dad[r] = this.dad[p];
        this.lson[r] = this.lson[p];
        this.rson[r] = this.rson[p];
        this.dad[this.lson[p]] = r;
        this.dad[this.rson[p]] = r;
        if (this.rson[this.dad[p]] == p) {
            this.rson[this.dad[p]] = r;
        } else {
            this.lson[this.dad[p]] = r;
        }
        this.dad[p] = 4096;
    }

    private final void pack_deletenode(int p) {
        int q;
        if (this.dad[p] == 4096) {
            return;
        }
        if (this.rson[p] == 4096) {
            q = this.lson[p];
        } else if (this.lson[p] == 4096) {
            q = this.rson[p];
        } else {
            q = this.lson[p];
            if (this.rson[q] != 4096) {
                while (this.rson[q = this.rson[q]] != 4096) {
                }
                this.rson[this.dad[q]] = this.lson[q];
                this.dad[this.lson[q]] = this.dad[q];
                this.lson[q] = this.lson[p];
                this.dad[this.lson[p]] = q;
            }
            this.rson[q] = this.rson[p];
            this.dad[this.rson[p]] = q;
        }
        this.dad[q] = this.dad[p];
        if (this.rson[this.dad[p]] == p) {
            this.rson[this.dad[p]] = q;
        } else {
            this.lson[this.dad[p]] = q;
        }
        this.dad[p] = 4096;
    }

    /*
     * Unable to fully structure code
     */
    private final void pack_write(int size, byte[] buf, int bufi, boolean last) throws IOException {
        skipme = false;
        if (this.state == 0) {
            this.code_buf[0] = 0;
            this.mask = 1;
            this.code_buf_ptr = 1;
            this.s = 0;
            this.r = 4080;
            this.inittree();
            this.len = 0;
        } else if (this.state == 1) {
            ++this.len;
        }
        if (this.state != 2) {
            while (this.len < 16 && size > 0) {
                this.text_buf[this.r + this.len] = buf[bufi++];
                if (--size == 0 && !last) {
                    this.state = 1;
                    return;
                }
                ++this.len;
            }
            if (this.len == 0) {
                return;
            }
            this.i = 1;
            while (this.i <= 16) {
                this.pack_insertnode(this.r - this.i);
                ++this.i;
            }
            this.pack_insertnode(this.r);
        } else {
            skipme = true;
        }
        do {
            if (!skipme) {
                if (this.match_length > this.len) {
                    this.match_length = this.len;
                }
                if (this.match_length <= 2) {
                    this.match_length = 1;
                    this.code_buf[0] = (byte)(this.code_buf[0] | this.mask);
                    this.code_buf[this.code_buf_ptr++] = this.text_buf[this.r];
                } else {
                    this.code_buf[this.code_buf_ptr++] = (byte)this.match_position;
                    this.code_buf[this.code_buf_ptr++] = (byte)(this.match_position >>> 4 & 240 | this.match_length - 3);
                }
                this.mask = (byte)(this.mask << 1);
                if (this.mask == 0) {
                    this.out.write(this.code_buf, 0, this.code_buf_ptr);
                    this.code_buf[0] = 0;
                    this.mask = 1;
                    this.code_buf_ptr = 1;
                }
                this.last_match_length = this.match_length;
                this.i = 0;
            }
            while (true) {
                block21: {
                    block20: {
                        if (skipme) break block20;
                        if (this.i >= this.last_match_length || size <= 0) ** GOTO lbl75
                        this.c = buf[bufi++];
                        if (--size == 0 && !last) {
                            this.state = 2;
                            return;
                        }
                        break block21;
                    }
                    skipme = false;
                }
                this.pack_deletenode(this.s);
                this.text_buf[this.s] = this.c;
                if (this.s < 15) {
                    this.text_buf[this.s + 4096] = this.c;
                }
                this.s = this.s + 1 & 4095;
                this.r = this.r + 1 & 4095;
                this.pack_insertnode(this.r);
                ++this.i;
            }
lbl-1000:
            // 1 sources

            {
                this.pack_deletenode(this.s);
                this.s = this.s + 1 & 4095;
                this.r = this.r + 1 & 4095;
                if (--this.len == 0) continue;
                this.pack_insertnode(this.r);
lbl75:
                // 3 sources

                ** while (this.i++ < this.last_match_length)
            }
lbl76:
            // 1 sources

        } while (this.len > 0);
        if (this.code_buf_ptr > 1) {
            this.out.write(this.code_buf, 0, this.code_buf_ptr);
            return;
        }
    }

    @Override
    public void write(byte[] zz, int ofs, int sz) throws IOException {
        this.pack_write(sz, zz, ofs, false);
    }

    @Override
    public void flush() throws IOException {
        this.pack_write(0, null, 0, true);
        super.flush();
    }

    @Override
    public void close() throws IOException {
        if (this.code_buf == null) {
            throw new IOException("LZSSOutputStream allready closed");
        }
        try {
            this.flush();
            this.out.close();
        }
        finally {
            this.out = null;
            this.code_buf = null;
            this.lson = null;
            this.rson = null;
            this.dad = null;
            this.text_buf = null;
        }
    }

    protected final void finalize() throws Throwable {
        try {
            this.flush();
        }
        finally {
            super.finalize();
        }
    }
}

