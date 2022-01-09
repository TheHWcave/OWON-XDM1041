/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.common.update;

import java.util.List;
import org.eclipse.swt.widgets.Shell;

public interface IUpdatable {
    public Shell getMainShell();

    public void notifyDestroy();

    public void close();

    public void startAgain();

    public List<String> getUpdatableServers();

    public String getRelativePath();

    public String getConfigurationDir();

    public String getProductVersion();
}

