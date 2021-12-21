/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.core.runtime.Plugin
 *  org.osgi.framework.BundleContext
 */
package com.owon.uppersoft.patch;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class Activator
extends Plugin {
    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    public void start(BundleContext context) throws Exception {
        Activator.context = context;
    }

    public void stop(BundleContext context) throws Exception {
        Activator.context = null;
    }
}

