/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.Resource
 */
package com.owon.uppersoft.common.utils;

import org.eclipse.swt.graphics.Resource;

public class DisposeUtil {
    public static final void tryDispose(Resource d) {
        if (d != null && !d.isDisposed()) {
            d.dispose();
        }
    }
}

