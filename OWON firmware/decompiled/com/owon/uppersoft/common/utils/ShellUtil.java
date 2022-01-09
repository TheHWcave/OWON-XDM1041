/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.Point
 *  org.eclipse.swt.graphics.Rectangle
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.common.utils;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

public class ShellUtil {
    public static final void centerLoc(Shell shell) {
        Rectangle r = shell.getMonitor().getClientArea();
        int width = r.width;
        int height = r.height;
        Point p = shell.getSize();
        int x = p.x;
        int y = p.y;
        if (x > width) {
            x = width;
        }
        if (y > height) {
            y = height;
        }
        shell.setSize(x, y);
        shell.setLocation(width - x >> 1, height - y >> 1);
    }
}

