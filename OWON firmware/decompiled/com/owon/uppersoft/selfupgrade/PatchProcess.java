/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.selfupgrade;

import com.owon.uppersoft.common.comm.CommonCommand;
import com.owon.uppersoft.common.comm.DLListener;
import com.owon.uppersoft.common.comm.job.model.DataItem;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.SendFile;
import com.owon.uppersoft.selfupgrade.MainFrm;
import java.util.Iterator;
import java.util.List;

public class PatchProcess
implements Runnable {
    private DLListener dl;
    private CommonCommand cc;
    private MainFrm mf;
    private boolean can;
    private List<DataItem> dfs;
    private Thread t;

    public PatchProcess(DLListener dl, MainFrm mf) {
        this.dl = dl;
        this.mf = mf;
        this.cc = new CommonCommand(dl);
    }

    protected boolean enterDownloadMode_byCOMMCall() {
        boolean b;
        byte[] req = new byte[]{21};
        byte[] res = new byte[1];
        this.cc.interact(req, req.length, res, res.length);
        boolean bl = b = res[0] == 55;
        if (b) {
            this.mf.logLn(MsgCenter.getString("BR.success"));
        } else {
            this.mf.showTips(MsgCenter.getString("BR.fail"));
        }
        return b;
    }

    @Override
    public void run() {
        this.can = false;
        try {
            while (!this.enterDownloadMode_byCOMMCall() && !this.can) {
                Thread.sleep(100L);
            }
            if (this.can) {
                return;
            }
            Thread.sleep(500L);
            SendFile sf = new SendFile(this.dl);
            this.handleUSBDown(sf, this.dfs);
            if (!this.dfs.isEmpty()) {
                this.handleUSBDown(sf, this.dfs);
            }
            boolean f = this.dfs.isEmpty();
            sf.eraseFLASH();
            this.mf.logLn(MsgCenter.getBundle().getString("BR.eraserFLASH"));
            Thread.sleep(500L);
            sf.reboot();
            System.out.println("reboot");
            if (!f || this.can) {
                Iterator<DataItem> idf = this.dfs.iterator();
                String names = "";
                while (idf.hasNext()) {
                    DataItem df = idf.next();
                    names = String.valueOf(names) + " " + df.getType();
                }
                String msg = String.valueOf(MsgCenter.getBundle().getString("DI.failed")) + names;
                this.mf.logLn(msg);
            } else {
                this.mf.logLn(MsgCenter.getBundle().getString("DI.done"));
            }
            this.mf.enable(true);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startPatch(List<DataItem> dfs) {
        this.dfs = dfs;
        this.t = new Thread(this);
        this.t.start();
    }

    public void cancel() {
        this.can = true;
        if (this.t != null) {
            try {
                this.t.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void handleUSBDown(SendFile sf, List<DataItem> dfs) {
        Iterator<DataItem> idf = dfs.iterator();
        while (idf.hasNext()) {
            DataItem df = idf.next();
            try {
                if (df.getValue() > 0) {
                    if (!sf.sendFile(df)) continue;
                    idf.remove();
                    Thread.sleep(100L);
                    continue;
                }
                sf.reboot();
                while (!this.enterDownloadMode_byCOMMCall()) {
                    Thread.sleep(300L);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

