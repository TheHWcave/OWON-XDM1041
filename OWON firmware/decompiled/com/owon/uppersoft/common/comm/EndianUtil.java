/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm;

import java.util.Arrays;

public class EndianUtil {
    public static final int nextShortB(byte[] b, int p) {
        int ch2 = b[p++] & 0xFF;
        int ch1 = b[p] & 0xFF;
        return ch1 | ch2 << 8;
    }

    public static final int nextIntB(byte[] b, int p) {
        int ch4 = b[p++] & 0xFF;
        int ch3 = b[p++] & 0xFF;
        int ch2 = b[p++] & 0xFF;
        int ch1 = b[p] & 0xFF;
        return ch1 | ch2 << 8 | ch3 << 16 | ch4 << 24;
    }

    public static final int nextShortL(byte[] b, int p) {
        int ch2 = b[p++] & 0xFF;
        int ch1 = b[p] & 0xFF;
        return ch1 << 8 | ch2;
    }

    public static final int nextIntL(byte[] b, int p) {
        int ch4 = b[p++] & 0xFF;
        int ch3 = b[p++] & 0xFF;
        int ch2 = b[p++] & 0xFF;
        int ch1 = b[p] & 0xFF;
        return ch1 << 24 | ch2 << 16 | ch3 << 8 | ch4;
    }

    public static final String nextStringL(byte[] b, int p, int length) {
        byte[] bb = Arrays.copyOfRange(b, p, p + length);
        return new String(bb);
    }

    public static void main(String[] args) {
        byte[] b = new byte[4];
        int i = 0;
        b[i++] = -56;
        b[i++] = 0;
        b[i++] = 0;
        b[i++] = 0;
        System.out.println(EndianUtil.nextIntL(b, 0));
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

    public static final void writeIntL(byte[] b, int off, int v) {
        b[off++] = (byte)v;
        b[off++] = (byte)(v >>> 8);
        b[off++] = (byte)(v >>> 16);
        b[off] = (byte)(v >>> 24);
    }

    public static final void writeShortB(byte[] b, int off, int v) {
        b[off++] = (byte)(v >>> 8);
        b[off] = (byte)v;
    }

    public static final void writeShortL(byte[] b, int off, int v) {
        b[off++] = (byte)v;
        b[off++] = (byte)(v >>> 8);
    }

    public static final void writeStringL(byte[] b, int off, String v, int length) {
        byte[] bytes = v.getBytes();
        int i = 0;
        while (i < length) {
            b[off + i] = i < bytes.length ? bytes[i] : (byte)0;
            ++i;
        }
    }
}

