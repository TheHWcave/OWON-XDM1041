/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.ntb.usb.USBException
 */
package com.owon.uppersoft.omap.usb;

import ch.ntb.usb.USBException;
import com.owon.uppersoft.common.comm.CDevice;
import com.owon.uppersoft.common.comm.IDevice;
import com.owon.uppersoft.omap.usb.DownloadItem;
import com.owon.uppersoft.omap.usb.MainFrmUsb;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class DoJob
extends Thread {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private CDevice dev;
    private boolean cancel;
    private static short vid = (short)1105;
    private static short pid = (short)-12279;
    private static int timeout = 2000;
    private static boolean reopenOnTimeout = false;
    private List<DownloadItem> diList;
    private MainFrmUsb mf;
    private int writeEndpoint;
    private int readEndpoint;

    public static void main(String[] args) {
    }

    public DoJob(MainFrmUsb mf) {
        this.mf = mf;
        this.diList = mf.getList();
    }

    @Override
    public void run() {
        this.mf.logLn("start...");
        this.mf.logLn("wait for search USB device...");
        this.findDev();
        this.mf.logLn("find device ,start send file...");
        this.write();
        this.mf.logLn("send file finish ");
        this.close();
    }

    public void findDev() {
        try {
            List<IDevice> ld = null;
            this.cancel = false;
            while (!this.cancel) {
                ld = CDevice.scanMatchedDevices(vid, pid);
                if (ld.size() > 0) break;
                this.slp(10L);
            }
            IDevice iDevice = (IDevice)ld.get(0);
            this.readEndpoint = iDevice.getReadEndpoint();
            this.writeEndpoint = iDevice.getWriteEndpoint();
            this.dev = CDevice.getDevice(vid, pid);
            CDevice.simulateOpen(this.dev, iDevice);
            int size = 512;
            byte[] b = new byte[size];
            int rn = this.dev.readBulk(this.readEndpoint, b, size, timeout, reopenOnTimeout);
            System.out.println(new String(b, 0, rn));
        }
        catch (USBException e) {
            e.printStackTrace();
        }
    }

    private void write() {
        Iterator<DownloadItem> it = this.diList.iterator();
        int i = 0;
        try {
            this.writeFirstItem(it);
            this.mf.setprog(++i);
        }
        catch (USBException e1) {
            e1.printStackTrace();
        }
        this.slp(10L);
        while (it.hasNext()) {
            DownloadItem di = it.next();
            System.err.println(di.getFileName());
            try {
                this.writeItem(di);
                this.mf.setprog(++i);
            }
            catch (USBException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void writeFirstItem(Iterator<DownloadItem> it) throws USBException {
        if (!it.hasNext()) {
            return;
        }
        DownloadItem di = it.next();
        byte[] b = this.long2bytes(di.getAdd());
        this.dev.writeBulk(this.writeEndpoint, b, b.length, timeout, reopenOnTimeout);
        b = this.long2bytes(di.getFileLen());
        this.dev.writeBulk(this.writeEndpoint, b, b.length, timeout, reopenOnTimeout);
        b = di.readFile();
        if (b == null) {
            System.out.println("file read null");
            return;
        }
        int rn = this.dev.writeBulk(this.writeEndpoint, b, b.length, timeout, reopenOnTimeout);
        System.out.print(rn);
    }

    private void writeItem(DownloadItem di) throws USBException {
        byte[] cmd = di.getCmd().getBytes();
        byte[] add = this.long2bytes(di.getAdd());
        byte[] len = this.long2bytes(di.getFileLen());
        byte[] buf = new byte[256];
        boolean finish = false;
        if (di.getFileName() == null) {
            ByteBuffer bb = ByteBuffer.allocate(12);
            bb.put(cmd).put(add).put(len);
            this.dev.writeBulk(this.writeEndpoint, bb.array(), 12, timeout, reopenOnTimeout);
            finish = true;
        }
        while (!finish) {
            int rn = this.dev.readBulk(this.readEndpoint, buf, buf.length, timeout, reopenOnTimeout);
            System.out.println(new String(buf, 0, rn));
            switch (buf[3]) {
                case 102: {
                    ByteBuffer bb = ByteBuffer.allocate(12);
                    bb.put(cmd).put(len).put(add);
                    this.dev.writeBulk(this.writeEndpoint, bb.array(), 12, timeout, reopenOnTimeout);
                    break;
                }
                case 110: {
                    byte[] temp = di.readFile();
                    if (temp == null) {
                        System.out.println("file read null");
                        return;
                    }
                    this.dev.writeBulk(this.writeEndpoint, temp, temp.length, timeout, reopenOnTimeout);
                    break;
                }
                case 111: {
                    finish = true;
                    break;
                }
            }
        }
    }

    private byte[] long2bytes(long l) {
        byte[] b = new byte[4];
        b[3] = (byte)(l >> 24 & 0xFFL);
        b[2] = (byte)(l >> 16 & 0xFFL);
        b[1] = (byte)(l >> 8 & 0xFFL);
        b[0] = (byte)(l & 0xFFL);
        return b;
    }

    private void slp(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            this.mf.enableBtn(true);
            this.dev.close();
        }
        catch (USBException e) {
            e.printStackTrace();
        }
    }
}

