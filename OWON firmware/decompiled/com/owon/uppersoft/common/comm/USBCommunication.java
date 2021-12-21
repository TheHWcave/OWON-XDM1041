/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.ntb.usb.LibusbJava
 *  ch.ntb.usb.USBException
 *  ch.ntb.usb.USBTimeoutException
 *  ch.ntb.usb.Usb_Device
 *  ch.ntb.usb.Usb_Device_Descriptor
 */
package com.owon.uppersoft.common.comm;

import ch.ntb.usb.LibusbJava;
import ch.ntb.usb.USBException;
import ch.ntb.usb.USBTimeoutException;
import ch.ntb.usb.Usb_Device;
import ch.ntb.usb.Usb_Device_Descriptor;
import com.owon.uppersoft.common.comm.CDevice;
import com.owon.uppersoft.common.comm.DLEvent;
import com.owon.uppersoft.common.comm.DLListener;
import com.owon.uppersoft.common.comm.IDevice;
import com.owon.uppersoft.common.comm.USBID;
import com.owon.uppersoft.common.comm.job.model.DataItem;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private int usbSelected;
    private DataItem di;
    private IDevice[] ids;

    public USBCommunication(DLListener dll) {
        this.dll = dll;
        this.usbSelected = 0;
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

    public boolean getTxt(String path, String fileFormat) {
        return this.doGetTxt(path, fileFormat);
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean getFile(String path, byte[] cmd) {
        this.transactionCanceled = false;
        long time1 = Calendar.getInstance().getTimeInMillis();
        int bufSize = 1024;
        byte[] buf = new byte[bufSize];
        try {
            try {
                File f;
                String fileName;
                if (this.iDevice == null) {
                    this.setLogln("scaning..");
                    List<IDevice> ld = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
                    if (ld.size() <= 0) {
                        return false;
                    }
                    this.iDevice = ld.get(0);
                }
                int WriteEndpoint = this.iDevice.getWriteEndpoint();
                int ReadEndpoint = this.iDevice.getReadEndpoint();
                this.dev = CDevice.getDevice(USBID.getVid(), USBID.getPid());
                CDevice.simulateOpen(this.dev, this.iDevice);
                int w = this.dev.writeBulk(WriteEndpoint, cmd, cmd.length, 3000, this.reopenOnTimeout);
                this.setLogln("write:" + w);
                int rn = this.dev.readBulk(ReadEndpoint, buf, 12, 3000, this.reopenOnTimeout);
                if (rn < 0) {
                    this.setLogln("read head err");
                    return false;
                }
                int fileLen = buf[0] & 0xFF;
                fileLen += (buf[1] & 0xFF) << 8;
                fileLen += (buf[2] & 0xFF) << 16;
                this.setLogln("file len=" + (fileLen += (buf[3] & 0xFF) << 24));
                int prg = rn = this.dev.readBulk(ReadEndpoint, buf, bufSize, 3000, this.reopenOnTimeout);
                rn = this.dev.readBulk(ReadEndpoint, buf, bufSize, 3000, this.reopenOnTimeout);
                prg += rn;
                if (!path.equals("") && !path.endsWith("\\")) {
                    path = String.valueOf(path) + "\\";
                }
                if (!(fileName = new String(buf, 0, 19).trim()).endsWith(".bin")) {
                    fileName = String.valueOf(fileName) + ".txt";
                    f = new File(String.valueOf(path) + fileName);
                } else {
                    f = new File(String.valueOf(path) + fileName);
                    if (f.exists()) {
                        fileName = fileName.replaceAll(".+\\.", String.valueOf(System.currentTimeMillis()) + "\\.");
                        f = new File(String.valueOf(path) + fileName);
                        this.setLogln("File exists>>>>>\n File rename " + fileName);
                    }
                }
                FileOutputStream outf = new FileOutputStream(f);
                BufferedOutputStream bufferout = new BufferedOutputStream(outf);
                bufferout.write(buf, 20, rn - 20);
                while (true) {
                    if (prg >= fileLen) {
                        bufferout.flush();
                        bufferout.close();
                        long time2 = Calendar.getInstance().getTimeInMillis() - time1;
                        double costT = (double)time2 / 1000.0;
                        double rate = (double)(fileLen >> 10) / costT;
                        this.setLogln(String.format("rate:%.2fkB/s,length:%dkB,%dms\n", rate, fileLen >> 10, time2));
                        this.setCommSpeed(rate);
                        this.setLogln("comm finished");
                        this.setLogln("file saved:" + f.getAbsolutePath());
                        return true;
                    }
                    int bl = fileLen - prg;
                    bl = bl < bufSize ? bl : bufSize;
                    rn = this.dev.readBulk(ReadEndpoint, buf, bl, 3000, this.reopenOnTimeout);
                    this.setProgress(prg += rn);
                    bufferout.write(buf, 0, rn);
                }
            }
            finally {
                this.endTransaction();
            }
        }
        catch (USBTimeoutException e) {
            this.setLogln(e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch (USBException e) {
            this.setLogln(e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch (FileNotFoundException e) {
            this.setLogln(e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            this.setLogln(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean doGetTxt(String path, String fileFormat) {
        this.transactionCanceled = false;
        long time1 = Calendar.getInstance().getTimeInMillis();
        byte[] startCommand = "STARTDEBUGTXT".getBytes();
        int bufSize = 1024;
        byte[] buf = new byte[bufSize];
        try {
            try {
                File f;
                String fileName;
                if (this.iDevice == null) {
                    this.setLogln("scaning..");
                    List<IDevice> ld = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
                    if (ld.size() <= 0) {
                        return false;
                    }
                    this.iDevice = ld.get(0);
                }
                int WriteEndpoint = this.iDevice.getWriteEndpoint();
                int ReadEndpoint = this.iDevice.getReadEndpoint();
                this.dev = CDevice.getDevice(USBID.getVid(), USBID.getPid());
                CDevice.simulateOpen(this.dev, this.iDevice);
                int w = this.dev.writeBulk(WriteEndpoint, startCommand, startCommand.length, 3000, this.reopenOnTimeout);
                this.setLogln("write:" + w);
                int rn = this.dev.readBulk(ReadEndpoint, buf, 12, 3000, this.reopenOnTimeout);
                if (rn < 0) {
                    this.setLogln("read head err");
                    return false;
                }
                int fileLen = buf[0] & 0xFF;
                fileLen += (buf[1] & 0xFF) << 8;
                fileLen += (buf[2] & 0xFF) << 16;
                this.setLogln("file len=" + (fileLen += (buf[3] & 0xFF) << 24));
                rn = this.dev.readBulk(ReadEndpoint, buf, bufSize, 3000, this.reopenOnTimeout);
                if (!path.equals("") && !path.endsWith("\\")) {
                    path = String.valueOf(path) + "\\";
                }
                if (!(fileName = new String(buf, 0, 19).trim().replaceAll("[ \\\\/:*?\"<>|]", "-")).endsWith(".bin")) {
                    fileName = String.valueOf(fileName) + fileFormat;
                    f = new File(String.valueOf(path) + fileName);
                } else {
                    f = new File(String.valueOf(path) + fileName);
                    if (f.exists()) {
                        fileName = fileName.replaceAll(".+\\.", String.valueOf(System.currentTimeMillis()) + "\\.");
                        f = new File(String.valueOf(path) + fileName);
                        this.setLogln("File exists>>>>>\n File rename " + fileName);
                    }
                }
                FileOutputStream outf = new FileOutputStream(f);
                BufferedOutputStream bufferout = new BufferedOutputStream(outf);
                bufferout.write(buf, 20, rn - 20);
                int prg = rn;
                while (true) {
                    if (prg >= fileLen) {
                        bufferout.flush();
                        bufferout.close();
                        long time2 = Calendar.getInstance().getTimeInMillis() - time1;
                        double costT = (double)time2 / 1000.0;
                        double rate = (double)(fileLen >> 10) / costT;
                        this.setLogln(String.format("rate:%.2fkB/s,length:%dkB,%dms\n", rate, fileLen >> 10, time2));
                        this.setCommSpeed(rate);
                        this.setLogln("comm finished");
                        this.setLogln("file saved:" + f.getAbsolutePath());
                        return true;
                    }
                    int bl = fileLen - prg;
                    bl = bl < bufSize ? bl : bufSize;
                    rn = this.dev.readBulk(ReadEndpoint, buf, bl, 3000, this.reopenOnTimeout);
                    this.setProgress(prg += rn);
                    bufferout.write(buf, 0, rn);
                }
            }
            finally {
                this.endTransaction();
            }
        }
        catch (USBTimeoutException e) {
            this.setLogln(e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch (USBException e) {
            this.setLogln(e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch (FileNotFoundException e) {
            this.setLogln(e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            this.setLogln(e.getMessage());
            e.printStackTrace();
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
                    this.setLogln("scaning..");
                    List<IDevice> ld = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
                    if (ld.size() <= 0) {
                        return false;
                    }
                    this.iDevice = ld.get(0);
                }
                System.err.println(String.valueOf(this.usbSelected) + ":" + this.iDevice.getSerialNumber());
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
            this.setLogln(e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch (USBException e) {
            this.setLogln(e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch (FileNotFoundException e) {
            this.setLogln(e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            this.setLogln(e.getMessage());
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

    private void endTransaction() {
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

    private void setCMDdetail(Object data) {
        if (this.dll == null) {
            return;
        }
        this.dll.handleEvent(new DLEvent((Object)this, 6, data));
    }

    private void refreshUSB(Object data) {
        if (this.dll == null) {
            return;
        }
        this.dll.handleEvent(new DLEvent((Object)this, 7, data));
    }

    private void setLogln(String txt) {
        this.setLog(String.valueOf(txt) + "\n");
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
        System.out.println("");
    }

    public void refreshUSBPort() {
        try {
            List<IDevice> udl = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
            for (IDevice id : udl) {
                Usb_Device dev = id.getUsb_Device();
                long handle = LibusbJava.usb_open((Usb_Device)dev);
                Usb_Device_Descriptor devDesc = dev.getDescriptor();
                String iSerialNumber = LibusbJava.usb_get_string_simple((long)handle, (int)devDesc.getISerialNumber());
                id.setSerialNumber(iSerialNumber);
                LibusbJava.usb_close((long)handle);
            }
            this.ids = new IDevice[udl.size()];
            this.ids = udl.toArray(this.ids);
            this.refreshUSB(this.ids);
        }
        catch (USBException e) {
            this.setLogln(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    }

    public void setUsbSelected(int usbSelected) {
        this.usbSelected = usbSelected;
    }
}

