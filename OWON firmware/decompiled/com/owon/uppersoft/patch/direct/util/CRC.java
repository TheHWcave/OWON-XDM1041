/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.patch.direct.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.CRC32;

public class CRC {
    public static int doCRC16(String s) {
        return CRC.doCRC16(new File(s));
    }

    public static int doCRC16(String s, int len) {
        return CRC.doCRC16(new File(s), len);
    }

    public static int doCRC16(File f) {
        int fileSize = (int)f.length();
        return CRC.doCRC16(f, fileSize);
    }

    public static int doCRC16(File f, int fileSize) {
        byte[] bytes = new byte[fileSize];
        try {
            FileInputStream fis = new FileInputStream(f);
            fis.read(bytes);
            fis.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return CRC.doCRC16(bytes);
    }

    public static int doCRC16(byte[] bytes) {
        int len = bytes.length;
        return CRC.doCRC16(bytes, len);
    }

    public static int doCRC16(byte[] bytes, int len) {
        int crc = 0;
        byte tmp = 0;
        int i = 0;
        while (i < len) {
            tmp = bytes[i];
            crc ^= tmp << 8;
            int j = 0;
            while (j < 8) {
                crc = (crc & 0x8000) != 0 ? crc << 1 ^ 0x1021 : (crc <<= 1);
                ++j;
            }
            ++i;
        }
        return crc;
    }

    public static long doCRC32(String s) {
        return CRC.doCRC32(new File(s));
    }

    public static long doCRC32(File f) {
        int fileSize = (int)f.length();
        byte[] bytes = new byte[fileSize];
        try {
            FileInputStream fis = new FileInputStream(f);
            fis.read(bytes);
            fis.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return CRC.doCRC32(bytes);
    }

    public static long doCRC32(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return crc32.getValue();
    }
}

