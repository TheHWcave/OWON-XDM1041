/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  gnu.io.CommPortIdentifier
 *  gnu.io.NoSuchPortException
 *  gnu.io.PortInUseException
 *  gnu.io.SerialPort
 *  gnu.io.UnsupportedCommOperationException
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.comm.frame.OdpDownloadComposite;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class OdpDownloadByComm
extends Thread {
    private String osPath;
    private String txtPath;
    private Text txtLog;
    private List<DownloadItem> listDownloadItem;
    private CommPortIdentifier portId;
    private SerialPort SrPt;
    private InputStream is;
    private OutputStream os;
    private String comPort;
    private boolean isStop;
    private boolean isWriteNewVer;
    private OdpDownloadComposite odc;

    public void setWriteNewVer(boolean isWriteNewVer) {
        this.isWriteNewVer = isWriteNewVer;
    }

    public OdpDownloadByComm(OdpDownloadComposite odc, Text txtLog, String comPort) {
        this.odc = odc;
        this.txtLog = txtLog;
        this.comPort = comPort;
        this.listDownloadItem = new ArrayList<DownloadItem>();
    }

    public void addItems(String type, int value, boolean isSelection, String path) {
        DownloadItem di = new DownloadItem(type, value, isSelection, path);
        if (di.getPath().length() > 0) {
            this.listDownloadItem.add(di);
        }
    }

    public boolean openPort(String port) {
        try {
            this.portId = CommPortIdentifier.getPortIdentifier((String)port);
            this.SrPt = (SerialPort)this.portId.open("OWON_SerialPort", 1000);
            return true;
        }
        catch (NoSuchPortException e) {
            e.printStackTrace();
        }
        catch (PortInUseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void init() {
        try {
            this.is = this.SrPt.getInputStream();
            this.os = this.SrPt.getOutputStream();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.SrPt.setSerialPortParams(115200, 8, 1, 0);
        }
        catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (this.listDownloadItem.isEmpty()) {
            this.setLog(MsgCenter.getString("ODP.nothingselection"));
            this.odc.enable();
            return;
        }
        boolean b = this.openPort(this.comPort);
        this.init();
        if (!b) {
            this.setLog(MsgCenter.getString("ODP.openportfail"));
            this.odc.enable();
            this.release();
            return;
        }
        this.isStop = false;
        if (!this.enter()) {
            this.odc.enable();
            this.release();
            return;
        }
        for (DownloadItem di : this.listDownloadItem) {
            this.setLog(String.valueOf(MsgCenter.getString("ODP.start")) + di.getType());
            byte[] res = new byte[]{(byte)di.getValue()};
            try {
                this.os.write(res, 0, res.length);
                if (!this.waitToRespone()) {
                    this.setLog(MsgCenter.getString("ODP.headerror"));
                    this.isStop = true;
                    break;
                }
                this.setLog(MsgCenter.getString("ODP.head"));
                File f = new File(di.getPath());
                int pack2 = (int)(f.length() / 10240L);
                if (f.length() % 10240L > 0L) {
                    ++pack2;
                }
                res[0] = (byte)pack2;
                this.os.write(res, 0, res.length);
                if (!this.waitToRespone()) {
                    this.setLog(String.valueOf(MsgCenter.getString("ODP.packageerror")) + pack2);
                    this.isStop = true;
                    break;
                }
                this.setLog(String.valueOf(MsgCenter.getString("ODP.package")) + pack2);
                FileInputStream fis = new FileInputStream(f);
                byte[] packagenum = new byte[10240];
                int i = 0;
                while (i < pack2) {
                    int num = fis.read(packagenum);
                    this.os.write(packagenum, 0, num);
                    if (!this.waitToRespone()) {
                        this.setLog(String.valueOf(MsgCenter.getString("ODP.packagenumerror")) + i);
                        this.isStop = true;
                        break;
                    }
                    this.setLog(String.valueOf(MsgCenter.getString("ODP.packno")) + i);
                    if (this.isStop) break;
                    ++i;
                }
                fis.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                this.setLog(MsgCenter.getString("ODP.error"));
                this.isStop = true;
            }
        }
        if (!this.isStop) {
            this.setLog(MsgCenter.getString("ODP.finish"));
            byte[] res = new byte[]{20};
            try {
                this.os.write(res, 0, 1);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.isWriteNewVer) {
            this.updateVersion();
        }
        this.odc.enable();
        this.release();
    }

    private void updateVersion() {
        try {
            Iterator<DownloadItem> it = this.listDownloadItem.iterator();
            String ver = null;
            while (it.hasNext()) {
                DownloadItem di = it.next();
                if (di.value != 5) continue;
                String path = di.path;
                int start = path.lastIndexOf("_") + 1;
                int end = path.lastIndexOf(".");
                ver = path.substring(start, end);
                System.out.println(ver);
            }
            if (ver == null) {
                this.setLog(MsgCenter.getString("ODP.filenameerror"));
                return;
            }
            this.setLog(MsgCenter.getString("ODP.waittip"));
            Thread.sleep(5000L);
            byte[] res = ("INT:VERSION " + ver + "\r\n").getBytes();
            this.os.write(res, 0, res.length);
            if (!this.waitToRespone()) {
                this.setLog(MsgCenter.getString("ODP.fail"));
            } else {
                this.setLog(MsgCenter.getString("ODP.succ"));
            }
        }
        catch (Exception exception) {
            this.setLog(MsgCenter.getString("ODP.fail"));
        }
    }

    public String getOsPath() {
        return this.osPath;
    }

    public void setOsPath(String osPath) {
        this.osPath = osPath;
    }

    public String getTxtPath() {
        return this.txtPath;
    }

    public void setTxtPath(String txtPath) {
        this.txtPath = txtPath;
    }

    private void setLog(final String s) {
        Display.getDefault().asyncExec(new Runnable(){

            @Override
            public void run() {
                OdpDownloadByComm.this.txtLog.append(String.valueOf(s) + "\n");
            }
        });
    }

    public void release() {
        try {
            try {
                if (this.os != null) {
                    this.os.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                if (this.SrPt != null && this.portId != null && this.portId.isCurrentlyOwned()) {
                    this.SrPt.close();
                }
            }
        }
        finally {
            if (this.SrPt != null && this.portId != null && this.portId.isCurrentlyOwned()) {
                this.SrPt.close();
            }
        }
    }

    private boolean waitToRespone() {
        int delayRead = 1000;
        byte[] res = new byte[1];
        int retryTimes = 5;
        boolean succ = false;
        try {
            while (!succ) {
                Thread.sleep(delayRead);
                this.is.read(res);
                if (res[0] != 55) {
                    this.setLog(String.valueOf(MsgCenter.getString("ODP.respones")) + res[0]);
                    this.setLog(String.valueOf(MsgCenter.getString("ODP.retry")) + retryTimes);
                    if (retryTimes-- < 0) {
                        break;
                    }
                } else {
                    succ = true;
                }
                if (!this.isStop) {
                    continue;
                }
                break;
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return succ;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean enter() {
        try {
            byte[] send = "start".getBytes();
            byte[] res = new byte[1];
            do {
                this.setLog(MsgCenter.getString("ODP.enterdownload"));
                this.os.write(send);
                this.os.flush();
                this.is.read(res);
                if (res[0] == 55) {
                    this.setLog(MsgCenter.getString("ODP.enterdownloadsucc"));
                    try {
                        Thread.sleep(1000L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    res = new byte[64];
                    this.is.read(res);
                    return true;
                }
                try {
                    Thread.sleep(400L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!this.isStop);
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setStop() {
        this.isStop = true;
    }

    public void getTxt(String path) {
        new GetTxtRunner(path).start();
    }

    class DownloadItem {
        boolean isSelection;
        String path;
        String type;
        int value;

        DownloadItem(String type, int value, boolean isSelection, String path) {
            this.type = type;
            this.value = value;
            this.isSelection = isSelection;
            this.path = path;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public boolean isSelection() {
            return this.isSelection;
        }

        public void setSelection(boolean isSelection) {
            this.isSelection = isSelection;
        }

        public String getPath() {
            return this.path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    class GetTxtRunner
    extends Thread {
        FileOutputStream fos;
        int fileLen;
        int prg;
        String path;

        public GetTxtRunner(String path) {
            this.path = path;
        }

        @Override
        public void run() {
            if (this.getTxt()) {
                this.responseOnDataAvaible();
            }
        }

        public boolean getTxt() {
            if (this.path.length() == 0) {
                OdpDownloadByComm.this.setLog("select directroy");
                OdpDownloadByComm.this.odc.enable();
                return false;
            }
            boolean b = OdpDownloadByComm.this.openPort(OdpDownloadByComm.this.comPort);
            OdpDownloadByComm.this.init();
            if (!b) {
                OdpDownloadByComm.this.setLog("open port fail!!!");
                OdpDownloadByComm.this.odc.enable();
                OdpDownloadByComm.this.release();
                return false;
            }
            OdpDownloadByComm.this.isStop = false;
            byte[] res = "INTernal:TXT\r\n".getBytes();
            try {
                OdpDownloadByComm.this.os.write(res, 0, res.length);
                return true;
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        private void responseOnDataAvaible() {
            this.fileLen = 0;
            this.prg = 0;
            int bufferSize = 1024;
            byte[] b = new byte[bufferSize];
            int size = 0;
            int trying = 0;
            do {
                try {
                    Thread.sleep(200L);
                    if (this.fileLen == 0) {
                        if (OdpDownloadByComm.this.is.available() >= 24) {
                            size = OdpDownloadByComm.this.is.read(b);
                            this.fileLen = b[0] & 0xFF;
                            this.fileLen += (b[1] & 0xFF) << 8;
                            this.fileLen += (b[2] & 0xFF) << 16;
                            this.fileLen += (b[3] & 0xFF) << 24;
                            OdpDownloadByComm.this.setLog("File lengh:" + this.fileLen);
                            String name = new String(b, 4, 20).trim();
                            OdpDownloadByComm.this.setLog("File Name:" + name);
                            this.fos = new FileOutputStream(String.valueOf(this.path) + "\\" + name + ".bin");
                            this.prg = size - 24;
                            this.fos.write(b, 24, size - 24);
                        }
                        if (trying++ <= 20) continue;
                        break;
                    }
                    size = OdpDownloadByComm.this.is.read(b);
                    this.prg += size;
                    if (this.prg >= this.fileLen) {
                        int i = this.prg - this.fileLen;
                        this.fos.write(b, 0, size - i);
                        OdpDownloadByComm.this.setLog("transtion finish");
                        break;
                    }
                    this.fos.write(b, 0, size);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!OdpDownloadByComm.this.isStop);
            try {
                this.fos.flush();
                this.fos.close();
            }
            catch (Exception exception) {}
            OdpDownloadByComm.this.odc.enable();
            OdpDownloadByComm.this.release();
        }
    }
}

