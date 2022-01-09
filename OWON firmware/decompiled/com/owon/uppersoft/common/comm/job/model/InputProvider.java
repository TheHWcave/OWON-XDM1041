/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job.model;

import java.io.InputStream;

public interface InputProvider {
    public long getFileLength();

    public InputStream openInput();

    public void closeInput();
}

