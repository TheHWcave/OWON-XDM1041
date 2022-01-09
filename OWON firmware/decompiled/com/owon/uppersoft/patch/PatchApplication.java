/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.equinox.app.IApplication
 *  org.eclipse.equinox.app.IApplicationContext
 */
package com.owon.uppersoft.patch;

import com.owon.uppersoft.common.comm.Main;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class PatchApplication
implements IApplication {
    public Object start(IApplicationContext context) throws Exception {
        String[] args = (String[])context.getArguments().get("application.args");
        Main.main(args);
        return IApplication.EXIT_OK;
    }

    public void stop() {
    }
}

