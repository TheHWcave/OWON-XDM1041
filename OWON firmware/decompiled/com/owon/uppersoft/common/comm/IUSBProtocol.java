/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm;

public interface IUSBProtocol {
    public static final int DevAltinterface = -1;
    public static final int RetryTimeout = 2000;
    public static final int RetryTimeout2 = 20000;
    public static final int BufSize = 16384;
    public static final int DefaultBMPFileFlag = 1;
    public static final int ResponseOnSTARTDataLength = 12;
    public static final String[] CMDS = new String[]{"STARTBIN", "STARTBMP", "STARTMEMDEPTH"};
    public static final int USB_REQUEST_MEMDEPTH_index = 2;
    public static final int CMDAvailMode_SDS = 2;
    public static final int CMDAvailMode_TD = 1;
    public static final int CMDAvailMode_Normal = 0;
}

