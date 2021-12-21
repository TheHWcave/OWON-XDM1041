/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.common.comm;

import com.owon.uppersoft.common.comm.TranslateTool;
import com.owon.uppersoft.common.comm.frame.MainFrm;
import com.owon.uppersoft.patch.direct.SinglePatchFrame;
import com.owon.uppersoft.patch.direct.util.EncryptFrm;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {
    public static final String ini = "/com/owon/uppersoft/common/comm/pref.ini";

    public static final void tool() {
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

    public static void main(String[] args) {
        Properties p = new Properties();
        try {
            InputStream is = Main.class.getResourceAsStream(ini);
            p.load(is);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String n = (String)p.get("launch");
        if (args.length > 0) {
            n = args[0];
        }
        if (n.equals("tool")) {
            Main.tool();
            return;
        }
        if (n.equals("patch")) {
            SinglePatchFrame.main(null);
            return;
        }
        if (n.equals("translate")) {
            new TranslateTool().browseBatch();
            return;
        }
        if (n.equals("encrypt")) {
            EncryptFrm.main(null);
            return;
        }
        if (n.equals("selfupdate")) {
            com.owon.uppersoft.selfupgrade.Main.main(null);
            return;
        }
        if (n.equals("omap")) {
            com.owon.uppersoft.omap.Main.main(null);
            return;
        }
        Main.tool();
    }
}

