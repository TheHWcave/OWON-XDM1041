/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  gnu.io.SerialPort
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.Listener
 */
package com.owon.uppersoft.patch.direct.serial;

import com.owon.uppersoft.common.comm.DLListener;
import com.owon.uppersoft.common.comm.job.MSOTYPE;
import com.owon.uppersoft.common.comm.job.OSCTYPE;
import com.owon.uppersoft.common.comm.job.model.FileDataFile;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.SendFile;
import com.owon.uppersoft.patch.direct.serial.SerialPortUtil;
import com.owon.uppersoft.patch.direct.serial.SinglePatchFrame;
import gnu.io.SerialPort;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class PatchProcess
implements Listener {
    public static final String itemsfile = "proper";
    public static final String infofile = "model";
    public static final int parity = 0;
    public static final int baudRate = 115200;
    public static final int stopBits = 1;
    public static final int dataBits = 8;
    public List<FileDataFile> dfs;
    private DLListener dl;
    private SinglePatchFrame sp;
    private SerialPortUtil spu;
    private BufferedReader br;
    NotifyT t = new NotifyT();
    private boolean sending;

    private void getDownloadFile(String type, String txtPath) {
        Properties p = new Properties();
        this.dfs = new LinkedList<FileDataFile>();
        try {
            Enum ot;
            String file = itemsfile;
            int bfsz = 16384;
            FileInputStream is = new FileInputStream(file);
            p.load(is);
            Set<Map.Entry<Object, Object>> set = p.entrySet();
            for (Map.Entry<Object, Object> me : set) {
                Enum ot2;
                String vfilename = me.getValue().toString();
                String k = me.getKey().toString();
                if (type.equalsIgnoreCase("osc")) {
                    ot2 = OSCTYPE.valueOf(k);
                    if (ot2 == null) continue;
                    this.dfs.add(new FileDataFile(ot2.name(), ((OSCTYPE)ot2).value(), vfilename, bfsz));
                    continue;
                }
                if (!type.equalsIgnoreCase("mso") || (ot2 = MSOTYPE.valueOf(k)) == null) continue;
                this.dfs.add(new FileDataFile(ot2.name(), ((MSOTYPE)ot2).value(), vfilename, bfsz));
            }
            int v = 0;
            String n = "";
            if (type.equalsIgnoreCase("osc")) {
                ot = OSCTYPE.valueOf("tx");
                v = ((OSCTYPE)ot).value();
                n = ot.name();
            } else if (type.equalsIgnoreCase("mso")) {
                ot = MSOTYPE.valueOf("tx");
                v = ((MSOTYPE)ot).value();
                n = ot.name();
            }
            if (txtPath != null && txtPath.length() != 0) {
                this.dfs.add(new FileDataFile(n, v, txtPath, bfsz));
                System.out.println(this.dfs.size());
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PatchProcess(DLListener dl, SinglePatchFrame sp) {
        this.dl = dl;
        this.sp = sp;
        this.spu = new SerialPortUtil(this);
    }

    public void handleEvent(Event event) {
        Thread t = new Thread(new Runnable(){

            @Override
            public void run() {
                PatchProcess.this.responseOnDataAvaible();
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

    protected void enterDownloadMode_byCOMMCall(String com) {
        this.rewind();
        SerialPort SrPt = this.spu.openPort(com);
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public void startPatch(String com, String mt, String txtPath) {
        this.enterDownloadMode_byCOMMCall(com);
        this.sp.setInfo(MsgCenter.getBundle().getString("DI.usbmode"));
        try {
            Thread.sleep(2000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean f = this.handleUSBDown(mt, txtPath);
        if (!f) {
            this.sp.setInfo(MsgCenter.getBundle().getString("DI.failed"));
        }
        this.sp.setPatchButtonEnable(true);
        this.closeCOM();
    }

    protected boolean handleUSBDown(String mt, String txtPath) {
        this.getDownloadFile(mt, txtPath);
        SendFile sf = new SendFile(this.dl);
        try {
            for (FileDataFile df : this.dfs) {
                sf.sendFile(df);
                Thread.sleep(100L);
            }
            sf.eraseFLASH();
            Thread.sleep(500L);
            sf.reboot();
            this.sp.setInfo(MsgCenter.getBundle().getString("DI.done"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
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
            PatchProcess.this.set();
        }
    }
}

