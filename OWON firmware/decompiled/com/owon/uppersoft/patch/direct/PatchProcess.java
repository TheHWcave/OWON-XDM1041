/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.patch.direct;

import com.owon.uppersoft.common.comm.CommonCommand;
import com.owon.uppersoft.common.comm.DLListener;
import com.owon.uppersoft.common.comm.job.model.DataItem;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.EnterDownloadMode;
import com.owon.uppersoft.patch.direct.Reg;
import com.owon.uppersoft.patch.direct.SendFile;
import com.owon.uppersoft.patch.direct.SinglePatchFrame;
import com.owon.uppersoft.patch.direct.util.ZipAndDe;
import java.util.Iterator;
import java.util.List;

public class PatchProcess
implements Runnable {
    private DLListener dl;
    private SinglePatchFrame sp;
    private CommonCommand cc;
    private EnterDownloadMode dm;
    private boolean can;
    private List<DataItem> dfs;
    private Thread t;
    private ZipAndDe zad;
    private String psn;

    public PatchProcess(DLListener dl, SinglePatchFrame sp, EnterDownloadMode dm) {
        this.dl = dl;
        this.sp = sp;
        this.dm = dm;
        this.cc = new CommonCommand(dl);
    }

    protected boolean enterDownloadMode_byCOMMCall() {
        boolean b;
        byte[] req = new byte[]{21};
        byte[] res = new byte[1];
        this.cc.interact(req, req.length, res, res.length);
        boolean bl = b = res[0] == 55;
        if (b) {
            this.sp.setInfo(MsgCenter.getString("BR.success"));
        } else {
            this.sp.setInfo(MsgCenter.getString("BR.fail"));
        }
        return b;
    }

    @Override
    public void run() {
        block14: {
            this.can = false;
            try {
                this.dm.enter();
                if (this.can) {
                    return;
                }
                try {
                    Thread.sleep(this.sp.getReg().getWaitTime());
                    SendFile sf = new SendFile(this.dl);
                    Reg reg = this.sp.getReg();
                    if (reg.isEraseBOOT()) {
                        sf.eraseBOOT();
                        this.sp.setInfo(MsgCenter.getBundle().getString("BR.eraserBOOT"));
                        Thread.sleep(reg.getEraseBootTime());
                    }
                    if (reg.isEraseTABLE()) {
                        sf.eraseTABLE();
                        this.sp.setInfo(MsgCenter.getBundle().getString("BR.eraserTABLE"));
                        Thread.sleep(reg.getEraseTableTime());
                    }
                    if (reg.isEraseBAK()) {
                        sf.eraseBACKUP();
                        this.sp.setInfo(MsgCenter.getBundle().getString("BR.eraserBAK"));
                        Thread.sleep(reg.getEraseBackupTime());
                    }
                    this.handleUSBDown(sf, this.dfs);
                    if (!this.dfs.isEmpty()) {
                        this.handleUSBDown(sf, this.dfs);
                    }
                    Thread.sleep(500L);
                    if (this.sp.getReg().isEraseFLASH()) {
                        sf.eraseFLASH();
                        this.sp.setInfo(MsgCenter.getBundle().getString("BR.eraserFLASH"));
                        System.out.println("earse flash");
                        Thread.sleep(1000L);
                    }
                    boolean f = this.dfs.isEmpty();
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
                        this.sp.setInfo(msg);
                        break block14;
                    }
                    this.sp.setInfo(MsgCenter.getBundle().getString("DI.done"));
                    this.sp.nextStep();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            finally {
                this.zad.closeZip();
                this.sp.setPatchButtonEnable(true);
            }
        }
    }

    public void startPatch(ZipAndDe zad, List<DataItem> dfs, String psn) {
        this.dfs = dfs;
        this.zad = zad;
        this.psn = psn;
        if (dfs == null || dfs.size() == 0) {
            zad.closeZip();
            return;
        }
        this.sp.setPatchButtonEnable(false);
        this.t = new Thread(this);
        this.t.start();
    }

    public void cancel() {
        this.can = true;
        System.out.println("close");
    }

    protected void handleUSBDown(SendFile sf, List<DataItem> dfs) {
        Iterator<DataItem> idf = dfs.iterator();
        while (idf.hasNext()) {
            DataItem df = idf.next();
            try {
                if (df.getValue() > 0) {
                    if (!sf.sendFile(df)) continue;
                    idf.remove();
                    Thread.sleep(500L);
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

