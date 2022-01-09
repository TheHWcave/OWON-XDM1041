/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.frame.vds;

public enum WriteType {
    OS(1),
    FPGA(2),
    TXT(3),
    ZIP(4);

    private int t;

    private WriteType(int t) {
        this.t = t;
    }

    public int value() {
        return this.t;
    }
}

