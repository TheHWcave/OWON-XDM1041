/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.utils;

import com.owon.uppersoft.common.utils.TimeMeasure;
import java.io.File;

public class FileUtil {
    public static String getExtension(File f) {
        return f != null ? FileUtil.getExtension(f.getName()) : "";
    }

    public static String getExtension(String filename) {
        return FileUtil.getExtension(filename, "");
    }

    public static String getExtension(String filename, String defExt) {
        if (filename != null && filename.length() > 0) {
            int i = filename.lastIndexOf(".");
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1);
            }
            return defExt;
        }
        return defExt;
    }

    public static void checkPath(String path) {
        FileUtil.checkPath(new File(path));
    }

    public static void checkPath(File file) {
        if (file.exists()) {
            return;
        }
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
    }

    public static void deleteFile(String path) {
        FileUtil.deleteFile(new File(path));
    }

    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] files;
            File[] fileArray = files = file.listFiles();
            int n = files.length;
            int n2 = 0;
            while (n2 < n) {
                File f = fileArray[n2];
                FileUtil.deleteFile(f);
                ++n2;
            }
            file.delete();
            return;
        }
    }

    public static void replaceFile(File src, File dest) {
        if (dest.exists()) {
            FileUtil.deleteFile(dest);
        }
        src.renameTo(dest);
    }

    public static void replaceInFile(File src, File destP) {
        File dest = new File(destP, src.getName());
        FileUtil.replaceFile(src, dest);
    }

    public static void main(String[] args) {
        TimeMeasure tm = new TimeMeasure();
        tm.start();
        FileUtil.deleteFile("F:/Alisoft");
        tm.stop();
        System.out.println(tm.measure());
    }
}

