/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job;

public enum OSCTYPE {
    fp(1),
    tx(2),
    bm(3),
    hz(4),
    os(5),
    me(7),
    boot(8),
    help(9),
    bios(6),
    ag(29);

    private int t;

    private OSCTYPE(int t) {
        this.t = t;
    }

    public int value() {
        return this.t;
    }
}

