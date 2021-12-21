/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  gnu.io.SerialPort
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.Listener
 */
package com.owon.uppersoft.patch.direct;

import com.owon.uppersoft.patch.direct.EnterDownloadMode;
import com.owon.uppersoft.patch.direct.SinglePatchFrame;
import com.owon.uppersoft.patch.direct.serial.SerialPortUtil;
import gnu.io.SerialPort;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class EnterDownloadMode_COM
implements EnterDownloadMode,
Listener {
    public static final int parity = 0;
    public static final int baudRate = 115200;
    public static final int stopBits = 1;
    public static final int dataBits = 8;
    private SinglePatchFrame sp;
    private SerialPortUtil spu;
    private BufferedReader br;
    NotifyT t = new NotifyT();
    private boolean sending;

    public EnterDownloadMode_COM(SinglePatchFrame sp) {
        this.sp = sp;
        this.spu = new SerialPortUtil(this);
    }

    @Override
    public void enter() {
        this.rewind();
        System.out.print(this.sp.getCom());
        SerialPort SrPt = this.spu.openPort(this.sp.getCom());
        if (SrPt == null) {
            return;
        }
        this.spu.loadPort(115200, 8, 1, 0);
        OutputStream os = this.spu.getOutputStream();
        InputStream is = this.spu.getInputStream();
        try {
            this.br = new BufferedReader(new InputStreamReader(is, "GB2312"));
            this.sp.setInfo("");
            this.t = new NotifyT();
            while (this.sending) {
                os.write(117);
                os.flush();
                this.sp.appendInfo("*");
                Thread.sleep(500L);
            }
            this.closeCOM();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void handleEvent(Event event) {
        Thread t = new Thread(new Runnable(){

            @Override
            public void run() {
                EnterDownloadMode_COM.this.responseOnDataAvaible();
            }
        });
        t.start();
    }

    protected void responseOnDataAvaible() {
        String line = "";
        try {
            line = this.br.readLine();
        }
        catch (IOException iOException) {
            return;
        }
        if (line.length() != 0) {
            System.out.println(line);
        }
        if (this.sending) {
            this.sp.appendInfo("*");
        }
        if (!this.t.b) {
            this.t.start();
        }
    }

    protected void set() {
        this.sending = false;
    }

    protected void rewind() {
        this.sending = true;
    }

    public void closeCOM() {
        this.set();
        this.spu.closeCOM();
        if (this.br != null) {
            try {
                this.br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void cancel() {
        System.out.print("adasfas");
        this.closeCOM();
    }

    class NotifyT
    extends Thread {
        public boolean b = false;

        NotifyT() {
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
            EnterDownloadMode_COM.this.set();
        }
    }
}

