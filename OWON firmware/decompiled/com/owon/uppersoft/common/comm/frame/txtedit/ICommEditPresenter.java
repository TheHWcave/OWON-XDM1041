/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.frame.txtedit;

import java.io.File;

public interface ICommEditPresenter {
    public void save(String var1);

    public String[] getModel();

    public void init();

    public void read(String var1);

    public boolean initView(String var1);

    public String loadData(File var1);
}

