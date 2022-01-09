/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.omap;

import com.owon.uppersoft.omap.usb.MainFrmUsb;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {
    public static void main(String[] args) {
        try {
            Display display = Display.getDefault();
            MainFrmUsb window = new MainFrmUsb();
            window.open();
            Shell shell = window.getShell();
            while (!shell.isDisposed()) {
                if (display.readAndDispatch()) continue;
                display.sleep();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

