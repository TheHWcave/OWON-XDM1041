/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.program.Program
 */
package com.owon.uppersoft.common.utils;

import com.owon.uppersoft.common.utils.SystemPropertiesUtil;
import java.io.File;
import java.io.IOException;
import org.eclipse.swt.program.Program;

public class LaunchManager {
    public static boolean tryOpenFile(File file) {
        if (file.exists()) {
            try {
                return Program.launch((String)file.getCanonicalPath());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean tryLaunchByName(String name) {
        return Program.launch((String)name);
    }

    public static void main(String[] args) {
        SystemPropertiesUtil.isWin32System();
        LaunchManager.tryLaunchByName("launcher");
    }
}

