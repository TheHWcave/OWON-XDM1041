/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.utils;

import com.owon.uppersoft.common.utils.FileUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipUtil {
    public static final int BufferSize = 2048;

    public static void unzip(File zipFile, String destName) {
        File f = new File(destName);
        ZipUtil.unzip(zipFile, f);
    }

    public static void unzip(File zipFile, File dest) {
        dest.mkdirs();
        try {
            ZipFile zf = new ZipFile(zipFile);
            Enumeration<? extends ZipEntry> zfe = zf.entries();
            byte[] buffer = new byte[2048];
            while (zfe.hasMoreElements()) {
                int l;
                ZipEntry ze = zfe.nextElement();
                String name = ze.getName();
                File file = new File(dest, name);
                FileUtil.deleteFile(file);
                if (ze.isDirectory()) {
                    file.mkdirs();
                    continue;
                }
                file.createNewFile();
                InputStream is = zf.getInputStream(ze);
                FileOutputStream fos = new FileOutputStream(file);
                while ((l = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, l);
                }
                fos.close();
                is.close();
            }
            zf.close();
        }
        catch (ZipException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ZipUtil.unzip(new File("c:/a.zip"), new File("c:/b"));
    }
}

