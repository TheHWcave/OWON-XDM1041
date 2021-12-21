/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.selfupgrade;

import com.owon.uppersoft.selfupgrade.Entity;
import java.nio.ByteBuffer;

public class AWGEntity
extends Entity {
    public AWGEntity(ByteBuffer bb) {
        super(bb);
    }

    @Override
    public void updateVer(String ver) {
        while (ver.length() < 20) {
            ver = String.valueOf(ver) + "\u0000";
        }
        System.arraycopy(ver.getBytes(), 0, this.content, 20, 20);
    }

    @Override
    public void init() {
        this.ver = new String(this.content, 20, 20).trim();
        this.sn = new String(this.content, 40, 40).trim();
    }

    @Override
    protected boolean supportOSReboot() {
        return true;
    }
}

