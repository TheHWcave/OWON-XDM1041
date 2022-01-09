/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.patch.direct;

import com.owon.uppersoft.common.comm.CommonCommand;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.EnterDownloadMode;
import com.owon.uppersoft.patch.direct.SinglePatchFrame;

public class EnterDownloadMode_USB
implements EnterDownloadMode {
    private SinglePatchFrame sp;
    private CommonCommand cc;
    private boolean can;

    public EnterDownloadMode_USB(SinglePatchFrame sp) {
        this.sp = sp;
        this.cc = new CommonCommand(sp);
    }

    @Override
    public void enter() {
        this.can = false;
        while (!this.doEnter() && !this.can) {
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean doEnter() {
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
    public void cancel() {
        this.can = true;
    }
}

