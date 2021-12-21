/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job.model;

import com.owon.uppersoft.common.comm.job.model.DataItem;
import com.owon.uppersoft.common.comm.job.model.XDataItemInputProvider;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ZipDataItem
extends DataItem {
    public ZipDataItem(String type, int value, XDataItemInputProvider pvd, int bfsz) {
        super(type, value, pvd, bfsz);
    }

    @Override
    public String toString() {
        return String.valueOf(super.toString()) + " path: " + ((XDataItemInputProvider)this.pvd).getPathName();
    }

    @Override
    public boolean fillbb(ByteBuffer bb) throws IOException {
        if (this.is == null) {
            return false;
        }
        bb.clear();
        return this.dofillbb(this.is, bb);
    }

    protected boolean dofillbb(InputStream is, ByteBuffer bb) throws IOException {
        int rd = is.read(bb.array());
        if (rd < 0) {
            return false;
        }
        if (rd == 0) {
            return this.dofillbb(is, bb);
        }
        bb.limit(rd);
        return true;
    }

    @Override
    public long getLength() {
        return this.pvd.getFileLength();
    }
}

