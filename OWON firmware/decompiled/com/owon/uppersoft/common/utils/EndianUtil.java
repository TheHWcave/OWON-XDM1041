/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.utils;

public class EndianUtil {
    public static final short nextShortB(byte[] b, int p) {
        int ch2 = b[p++] & 0xFF;
        int ch1 = b[p] & 0xFF;
        return (short)(ch1 | ch2 << 8);
    }

    public static final int nextIntB(byte[] b, int p) {
        int ch4 = b[p++] & 0xFF;
        int ch3 = b[p++] & 0xFF;
        int ch2 = b[p++] & 0xFF;
        int ch1 = b[p] & 0xFF;
        return ch1 | ch2 << 8 | ch3 << 16 | ch4 << 24;
    }

    public static final short nextShortL(byte[] b, int p) {
        int ch2 = b[p++] & 0xFF;
        int ch1 = b[p] & 0xFF;
        return (short)(ch1 << 8 | ch2);
    }

    public static final int nextIntL(byte[] b, int p) {
        int ch4 = b[p++] & 0xFF;
        int ch3 = b[p++] & 0xFF;
        int ch2 = b[p++] & 0xFF;
        int ch1 = b[p] & 0xFF;
        return ch1 << 24 | ch2 << 16 | ch3 << 8 | ch4;
    }

    public static final long nextLongL(byte[] b, int p) {
        long tmp = EndianUtil.nextIntL(b, p);
        return tmp |= (long)EndianUtil.nextIntL(b, p += 4) << 32;
    }

    public static void main(String[] args) {
        long v = -2034550006L;
        byte[] bs = new byte[8];
        EndianUtil.writeLongL(bs, 0, v);
        System.out.println(EndianUtil.nextLongL(bs, 0));
    }

    public static final float nextFloatB(byte[] b, int p) {
        int v = EndianUtil.nextIntB(b, p);
        return Float.intBitsToFloat(v);
    }

    public static final float nextFloatL(byte[] b, int p) {
        int v = EndianUtil.nextIntL(b, p);
        return Float.intBitsToFloat(v);
    }

    public static final int int_reverse_Bytes(int i) {
        return Integer.reverseBytes(i);
    }

    public static final void writeIntB(byte[] b, int off, int v) {
        b[off++] = (byte)(v >>> 24);
        b[off++] = (byte)(v >>> 16);
        b[off++] = (byte)(v >>> 8);
        b[off] = (byte)v;
    }

    public static final void writeLongL(byte[] b, int off, long v) {
        int i = 0;
        while (i < 8) {
            b[off++] = (byte)v;
            v >>>= 8;
            ++i;
        }
    }
}

