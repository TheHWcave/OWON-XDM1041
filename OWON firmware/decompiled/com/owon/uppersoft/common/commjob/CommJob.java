/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.commjob;

import com.owon.uppersoft.common.commjob.instance.CommunicateFactory;
import com.owon.uppersoft.common.commjob.instance.ICommunication;

public class CommJob {
    private CommJob cj;
    private ICommunication comm;
    private boolean isConnected;

    public static void main(String[] args) {
        CommJob cj = new CommJob();
        cj.disConnect();
    }

    public CommJob getInstance() {
        if (this.cj == null) {
            this.cj = new CommJob();
        }
        return this.cj;
    }

    public boolean isConnected() {
        return this.comm.isConnected();
    }

    public void disConnect() {
        if (this.isConnected) {
            this.comm.close();
        }
    }

    public boolean connect(String type, String[] para) {
        this.comm = CommunicateFactory.getComm(type);
        this.isConnected = this.comm.open(para);
        return this.isConnected;
    }
}

