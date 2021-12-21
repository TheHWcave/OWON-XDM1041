/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.job;

import com.owon.uppersoft.common.comm.CommonCommand;
import com.owon.uppersoft.common.comm.job.BatchRunner;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import org.eclipse.swt.widgets.Text;

public class DownloadRequestThread
extends Thread {
    private byte command;
    private CommonCommand cc;
    private Text txtLog;
    private BatchRunner br;
    private Runnable r;
    private int timeCount = 0;

    public DownloadRequestThread(Runnable r, CommonCommand cc, byte command) {
        this.r = r;
        this.cc = cc;
        this.command = command;
    }

    @Override
    public void run() {
        byte[] bytes = new byte[]{this.command};
        byte[] responses = new byte[1];
        this.cc.interact(bytes, bytes.length, responses, responses.length);
        byte[] req = new byte[]{21};
        byte[] res = new byte[1];
        while (res[0] != 55 && this.timeCount < 500) {
            this.cc.interact(req, req.length, res, res.length);
            try {
                Thread.sleep(10L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++this.timeCount;
        }
        byte cmd = res[0];
        String msg = "21 ";
        if (cmd == 55) {
            msg = String.valueOf(msg) + ">> " + MsgCenter.getString("BR.success");
            System.out.println("enter usb mode success!");
        } else {
            msg = String.valueOf(msg) + MsgCenter.getString("BR.fail");
        }
        this.r.run();
    }
}

