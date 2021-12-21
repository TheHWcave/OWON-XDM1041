/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.Listener
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.serial.SerialPortUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class SerialSender
implements Runnable,
Listener {
    private boolean s;
    private BufferedReader breader;
    private Text txtLog;
    private Thread t;
    private SerialPortUtil spu;
    private String line;
    NoThread nt = new NoThread();

    public SerialSender(Text txtLog) {
        this.txtLog = txtLog;
    }

    public boolean openPort(String port) {
        this.spu = new SerialPortUtil(this);
        if (this.spu.openPort(port) == null) {
            this.txtLog.setText("RS232 ERROR!");
            return false;
        }
        this.t = new Thread(this);
        this.t.start();
        return true;
    }

    @Override
    public void run() {
        this.spu.loadPort(115200, 8, 1, 0);
        OutputStream os = this.spu.getOutputStream();
        try {
            this.breader = new BufferedReader(new InputStreamReader(this.spu.getInputStream(), "GB2312"));
            this.s = true;
            os.write(new byte[]{117});
            while (this.s) {
                Thread.sleep(500L);
                if (this.s) {
                    os.write(new byte[]{117});
                    Display.getDefault().asyncExec(new Runnable(){

                        @Override
                        public void run() {
                            SerialSender.this.txtLog.append("<< u\n");
                        }
                    });
                    continue;
                }
                break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        this.s = false;
        if (this.spu != null) {
            this.spu.closeCOM();
        }
        this.spu = null;
        if (this.t != null) {
            try {
                this.t.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleEvent(Event event) {
        this.line = "";
        try {
            try {
                this.line = this.breader.readLine();
            }
            catch (IOException iOException) {
                Display.getDefault().asyncExec(new Runnable(){

                    @Override
                    public void run() {
                        SerialSender.this.txtLog.append(">> " + SerialSender.this.line + "\n");
                    }
                });
                return;
            }
        }
        catch (Throwable throwable) {
            Display.getDefault().asyncExec(new /* invalid duplicate definition of identical inner class */);
            throw throwable;
        }
        Display.getDefault().asyncExec(new /* invalid duplicate definition of identical inner class */);
        if (!this.nt.b) {
            this.nt.start();
        }
    }

    class NoThread
    extends Thread {
        public boolean b = false;

        NoThread() {
        }

        @Override
        public void run() {
            this.b = true;
            try {
                Thread.sleep(3000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            SerialSender.this.s = false;
            Display.getDefault().asyncExec(new Runnable(){

                @Override
                public void run() {
                    SerialSender.this.txtLog.append(MsgCenter.getString("MainFrm.usbmode"));
                    SerialSender.this.txtLog.append("\n");
                }
            });
        }
    }
}

