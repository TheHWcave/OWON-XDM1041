/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.omap;

import com.owon.uppersoft.common.logger.LoggerUtil;
import com.owon.uppersoft.common.utils.StringPool;
import com.owon.uppersoft.omap.CRC16;
import com.owon.uppersoft.omap.ScriptEntry;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YModem {
    public static final byte SOH = 1;
    public static final byte STX = 2;
    public static final byte EOT = 4;
    public static final byte ACK = 6;
    public static final byte BS = 8;
    public static final byte NAK = 21;
    public static final byte CAN = 24;
    public static final byte Byte_C = 67;
    public static final int XMODEM_CRC_BLOCK_SIZE = 128;
    public static final int XMODEM_1K_BLOCK_SIZE = 1024;
    public static final int START_OF_COMMUNICATION = -1;
    public static final int END_OF_COMMUNICATION = -3;
    public static final int PASSIVE_CANCEL = -5;
    public static final int TIMEOUT_CANCEL = -6;
    public static final int ACTIVE_CANCEL = -7;
    public static final int TERMINATED = -10;
    public static final int END_OF_TRANSMISSION = -12;
    public static final int ERROR = -20;
    public static final int BLOCK_NO_ERROR = -24;
    public static final int CRC_ERROR = -25;
    public static final int UNKNOWN_ERROR = -30;
    public static final int InitBufferSize = 1029;
    private static final int bufferSize = 1029;
    private InputStream is;
    private OutputStream os;
    private int BlockNo;
    private byte[] bigData;
    private int fileLength;
    private int blockLength;
    private String fileType;
    private String fileName;
    private int injectDataIndex = 0;
    private int transactionStatus;
    private int correctRate;
    private ScriptEntry se;
    public static final Logger logger = LoggerUtil.getConsoleLogger(YModem.class.getName(), Level.FINE);
    private byte[] b;
    public boolean isFinish;

    public YModem(InputStream is, OutputStream os, ScriptEntry se) {
        this.is = is;
        this.os = os;
        this.se = se;
    }

    public void onData(byte data) {
        switch (data) {
            case 6: {
                logger.info(String.valueOf(StringPool.LINE_SEPARATOR) + "[YModem: ACK]: ACK");
                int avi = this.se.available();
                System.out.print(avi);
                if (avi == 0) {
                    this.sendFinish();
                    return;
                }
                if (avi >= 1024) {
                    this.blockLength = 1024;
                    this.transactionStatus = 2;
                } else {
                    this.blockLength = 128;
                    this.transactionStatus = 1;
                }
                this.b = new byte[this.blockLength];
                this.se.read(this.b);
                this.send(this.b);
                break;
            }
            case 24: {
                logger.fine("can!");
                break;
            }
            default: {
                logger.fine("others:" + data);
            }
        }
    }

    private void send(byte[] b) {
        int crc = CRC16.encode(b);
        int crcHi = crc / 256;
        int crcLow = crc % 256;
        try {
            this.os.write(this.transactionStatus);
            this.os.write(this.BlockNo);
            this.os.write(~this.BlockNo);
            ++this.BlockNo;
            this.os.write(b);
            this.os.write(crcHi);
            this.os.write(crcLow);
            this.os.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileType() {
        return this.fileType;
    }

    public byte[] getBigData() {
        return this.bigData;
    }

    public void startTransaction() {
        this.BlockNo = 0;
        this.bigData = null;
        this.b = new byte[128];
        this.se.initBB();
        String s = String.valueOf(this.se.getName()) + "\u0000" + this.se.getSize();
        System.out.println(s);
        byte[] tep = s.getBytes();
        System.arraycopy(tep, 0, this.b, 0, tep.length);
        this.transactionStatus = 1;
        this.send(this.b);
    }

    public void cancelTransaction() {
        byte[] can = new byte[]{24, 24, 24};
        try {
            this.os.write(can);
            this.os.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(String.valueOf(StringPool.LINE_SEPARATOR) + "[YModem: can]: CAN, CAN, CAN");
        this.bigData = null;
    }

    public void sendFinish() {
        try {
            this.os.write(4);
            this.os.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(String.valueOf(StringPool.LINE_SEPARATOR) + "[YModem: EOT]: EOT");
        this.isFinish = true;
    }
}

