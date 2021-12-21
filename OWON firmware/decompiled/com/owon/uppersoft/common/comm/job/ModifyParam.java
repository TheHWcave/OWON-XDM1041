/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job;

import com.owon.uppersoft.common.comm.EndianUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ModifyParam {
    public byte[] readPathContent(String fileName) {
        File file = new File(fileName);
        byte[] data = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            data = new byte[fis.available()];
            fis.read(data);
            fis.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public byte[] changeByte(int value, byte[] b, String path, int pos) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            EndianUtil.writeIntL(b, pos, value);
            fos.write(b);
            fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public byte[] changeIndefiniteByte(String str, byte[] b, String path, int pos, int length) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            EndianUtil.writeStringL(b, pos, str, length);
            fos.write(b);
            fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
}

