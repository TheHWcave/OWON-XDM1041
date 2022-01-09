/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.ntb.usb.USBException
 *  ch.ntb.usb.USBTimeoutException
 */
package com.owon.uppersoft.selfupgrade;

import ch.ntb.usb.USBException;
import ch.ntb.usb.USBTimeoutException;
import com.owon.uppersoft.common.comm.CDevice;
import com.owon.uppersoft.common.comm.DLEvent;
import com.owon.uppersoft.common.comm.DLListener;
import com.owon.uppersoft.common.comm.IDevice;
import com.owon.uppersoft.common.comm.USBID;
import com.owon.uppersoft.common.comm.job.model.DataItem;
import com.owon.uppersoft.selfupgrade.AWGEntity;
import com.owon.uppersoft.selfupgrade.Entity;
import com.owon.uppersoft.selfupgrade.HDSEntity;
import com.owon.uppersoft.selfupgrade.ODPEntity;
import com.owon.uppersoft.selfupgrade.OSCEntity;
import com.owon.uppersoft.selfupgrade.VDSEntity;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.List;

public class USBCommunication {
    private static final int timeout = 3000;
    private static final int acktimeout = 200000;
    private byte[] startCommand = new byte[32];
    private IDevice iDevice;
    private CDevice dev;
    private boolean transactionCanceled;
    private boolean reopenOnTimeout = true;
    private DLListener dll;
    private DataItem di;
    private Entity en;
    private String errKey;
    private HDSEntity hdsEntity;

    public USBCommunication(DLListener dll) {
        this.dll = dll;
    }

    public USBCommunication() {
    }

    public void setDataItem(DataItem di) {
        this.di = di;
    }

    public boolean getData() {
        if (this.di == null) {
            return false;
        }
        boolean sucess = this.doGetData();
        return sucess;
    }

    public void addEncryptionMark(String mark) {
        this.addEncryptionMark(mark, 5);
    }

    public void addEncryptionMark(String mark, int index) {
        if (mark == null) {
            return;
        }
        char[] cs = mark.toCharArray();
        int length = cs.length;
        int i = index;
        int j = 0;
        while (j < length) {
            this.startCommand[i] = (byte)cs[j];
            ++i;
            ++j;
        }
        length = this.startCommand.length;
        while (i < length) {
            this.startCommand[i++] = 0;
        }
    }

    public Entity getEntity() {
        return this.en;
    }

