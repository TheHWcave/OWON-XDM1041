/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.ntb.usb.USBException
 *  ch.ntb.usb.USBTimeoutException
 */
package com.owon.uppersoft.common.comm;

import ch.ntb.usb.USBException;
import ch.ntb.usb.USBTimeoutException;
import com.owon.uppersoft.common.comm.CDevice;
import com.owon.uppersoft.common.comm.DLEvent;
import com.owon.uppersoft.common.comm.DLListener;
import com.owon.uppersoft.common.comm.IDevice;
import com.owon.uppersoft.common.comm.USBID;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.List;

public class CommonCommand {
    private static final int timeout = 3000;
    private boolean reopenOnTimeout = true;
    private CDevice dev;
    private DLListener dll;

    public CommonCommand(DLListener dll) {
        this.dll = dll;
    }

    public void interact(byte[] request, byte[] response) {
        if (request == null || request.length == 0) {
            return;
        }
        int l = 0;
        if (response != null) {
            l = response.length;
        }
        this.interact(request, request.length, response, l);
    }

    public void broadcast(byte[] request, int reqLen, byte[] response, int resLen) {
        try {
            List<IDevice> ld = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
            Iterator<IDevice> it = ld.iterator();
            while (it.hasNext()) {
                try {
                    IDevice iDevice = it.next();
                    this.dev = CDevice.getDevice(USBID.getVid(), USBID.getPid());
                    CDevice.simulateOpen(this.dev, iDevice);
                    this.setStatusFlag(request);
                    this.dev.writeBulk(iDevice.getWriteEndpoint(), request, reqLen, 300, false);
                    String msg = "";
                    if (resLen == 0) continue;
                    this.dev.readBulk(iDevice.getReadEndpoint(), response, resLen, 300, false);
                    this.setStatusFlag(response);
                    byte cmd = response[0];
                    msg = "21 ";
                    if (cmd == 55) {
                        msg = String.valueOf(msg) + ">> " + iDevice.getSerialNumber() + MsgCenter.getString("BR.success");
                    }
                    this.setLog(String.valueOf(msg) + " " + cmd + "\r\n");
                }
                finally {
                    this.endTransaction();
                }
            }
        }
        catch (USBTimeoutException e) {
            e.printStackTrace();
            this.setLog(String.valueOf(MsgCenter.getString("BR.fail")) + "\n");
        }
        catch (USBException e) {
            e.printStackTrace();
            this.setLog(String.valueOf(MsgCenter.getString("BR.fail")) + "\n");
        }
    }

    public void interact(byte[] request, int reqLen, byte[] response, int resLen) {
        try {
            try {
                List<IDevice> ld = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
                if (ld.size() <= 0) {
                    return;
                }
                IDevice iDevice = ld.get(0);
                this.dev = CDevice.getDevice(USBID.getVid(), USBID.getPid());
                CDevice.simulateOpen(this.dev, iDevice);
                if (request.length > 5) {
                    this.setLog("write length:" + reqLen);
                } else {
                    this.setStatusFlag(request);
                }
                this.dev.writeBulk(iDevice.getWriteEndpoint(), request, reqLen, 3000, this.reopenOnTimeout);
                if (resLen != 0) {
                    this.dev.readBulk(iDevice.getReadEndpoint(), response, resLen, 3000, this.reopenOnTimeout);
                    this.setStatusFlag(response);
                }
            }
            finally {
                this.endTransaction();
            }
        }
        catch (USBTimeoutException e) {
            e.printStackTrace();
        }
        catch (USBException e) {
            e.printStackTrace();
        }
    }

    public void write(String cmd, String path) {
        try {
            try {
                List<IDevice> ld = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
                if (ld.size() <= 0) {
                    return;
                }
                IDevice iDevice = ld.get(0);
                this.dev = CDevice.getDevice(USBID.getVid(), USBID.getPid());
                CDevice.simulateOpen(this.dev, iDevice);
                int bufSize = 512;
                FileInputStream fis = new FileInputStream(path);
                int len = cmd.length() + fis.available();
                this.setLog("write length:" + len);
                ByteBuffer bb = ByteBuffer.allocate(cmd.length() + 4);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                bb.putInt(len);
                bb.put(cmd.getBytes());
                byte[] buf = bb.array();
                this.dev.writeBulk(iDevice.getWriteEndpoint(), buf, buf.length, 3000, this.reopenOnTimeout);
                buf = new byte[bufSize];
                while (fis.available() > 0) {
                    int rn = fis.read(buf);
                    this.dev.writeBulk(iDevice.getWriteEndpoint(), buf, rn, 3000, this.reopenOnTimeout);
                    try {
                        Thread.sleep(10L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                fis.close();
            }
            finally {
                this.endTransaction();
            }
        }
        catch (USBTimeoutException e) {
            e.printStackTrace();
        }
        catch (USBException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String comm(byte[] request, int reqLen, int timeout) {
        try {
            byte[] response = new byte[51200];
            try {
                List<IDevice> ld = CDevice.scanMatchedDevices(USBID.getVid(), USBID.getPid());
                if (ld.size() <= 0) {
                    return null;
                }
                IDevice iDevice = ld.get(0);
                this.dev = CDevice.getDevice(USBID.getVid(), USBID.getPid());
                CDevice.simulateOpen(this.dev, iDevice);
                this.dev.writeBulk(iDevice.getWriteEndpoint(), request, reqLen, 30000, this.reopenOnTimeout);
                int rn = this.dev.readBulk(iDevice.getReadEndpoint(), response, response.length, timeout, this.reopenOnTimeout);
                String string = new String(response, 0, rn);
                return string;
            }
            finally {
                this.endTransaction();
            }
        }
        catch (USBTimeoutException e) {
            e.printStackTrace();
            return null;
        }
        catch (USBException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean endTransaction() {
        try {
            if (this.dev != null) {
                this.dev.close();
                this.dev = null;
            }
        }
        catch (USBException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void setStatusFlag(Object data) {
        if (this.dll == null) {
            return;
        }
        this.dll.handleEvent(new DLEvent((Object)this, 6, data));
    }

    private void setLog(Object data) {
        if (this.dll == null) {
            return;
        }
        this.dll.handleEvent(new DLEvent((Object)this, 5, data));
    }
}

