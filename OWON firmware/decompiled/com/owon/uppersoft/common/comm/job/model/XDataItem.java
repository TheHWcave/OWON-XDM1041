/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job.model;

import com.owon.uppersoft.common.comm.job.model.DataItem;
import com.owon.uppersoft.common.comm.job.model.InputProvider;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

public class XDataItem
extends DataItem {
    private long length;
    private Cipher dc;
    private ByteBuffer inb;

    public XDataItem(String type, int value, long length, InputProvider pvd, Cipher dc, int insz, int bfsz) {
        super(type, value, pvd, bfsz);
        this.length = length;
        this.pvd = pvd;
        this.dc = dc;
        this.inb = ByteBuffer.allocate(insz);
    }

    @Override
    public long getLength() {
        return this.length;
    }

    @Override
    public String toString() {
        return String.valueOf(super.toString()) + " IN:" + this.inb.capacity() + " OUT:" + this.outb.capacity();
    }

    protected boolean dofillbb(InputStream is, ByteBuffer inb) throws IOException {
        byte[] array = inb.array();
        int len = array.length;
        int p = 0;
        while (true) {
            int rd;
            if ((rd = is.read(array, p, len - p)) == 0) {
                continue;
            }
            if (rd < 0) {
                if (p != 0) break;
                return false;
            }
            if ((p += rd) == len) break;
        }
        inb.limit(p);
        return true;
    }

    @Override
    public boolean fillbb(ByteBuffer bb) throws IOException {
        if (this.is == null) {
            return false;
        }
        bb.clear();
        this.inb.clear();
        boolean b = this.dofillbb(this.is, this.inb);
        if (b) {
            try {
                this.dc.doFinal(this.inb, bb);
                bb.flip();
            }
            catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            catch (BadPaddingException e) {
                e.printStackTrace();
            }
            catch (ShortBufferException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    @Override
    public void termbb() {
        this.pvd.closeInput();
    }
}

