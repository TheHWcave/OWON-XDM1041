/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job.model;

import com.owon.uppersoft.common.comm.job.model.InputProvider;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class XDataItemInputProvider
implements InputProvider {
    private ZipFile zf;
    private ZipEntry ze;

    public XDataItemInputProvider(ZipFile zf, ZipEntry ze) {
        this.zf = zf;
        this.ze = ze;
    }

    @Override
    public InputStream openInput() {
        try {
            return this.zf.getInputStream(this.ze);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPathName() {
        return this.ze.getName();
    }

    @Override
    public long getFileLength() {
        return this.ze.getSize();
    }

    @Override
    public void closeInput() {
    }
}

