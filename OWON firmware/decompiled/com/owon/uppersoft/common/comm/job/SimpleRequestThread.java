/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job;

import com.owon.uppersoft.common.comm.CommonCommand;

public class SimpleRequestThread
extends Thread {
    private byte command;
    private CommonCommand cc;

    public SimpleRequestThread(CommonCommand cc, byte command) {
        this.cc = cc;
        this.command = command;
    }

    @Override
    public void run() {
        byte[] bytes = new byte[]{this.command};
        byte[] responses = new byte[1];
        this.cc.interact(bytes, bytes.length, responses, responses.length);
        try {
            SimpleRequestThread.sleep(500L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

