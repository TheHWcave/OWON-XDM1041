/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.omap;

import java.io.UnsupportedEncodingException;

public class CRC16 {
    public static int encode(byte[] bytes) {
        return CRC16.encode(bytes, 0, bytes.length);
    }

    public static int encode(byte[] bytes, int offset, int length) {
        int crc = 0;
        byte tmp = 0;
        int i = offset;
        while (i < offset + length) {
            tmp = bytes[i];
            crc ^= tmp << 8;
            int j = 0;
            while (j < 8) {
                crc = (crc & 0x8000) != 0 ? (crc & Short.MAX_VALUE) << 1 ^ 0x1021 : (crc & Short.MAX_VALUE) << 1;
                ++j;
            }
            ++i;
        }
        return crc;
    }

    public static void main_hide(String[] args) {
        byte[] byArray = new byte[128];
        byArray[0] = 115;
        byArray[1] = 112;
        byArray[2] = 98;
        byArray[3] = 48;
        byArray[4] = 46;
        byArray[5] = 98;
        byArray[6] = 105;
        byArray[7] = 110;
        byArray[9] = 50;
        byArray[10] = 53;
        byArray[11] = 48;
        byArray[12] = 52;
        byte[] b1 = byArray;
        System.out.println(CRC16.encode(b1) == 8414);
        b1[3] = 49;
        System.out.println(CRC16.encode(b1, 0, b1.length) == 38249);
        b1[3] = 50;
        System.out.println(CRC16.encode(b1) == 23441);
        b1[3] = 51;
        System.out.println(CRC16.encode(b1, 0, b1.length) == 60966);
        b1[3] = 52;
        System.out.println(CRC16.encode(b1) == 54848);
        try {
            String ss = new String(b1, "UTF-8");
            System.out.println(ss);
            int zero = 0;
            int index = ss.indexOf(zero);
            System.out.println(index);
            ++index;
            index = ss.indexOf(zero, index);
            System.out.println(index);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

