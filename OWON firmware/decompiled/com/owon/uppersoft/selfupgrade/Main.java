/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.selfupgrade;

import com.owon.uppersoft.selfupgrade.MainFrm;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {
    public static void main(String[] args) {
        try {
            Display display = Display.getDefault();
            MainFrm window = new MainFrm();
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

