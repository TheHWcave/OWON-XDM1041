/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job;

public interface IStatic {
    public static final String confDir = "conf";
    public static final int STATUS_UNKNOWN = 0;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAIL = 2;
    public static final int STATUS_MUTI_FILE = 3;
    public static final int ENCRYPTION_COMMAND = 19;
    public static final byte REBOOT_COMMAND = 20;
    public static final byte USB_MODE_COMMAND = 21;
    public static final byte RUN_OS_COMMAND = 22;
    public static final byte ERASE_BIOS_COMMAND = 23;
    public static final byte ERASE_BOOT_COMMAND = 24;
    public static final byte ERASE_FLASH_COMMAND = 25;
    public static final byte ERASE_TABLE_COMMAND = 27;
    public static final byte ERASE_BAK_COMMAND = 28;
    public static final byte SINGLE_SN_COMMAND = 26;
    public static final int NAK_RESPONSE_COMMAND = 55;
    public static final String ENCRYPTION_NAME = "en";
    public static final int TRANSMIT_HEADER_BYTELENGTH = 32;
}

