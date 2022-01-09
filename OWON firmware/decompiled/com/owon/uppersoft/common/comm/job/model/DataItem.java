/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job.model;

import com.owon.uppersoft.common.comm.job.model.InputProvider;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public abstract class DataItem {
    private String type = "";
    private int value = -1;
    private int progress = 0;
    private boolean selected;
    private int status = 0;
    protected InputProvider pvd;
    protected ByteBuffer outb;
    protected InputStream is;

    public DataItem(String type, int value, InputProvider pvd, int bfsz) {
        this.type = type;
        this.value = value;
        this.pvd = pvd;
        this.outb = ByteBuffer.allocate(bfsz);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String typ) {
        this.type = typ;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        String n = "type: " + this.getType() + " value: " + this.getValue() + " length: " + this.getLength();
        return n;
    }

    public abstract long getLength();

    public ByteBuffer initbb() {
        this.is = this.pvd.openInput();
        return this.is != null ? this.outb : null;
    }

    public abstract boolean fillbb(ByteBuffer var1) throws IOException;

    public void termbb() {
        this.pvd.closeInput();
    }
}

