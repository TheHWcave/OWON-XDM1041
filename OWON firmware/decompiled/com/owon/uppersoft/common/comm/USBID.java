/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm;

public class USBID {
    public static boolean isAFG = false;

    public static void main(String[] args) {
    }

    public static short getPid() {
        if (isAFG) {
            return 851;
        }
        return 4660;
    }

    public static short getVid() {
        if (isAFG) {
            return 1689;
        }
        return 21317;
    }
}

