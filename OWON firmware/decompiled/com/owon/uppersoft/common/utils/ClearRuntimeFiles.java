/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.utils;

import java.io.File;

public class ClearRuntimeFiles {
    public static void clear() {
        ClearRuntimeFiles.delFolder("configuration\\org.eclipse.core.runtime");
    }

    public static void delFolder(String folderPath) {
        try {
            ClearRuntimeFiles.delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        int i = 0;
        while (i < tempList.length) {
            temp = path.endsWith(File.separator) ? new File(String.valueOf(path) + tempList[i]) : new File(String.valueOf(path) + File.separator + tempList[i]);
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                ClearRuntimeFiles.delAllFile(String.valueOf(path) + "/" + tempList[i]);
                ClearRuntimeFiles.delFolder(String.valueOf(path) + "/" + tempList[i]);
                flag = true;
            }
            ++i;
        }
        return flag;
    }
}

