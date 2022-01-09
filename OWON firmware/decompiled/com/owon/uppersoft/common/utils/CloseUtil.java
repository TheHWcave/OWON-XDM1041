/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.utils;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {
    public static final void tryClose(Closeable c) throws IOException {
        if (c == null) {
            return;
        }
        c.close();
    }
}

