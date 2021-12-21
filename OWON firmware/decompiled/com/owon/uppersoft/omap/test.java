/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.omap;

import com.owon.uppersoft.patch.direct.util.CRC;

public class test {
    public static void main(String[] args) {
        int i = 1;
        System.out.print(Integer.toHexString(~i));
    }

    public static byte[] doit(byte[] b) {
        byte[] re = new byte[b.length];
        re[0] = b[0];
        re[1] = b[1];
        int i = 2;
        while (i < b.length) {
            byte[] b1 = new byte[i - 1];
            System.arraycopy(b, 0, b1, 0, i - 1);
            re[i] = (byte)(CRC.doCRC32(b1) ^ (long)b[i]);
            ++i;
        }
        return re;
    }
}

