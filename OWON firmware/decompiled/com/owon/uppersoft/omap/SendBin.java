/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  gnu.io.SerialPort
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.Listener
 */
package com.owon.uppersoft.omap;

import com.owon.uppersoft.common.comm.IPref;
import com.owon.uppersoft.omap.DataPacket;
import com.owon.uppersoft.omap.MainFrm;
import com.owon.uppersoft.omap.MsgPacket;
import com.owon.uppersoft.omap.ScriptEntry;
import com.owon.uppersoft.omap.YModem;
import com.owon.uppersoft.patch.direct.serial.SerialPortUtil;
import gnu.io.SerialPort;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class SendBin
implements Listener,
IPref {
    private String port;
    private OutputStream os;
    private InputStream is;
    private FileInputStream fis1;
    private FileInputStream fis2;
    private MainFrm mf;
    private int totalCounter;
    private int counter;
    private SerialPortUtil spu;
    private ByteBuffer bb = ByteBuffer.allocate(58);
    private ByteBuffer bbMsg = ByteBuffer.allocate(12);
    private int step;
    private YModem ym;
    private int counterC;

    private void print(ByteBuffer bb) {
        byte[] b = bb.array();
        int i = 0;
        while (i < b.length) {
            System.out.print(String.valueOf(Integer.toHexString(b[i])) + " ");
            ++i;
        }
        System.out.println();
    }

    public SendBin(String port, MainFrm mf) {
        this.port = port;
        this.mf = mf;
        this.step = 0;
    }

    private void logLn(String s) {
        this.mf.logLn(String.valueOf(s) + "\n");
    }

    private void logAppend(String s) {
        this.mf.logLn(s);
    }

    public void sendFiles() {
        if (this.spu == null) {
            this.spu = new SerialPortUtil(this);
            SerialPort sp = this.spu.openPort(this.port);
            if (sp == null) {
                this.finish();
                this.logLn(String.valueOf(this.port) + " err,pls Check");
                return;
            }
            this.spu.loadPort(115200, 8, 1, 2);
            this.os = this.spu.getOutputStream();
            this.is = this.spu.getInputStream();
        }
        try {
            this.fis1 = new FileInputStream("e");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            this.finish();
            this.logLn("open file evm.bin err,pls Check");
            return;
        }
        try {
            this.fis2 = new FileInputStream("b");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            this.finish();
            this.logLn("open file boot.bin err,pls Check");
            return;
        }
        this.bb.order(ByteOrder.LITTLE_ENDIAN);
    }

    public void handleEvent(Event arg0) {
        try {
            byte[] b = new byte[1024];
            int readLen = this.is.read(b);
            if (this.step >= 3) {
                this.onData(b, readLen);
                return;
            }
            int i = 0;
            while (i < readLen) {
                switch (this.step) {
                    case 0: {
                        if (b[i] != 4) break;
                        this.bb.put(b[i]);
                        ++this.step;
                        break;
                    }
                    case 1: {
                        this.bb.put(b[i]);
                        if (this.bb.remaining() != 0) break;
                        this.print(this.bb);
                        ++this.step;
                        this.bb.clear();
                        this.logLn("send first file...");
                        this.writeMem();
                        this.logLn("send first file finish");
                        this.logLn("send second file...");
                        break;
                    }
                    case 2: {
                        this.bbMsg.put(b[i]);
                        if (this.bbMsg.remaining() != 0) break;
                        this.bbMsg.flip();
                        this.writeNand(new MsgPacket(this.bbMsg));
                        this.bbMsg.clear();
                        break;
                    }
                    default: {
                        System.out.print(b[i]);
                    }
                }
                ++i;
            }
        }
        catch (Exception exception) {}
    }

    private void writeMem() {
        try {
            byte[] byArray = new byte[4];
            byArray[0] = 2;
            byArray[2] = 3;
            byArray[3] = -16;
            byte[] wrt = byArray;
            this.os.write(wrt);
            this.os.flush();
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            int len = this.fis1.available();
            wrt[0] = (byte)(len & 0xFF);
            wrt[1] = (byte)(len >> 8 & 0xFF);
            wrt[2] = (byte)(len >> 16 & 0xFF);
            wrt[3] = (byte)(len >> 24 & 0xFF);
            this.os.write(wrt);
            this.os.flush();
            try {
                Thread.sleep(1L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            wrt = new byte[2048];
            while (this.fis1.available() > 0) {
                int rn = this.fis1.read(wrt);
                this.os.write(wrt, 0, rn);
                this.os.flush();
                try {
                    Thread.sleep(1L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.fis1.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeNand(MsgPacket pck) {
        if (pck.getPkt_type() != 1) {
            System.err.print("no msg");
            return;
        }
        try {
            switch (pck.getMsg_type()) {
                case 1: {
                    MsgPacket msg = new MsgPacket();
                    msg.setMsg_type((short)1);
                    this.totalCounter = this.fis2.available() / 50 + 1;
                    this.counter = 0;
                    int[] nArray = new int[2];
                    nArray[0] = this.totalCounter;
                    msg.setMsg_params(nArray);
                    this.os.write(msg.getPacket());
                    this.os.flush();
                }
                case 3: {
                    this.os.write(new DataPacket(this.fis2).getPacket());
                    this.os.flush();
                    int percent = this.counter++ * 100 / this.totalCounter;
                    this.logLn("send file :" + percent + "%");
                    break;
                }
                case 5: {
                    ++this.step;
                    this.finish();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void finish() {
        this.logLn("send second file finish");
        this.logLn("END");
        try {
            this.fis1.close();
            this.fis2.close();
            this.spu.setSerialPortParams(115200, 8, 1, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onData(byte[] b, int len) {
        switch (this.step) {
            case 3: 
            case 5: 
            case 7: {
                String s = new String(b, 0, len);
                this.logAppend(s);
                if (!this.checkStart(s)) break;
                ScriptEntry se = this.mf.getScriptEntry(this.step);
                this.counterC = 0;
                ++this.step;
                this.ym = new YModem(this.is, this.os, se);
                this.ym.startTransaction();
                break;
            }
            case 4: 
            case 6: {
                int i = 0;
                while (i < len) {
                    this.ym.onData(b[i]);
                    if (this.ym.isFinish) {
                        ++this.step;
                    }
                    ++i;
                }
                break;
            }
        }
    }

    private boolean checkStart(String s) {
        if (s.equals("C")) {
            ++this.counterC;
            if (this.counterC > 1) {
                return true;
            }
        }
        return false;
    }

    public void close() {
        this.spu.closeCOM();
    }
}

