/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job.model;

import com.owon.uppersoft.common.comm.job.model.DataItem;
import com.owon.uppersoft.common.comm.job.model.FileDataFileInputProvider;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileDataFile
extends DataItem {
    private FileDataFileInputProvider pvd;
    private FileChannel fc;

    public FileDataFile(String type, int value, String path, int bfsz) {
        this(type, value, new FileDataFileInputProvider(new File(path)), bfsz);
    }

    public FileDataFile(String type, int value, FileDataFileInputProvider pvd, int bfsz) {
        super(type, value, pvd, bfsz);
        this.pvd = pvd;
    }

    public String getPath() {
        return this.pvd.getPath();
    }

    public void setPath(String path) {
        this.pvd.setFile(new File(path));
    }

    @Override
    public long getLength() {
        return this.pvd.getFileLength();
    }

    @Override
    public ByteBuffer initbb() {
        this.fc = this.pvd.openChannel();
        return this.fc != null ? this.outb : null;
    }

    @Override
    public String toString() {
        return String.valueOf(super.toString()) + " Path: " + this.getPath();
    }

    @Override
    public boolean fillbb(ByteBuffer bb) throws IOException {
        if (this.fc == null) {
            return false;
        }
        bb.clear();
        return this.dofillbb(this.fc, bb);
    }

    protected boolean dofillbb(FileChannel fc, ByteBuffer bb) throws IOException {
        int rd = fc.read(bb);
        if (rd < 0) {
            return false;
        }
        if (rd == 0) {
            return this.dofillbb(fc, bb);
        }
        bb.flip();
        return true;
    }
}

