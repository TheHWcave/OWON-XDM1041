/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job;

public enum MSOTYPE {
    fp(11),
    la(12),
    bm(13),
    hz(14),
    tx(16),
    os(17),
    bi(18),
    me_osc(56),
    me_la(57);

    private int t;

    private MSOTYPE(int t) {
        this.t = t;
    }

    public int value() {
        return this.t;
    }
}

