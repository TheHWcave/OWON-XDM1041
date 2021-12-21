/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.selfupgrade;

import com.owon.uppersoft.common.comm.job.model.DataItem;
import com.owon.uppersoft.common.comm.job.model.InputProvider;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TxDataItem
extends DataItem {
    public TxDataItem(String type, int value, InputProvider pvd, int bfsz) {
        super(type, value, pvd, bfsz);
    }

    @Override
    public boolean fillbb(ByteBuffer bb) throws IOException {
        if (this.is == null) {
            return false;
        }
        bb.clear();
        byte[] array = bb.array();
        int rd = this.is.read(array);
        if (rd == 0) {
            this.fillbb(bb);
        } else if (rd < 0) {
            return false;
        }
        bb.limit(rd);
        return true;
    }

    @Override
    public long getLength() {
        return this.pvd.getFileLength();
    }
}

