/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.selfupgrade;

import com.owon.uppersoft.common.comm.EndianUtil;
import com.owon.uppersoft.common.comm.job.model.InputProvider;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HDSEntity
implements InputProvider {
    public byte[] content;

    public HDSEntity(byte[] content) {
        this.content = content;
    }

    public void updateTXTVerMes(int txtver) {
        EndianUtil.writeIntL(this.content, 0, txtver);
    }

    public void updateFunctionSwitch(int funSw) {
        EndianUtil.writeIntL(this.content, 4, funSw);
    }

    public void updateFunctionSwitch2(int funSw) {
        EndianUtil.writeIntL(this.content, 8, funSw);
    }

    public void updateDefaultLanguage(int lanSw) {
        EndianUtil.writeIntL(this.content, 12, lanSw);
    }

    public void updateLanguageSwitch(int defLan) {
        EndianUtil.writeIntL(this.content, 16, defLan);
    }

    public void updateVer(String ver) {
        EndianUtil.writeStringL(this.content, 20, ver, 40);
    }

    public void updateSn(String sn) {
        EndianUtil.writeStringL(this.content, 60, sn, 40);
    }

    public void updateOemLogo(String oenlogo) {
        EndianUtil.writeStringL(this.content, 100, oenlogo, 40);
    }

    public void updateBin() {
    }

    @Override
    public void closeInput() {
    }

    @Override
    public long getFileLength() {
        return this.content.length;
    }

    @Override
    public InputStream openInput() {
        return new ByteArrayInputStream(this.content);
    }
}

