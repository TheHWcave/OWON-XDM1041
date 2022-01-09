/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.common.update;

import com.owon.uppersoft.common.update.IUpdatable;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.widgets.Shell;

class DefaultUpdatable
implements IUpdatable {
    Shell s = new Shell();

    public DefaultUpdatable() {
        this.s.open();
    }

    @Override
    public Shell getMainShell() {
        return this.s;
    }

    @Override
    public String getRelativePath() {
        return "OWON_Oscilloscope.xml";
    }

    @Override
    public List<String> getUpdatableServers() {
        LinkedList<String> servers = new LinkedList<String>();
        servers.add("http://127.0.0.1/RCPUpdate/");
        return servers;
    }

    @Override
    public void close() {
    }

    @Override
    public void startAgain() {
    }

    public static void main(String[] args) {
    }

    @Override
    public String getConfigurationDir() {
        return null;
    }

    @Override
    public void notifyDestroy() {
    }

    @Override
    public String getProductVersion() {
        return "2.0.5.0";
    }
}