    public HDSEntity getHDSEntity() {
        return this.hdsEntity;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean doGetTxt() {
        long time1 = Calendar.getInstance().getTimeInMillis();
        this.startCommand = "STARTDEBUGTXT".getBytes();
        int bufSize = 1024;
        byte[] buf = new byte[bufSize];
        try {
            try {
                if (this.iDevice == null) {
                    System.out.println("scaning..");
                    List<IDevice> ld = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
                    System.out.println(ld.size());
                    if (ld.size() <= 0) {
                        this.errKey = "nofinddev";
                        return false;
                    }
                    this.iDevice = ld.get(0);
                }
                this.dev = CDevice.getDevice(USBID.getVid(), USBID.getPid());
                CDevice.simulateOpen(this.dev, this.iDevice);
                int WriteEndpoint = this.iDevice.getWriteEndpoint();
                int ReadEndpoint = this.iDevice.getReadEndpoint();
                this.dev.writeBulk(WriteEndpoint, this.startCommand, this.startCommand.length, 3000, this.reopenOnTimeout);
                int rn = this.dev.readBulk(ReadEndpoint, buf, bufSize, 3000, this.reopenOnTimeout);
                if (rn < 4) {
                    this.setLogln("read filelength err");
                    return false;
                }
                int fileLen = buf[0] & 0xFF;
                fileLen += (buf[1] & 0xFF) << 8;
                fileLen += (buf[2] & 0xFF) << 16;
                this.setLogln("file len=" + (fileLen += (buf[3] & 0xFF) << 24));
                if (fileLen > 1000000) {
                    System.err.println("fileLen err");
                    return false;
                }
                ByteBuffer bb = ByteBuffer.allocate(fileLen);
                rn = this.dev.readBulk(ReadEndpoint, buf, bufSize, 3000, this.reopenOnTimeout);
                bb.put(buf, 20, rn - 20);
                System.out.println(new String(buf, 0, 20));
                byte ext = buf[19];
                System.out.println("ext:" + ext);
                switch (ext) {
                    case 0: {
                        this.en = new OSCEntity(bb);
                        break;
                    }
                    case 1: {
                        this.en = new AWGEntity(bb);
                        break;
                    }
                    case 2: {
                        this.en = new VDSEntity(bb);
                        break;
                    }
                    case 52: {
                        this.en = new ODPEntity(bb);
                        break;
                    }
                    default: {
                        this.errKey = "errType";
                        return false;
                    }
                }
                int prg = rn;
                while (true) {
                    if (prg >= fileLen) {
                        long time2 = Calendar.getInstance().getTimeInMillis() - time1;
                        double costT = (double)time2 / 1000.0;
                        double rate = (double)(fileLen >> 10) / costT;
                        this.setLogln(String.format("rate:%.2fkB/s,length:%dkB,%dms\n", rate, fileLen >> 10, time2));
                        this.setCommSpeed(rate);
                        this.setLogln("comm finished");
                        return true;
                    }
                    rn = this.dev.readBulk(ReadEndpoint, buf, bufSize, 3000, this.reopenOnTimeout);
                    prg += rn;
                    bb.put(buf, 0, rn);
                }
            }
            finally {
                this.endTransaction();
            }
        }
        catch (USBTimeoutException e) {
            e.printStackTrace();
            this.errKey = "errcomm";
            return false;
        }
        catch (USBException e) {
            e.printStackTrace();
            this.errKey = "errcomm";
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean doGetHdsBin() {
        long time1 = Calendar.getInstance().getTimeInMillis();
        this.startCommand = "STARTDEBUGTXT".getBytes();
        int bufSize = 1024;
        byte[] buf = new byte[bufSize];
        try {
            try {
                if (this.iDevice == null) {
                    System.out.println("scaning..");
                    List<IDevice> ld = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
                    System.out.println(ld.size());
                    if (ld.size() <= 0) {
                        this.errKey = "nofinddev";
                        return false;
                    }
                    this.iDevice = ld.get(0);
                }
                this.dev = CDevice.getDevice(USBID.getVid(), USBID.getPid());
                CDevice.simulateOpen(this.dev, this.iDevice);
                int WriteEndpoint = this.iDevice.getWriteEndpoint();
                int ReadEndpoint = this.iDevice.getReadEndpoint();
                this.dev.writeBulk(WriteEndpoint, this.startCommand, this.startCommand.length, 3000, this.reopenOnTimeout);
                int rn = this.dev.readBulk(ReadEndpoint, buf, bufSize, 3000, this.reopenOnTimeout);
                if (rn < 4) {
                    this.setLogln("read filelength err");
                    return false;
                }
                int fileLen = buf[0] & 0xFF;
                fileLen += (buf[1] & 0xFF) << 8;
                fileLen += (buf[2] & 0xFF) << 16;
                this.setLogln("file len=" + (fileLen += (buf[3] & 0xFF) << 24));
                if (fileLen > 1000000) {
                    System.err.println("fileLen err");
                    return false;
                }
                ByteBuffer bb = ByteBuffer.allocate(fileLen - 20);
                rn = this.dev.readBulk(ReadEndpoint, buf, bufSize, 3000, this.reopenOnTimeout);
                bb.put(buf, 20, rn - 20);
                System.out.println(new String(buf, 0, 20));
                this.hdsEntity = new HDSEntity(bb.array());
                int prg = rn;
                while (true) {
                    if (prg >= fileLen) {
                        long time2 = Calendar.getInstance().getTimeInMillis() - time1;
                        double costT = (double)time2 / 1000.0;
                        double rate = (double)(fileLen >> 10) / costT;
                        this.setLogln(String.format("rate:%.2fkB/s,length:%dkB,%dms\n", rate, fileLen >> 10, time2));
                        this.setCommSpeed(rate);
                        this.setLogln("comm finished");
                        return true;
                    }
                    rn = this.dev.readBulk(ReadEndpoint, buf, bufSize, 3000, this.reopenOnTimeout);
                    prg += rn;
                    bb.put(buf, 0, rn);
                }
            }
            finally {
                this.endTransaction();
            }
        }
        catch (USBTimeoutException e) {
            e.printStackTrace();
            this.errKey = "errcomm";
            return false;
        }
        catch (USBException e) {
            e.printStackTrace();
            this.errKey = "errcomm";
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean doGetData() {
        this.transactionCanceled = false;
        long time1 = Calendar.getInstance().getTimeInMillis();
        int fileLength = (int)this.di.getLength();
        this.startCommand[0] = (byte)this.di.getValue();
        this.startCommand[1] = (byte)fileLength;
        this.startCommand[2] = (byte)(fileLength >> 8);
        this.startCommand[3] = (byte)(fileLength >> 16);
        this.startCommand[4] = (byte)(fileLength >> 24);
        ByteBuffer bb = this.di.initbb();
        if (bb == null) {
            return false;
        }
        try {
            try {
                double rate;
                double costT;
                if (this.iDevice == null) {
                    System.out.println("scaning..");
                    List<IDevice> ld = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
                    System.out.println(ld.size());
                    if (ld.size() <= 0) {
                        this.errKey = "nofinddev";
                        return false;
                    }
                    this.iDevice = ld.get(0);
                }
                this.dev = CDevice.getDevice(USBID.getVid(), USBID.getPid());
                CDevice.simulateOpen(this.dev, this.iDevice);
                int WriteEndpoint = this.iDevice.getWriteEndpoint();
                int ReadEndpoint = this.iDevice.getReadEndpoint();
                this.dev.writeBulk(WriteEndpoint, this.startCommand, this.startCommand.length, 3000, this.reopenOnTimeout);
                this.setProgressRange(fileLength);
                int prg = 0;
                while (true) {
                    if (this.transactionCanceled) {
                        return false;
                    }
                    if (!this.di.fillbb(bb)) {
                        this.di.termbb();
                        long time2 = Calendar.getInstance().getTimeInMillis() - time1;
                        costT = (double)time2 / 1000.0;
                        rate = (double)(fileLength >> 10) / costT;
                        this.setLogln(String.format("rate:%.2fkB/s,length:%dkB,%dms\n", rate, fileLength >> 10, time2));
                        this.setCommSpeed(rate);
                        this.setLogln("comm finished");
                        byte[] buffer = new byte[1];
                        this.dev.readBulk(ReadEndpoint, buffer, 1, 200000, this.reopenOnTimeout);
                        this.setLogln("response data: " + buffer[0]);
                        if (buffer[0] != 55) {
                            return false;
                        }
                        break;
                    }
                    if (!bb.hasRemaining()) continue;
                    int bl = bb.limit();
                    this.dev.writeBulk(WriteEndpoint, bb.array(), bl, 3000, this.reopenOnTimeout);
                    this.setProgress(prg += bl);
                }
                this.setLogln("write Data finished");
                long time3 = Calendar.getInstance().getTimeInMillis() - time1;
                costT = (double)time3 / 1000.0;
                rate = (double)(fileLength >> 10) / costT;
                this.setLogln(String.format("rate:%.2fkB/s,length:%dkB,%dms\n", rate, fileLength >> 10, time3));
                this.setDownloadSpeed(rate);
                this.setLogln("download finished");
                return true;
            }
            finally {
                this.endTransaction();
            }
        }
        catch (USBTimeoutException e) {
            e.printStackTrace();
            return false;
        }
        catch (USBException e) {
            e.printStackTrace();
            return false;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void cancelTransaction() {
        this.transactionCanceled = true;
    }

    public boolean isTransactionCanceled() {
        return this.transactionCanceled;
    }

    public void endTransaction() {
        this.setLogln("call endTransaction");
        try {
            if (this.dev != null && this.dev.isOpen()) {
                this.dev.close();
                this.dev = null;
                this.iDevice = null;
            }
        }
        catch (USBException e) {
            e.printStackTrace();
        }
    }

    private void setProgress(int value) {
        if (this.dll == null) {
            return;
        }
        this.dll.handleEvent(new DLEvent((Object)this, 1, value));
    }

    private void setProgressRange(int value) {
        if (this.dll == null) {
            return;
        }
        this.dll.handleEvent(new DLEvent((Object)this, 2, value));
    }

    private void setCommSpeed(double value) {
        if (this.dll == null) {
            return;
        }
        this.dll.handleEvent(new DLEvent((Object)this, 3, (int)value));
    }

    private void setDownloadSpeed(double value) {
        if (this.dll == null) {
            return;
        }
        this.dll.handleEvent(new DLEvent((Object)this, 4, (int)value));
    }

    private void setLog(String txt) {
        if (this.dll == null) {
            return;
        }
        this.dll.handleEvent(new DLEvent((Object)this, 5, txt));
    }

    private void setLogln(String txt) {
        System.out.println(txt);
    }

    public static void logData(byte[] data, String type) {
        System.out.print("Data: ");
        if (type.equalsIgnoreCase("hex")) {
            int i = 0;
            while (i < data.length) {
                System.out.print("0x" + Integer.toHexString(data[i] & 0xFF) + " ");
                ++i;
            }
        } else if (type.equalsIgnoreCase("dec")) {
            int i = 0;
            while (i < data.length) {
                System.out.print(String.valueOf(data[i] & 0xFF) + " ");
                ++i;
            }
        }
        System.out.println();
    }

    public String getErrKey() {
        return this.errKey;
    }
}

