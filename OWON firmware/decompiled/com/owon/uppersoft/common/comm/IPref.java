/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm;

import java.util.Locale;

public interface IPref {
    public static final String ToolVer = "1.9.9";
    public static final int FILEBUFFERSIZE = 16384;
    public static final int b_2048 = 2048;
    public static final int b_2064 = 2064;
    public static final String TXSZIP = "txs.zip";
    public static final String BUNDLES = "bundles";
    public static final String PUBLICKEY = "public.key";
    public static final Locale[] LOCALES = new Locale[]{Locale.ENGLISH, Locale.SIMPLIFIED_CHINESE};
    public static final boolean checkTXT = false;
    public static final int MSG_PACKET = 1;
    public static final int DATA_PACKET = 2;
    public static final int MSG_READY = 1;
    public static final int MSG_READY_ACK = 2;
    public static final int MSG_DATA_ACK = 3;
    public static final int MSG_DATA_NAK = 4;
    public static final int MSG_FILE_TRANSFERED = 5;
    public static final int MSG_FILE_ABORT = 6;
    public static final int MSG_DATA_BLK = 9;
    public static final int EXIT = 8;
    public static final int MSG_UNSUPPORTED_CHIP = 7;
    public static final int MSG_PKT_SIZE = 12;
    public static final int MAX_DATA_SIZE = 50;
}

