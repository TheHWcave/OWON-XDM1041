/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.SWT
 */
package com.owon.uppersoft.common.utils;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import org.eclipse.swt.SWT;

public class SystemPropertiesUtil {
    public static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String USER_HOME = System.getProperty("user.home");
    public static final String USER_NAME = System.getProperty("user.name");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");
    public static final String JAVA_HOME = System.getProperty("java.home");
    public static final String JAVA_VENDOR = System.getProperty("java.vendor");
    public static final String JAVA_VENDOR_URL = System.getProperty("java.vendor.url");
    public static final String JAVA_VERSION = System.getProperty("java.version");
    public static final String JAVA_CLASS_PATH = System.getProperty("java.class.path");
    public static final String JAVA_CLASS_VERSION = System.getProperty("java.class.version");
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_ARCH = System.getProperty("os.arch");
    public static final String OS_VERSION = System.getProperty("os.version");
    public static final String[] FONT_NAME_LIST = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    public static final Font[] FONT_LIST = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    public static final int ARCH_NUM = Integer.parseInt(OS_ARCH.substring(1));
    public static final String WIN_NAME = OS_NAME.substring(8);
    public static final double WIN_VER = Double.parseDouble(OS_VERSION);

    protected SystemPropertiesUtil() {
    }

    public static void SystemInfo() {
        System.getProperties().list(System.out);
    }

    public static final boolean isWin32System() {
        return OS_NAME.toLowerCase().contains("windows") && FILE_SEPARATOR.equals("\\");
    }

    public static final String getPlatfromType() {
        return SWT.getPlatform();
    }

    public static void main(String[] args) {
        SystemPropertiesUtil.SystemInfo();
        System.out.println(ARCH_NUM);
        System.out.println(WIN_NAME);
        System.out.println(WIN_VER);
    }
}

