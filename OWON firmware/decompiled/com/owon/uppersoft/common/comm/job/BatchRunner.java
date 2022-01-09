/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.viewers.CheckboxTableViewer
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.ProgressBar
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.job;

import com.owon.uppersoft.common.comm.CommonCommand;
import com.owon.uppersoft.common.comm.DLEvent;
import com.owon.uppersoft.common.comm.DLListener;
import com.owon.uppersoft.common.comm.IDevice;
import com.owon.uppersoft.common.comm.USBCommunication;
import com.owon.uppersoft.common.comm.frame.InstrumentComposite;
import com.owon.uppersoft.common.comm.frame.SerialComm;
import com.owon.uppersoft.common.comm.job.DownloadRequestThread;
import com.owon.uppersoft.common.comm.job.SimpleRequestThread;
import com.owon.uppersoft.common.comm.job.model.FileDataFile;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.util.LZSSOutputStream;
import com.owon.uppersoft.selfupgrade.MainFrm;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

public class BatchRunner
implements DLListener {
    private Object[] os;
    private USBCommunication usbCom;
    private FileDataFile df;
    private ProgressBar progressBar;
    private Label progressLabel;
    private Text txtLog;
    private boolean cancel;
    private com.owon.uppersoft.common.comm.frame.MainFrm mf;
    private CommonCommand cc;
    private Display display;
    private CheckboxTableViewer ctv;
    private Combo cobUsb;
    private CheckRebootRunner checkRebootRunner = new CheckRebootRunner();
    private CTVRefreshRunner ctvRefreshRunner = new CTVRefreshRunner();
    private StartUSBRunner startUSBRunner;
    private SendSNRunner ssnr;
    private DebugRunner debugRunner;
    private ClcRunner clcRunner;
    private TRunner tRunner;
    private GetFileRunner getFileRunner;
    private InstrumentComposite icom;
    private boolean isCompress = false;
    private String mark = null;
    private SerialComm sc;
    private HandleEventRunner handleEventRunner = new HandleEventRunner();

    public BatchRunner(com.owon.uppersoft.common.comm.frame.MainFrm mf) {
        this.mf = mf;
        this.progressBar = mf.getProgressBar();
        this.progressLabel = mf.getProgressLabel();
        this.txtLog = mf.getTxtLog();
        this.cobUsb = mf.getCobUSB();
        this.usbCom = new USBCommunication(this);
        this.cc = new CommonCommand(this);
        this.ssnr = new SendSNRunner(this.cc);
        this.startUSBRunner = new StartUSBRunner(this.cc);
        this.tRunner = new TRunner(this.cc);
        this.display = mf.getShell().getDisplay();
    }

    public BatchRunner() {
    }

    public com.owon.uppersoft.common.comm.frame.MainFrm getMainFrm() {
        return this.mf;
    }

    public CheckRebootRunner getcheck() {
        return this.checkRebootRunner;
    }

    public void setDataFiles(CheckboxTableViewer ctv, boolean isCompress) {
        this.ctv = ctv;
        this.os = ctv.getCheckedElements();
        this.isCompress = isCompress;
        this.mark = null;
    }

    public void setDataFile(FileDataFile dataFile, String mark) {
        this.os = new Object[]{dataFile};
        this.mark = mark;
    }

    public void thread() {
        new Thread(new Runnable(){

            @Override
            public void run() {
                BatchRunner.this.batch();
            }
        }).start();
    }

    public boolean isCancel() {
        return this.cancel;
    }

    protected void batch() {
        this.cancel = false;
        Object[] objectArray = this.os;
        int n = this.os.length;
        int n2 = 0;
        while (n2 < n) {
            Object o = objectArray[n2];
            if (this.cancel) {
                return;
            }
            this.df = (FileDataFile)o;
            String path = this.df.getPath();
            File file = new File(path);
            if (!file.exists() || file.isDirectory()) {
                this.df.setStatus(2);
                this.refreshCTV();
            } else {
                if (this.isCompress) {
                    try {
                        int i;
                        FileInputStream in = new FileInputStream(file);
                        File f1 = new File("pack" + file.getName());
                        OutputStream out = new FileOutputStream(f1);
                        System.out.println(f1.getAbsolutePath());
                        out = new LZSSOutputStream(out);
                        byte[] b = new byte[512000];
                        while ((i = ((InputStream)in).read(b)) != -1) {
                            out.write(b, 0, i);
                        }
                        ((InputStream)in).close();
                        out.close();
                        this.df.setPath(f1.getAbsolutePath());
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                this.usbCom.setDataItem(this.df);
                this.usbCom.addEncryptionMark(this.mark);
                boolean b = this.usbCom.getData();
                if (b) {
                    this.df.setStatus(1);
                } else {
                    this.df.setStatus(2);
                }
                this.refreshCTV();
            }
            ++n2;
        }
        this.mark = null;
        this.os = null;
        this.display.asyncExec((Runnable)this.checkRebootRunner);
    }

    private void refreshCTV() {
        this.display.asyncExec((Runnable)this.ctvRefreshRunner);
    }

    public void reboot() {
        this.txtLog.append(MsgCenter.getString("BR.reboot"));
        new SimpleRequestThread(this.cc, 20).start();
    }

    public void txtreboot() {
        new SimpleRequestThread(this.cc, 20).run();
    }

    public void oneKeyDownload(Runnable r) {
        this.txtLog.append(MsgCenter.getString("BR.reboot"));
        DownloadRequestThread s = new DownloadRequestThread(r, this.cc, 20);
        s.start();
    }

    public void runOS() {
        this.txtLog.append(MsgCenter.getString("BR.runOS"));
        new SimpleRequestThread(this.cc, 22).start();
    }

    public void eraseBIOS() {
        this.txtLog.append(MsgCenter.getString("BR.eraserBIOS"));
        new ResopneThread(this.cc, 23).start();
    }

    public void eraseBOOT() {
        this.txtLog.append(MsgCenter.getString("BR.eraserBOOT"));
        new ResopneThread(this.cc, 24).start();
    }

    public void eraseFLASH() {
        this.txtLog.append(MsgCenter.getString("BR.eraserFLASH"));
        new ResopneThread(this.cc, 25).start();
    }

    public void eraseTABLE() {
        this.txtLog.append(MsgCenter.getString("BR.eraserTABLE"));
        new ResopneThread(this.cc, 27).start();
    }

    public void eraseBAK() {
        this.txtLog.append(MsgCenter.getString("BR.eraserBAK"));
        new ResopneThread(this.cc, 28).start();
    }

    public void debug(String path, String fileFormat) {
        this.txtLog.append(String.valueOf(MsgCenter.getString("MainFrm.debug")) + "\n");
        this.debugRunner = new DebugRunner(path, fileFormat);
        new Thread(this.debugRunner).start();
    }

    public void barcode(String path, String fileFormat) {
        this.debugRunner = new DebugRunner(path, fileFormat);
        new Thread(this.debugRunner).run();
    }

    public void setUsbSelected(int index) {
        this.usbCom.setUsbSelected(index);
    }

    public void refreshUsb() {
        this.usbCom.refreshUSBPort();
    }

    public void clc(SerialComm sc, boolean isAddHead, String path) {
        this.sc = sc;
        this.clcRunner = new ClcRunner(this.cc, isAddHead, path);
        new Thread(this.clcRunner).start();
    }

    public void t(SerialComm sc, String path) {
        this.sc = sc;
        this.tRunner.setPath(path);
        new Thread(this.tRunner).start();
    }

    public void enterUSBMode() {
        this.startUSBRunner.run();
    }

    protected void cancel() {
        if (!this.cancel) {
            this.cancel = true;
            this.usbCom.cancelTransaction();
        }
    }

    @Override
    public void handleEvent(DLEvent e) {
        this.handleEventRunner.setDLEvent(e);
        this.display.syncExec((Runnable)this.handleEventRunner);
    }

    public void getText(String path, String fileFormat) {
        this.usbCom.getTxt(path, fileFormat);
    }

    private String writeRead(String cmd, boolean isAddHead, int timeouts) {
        ByteBuffer bb;
        if (isAddHead) {
            cmd = "SIGNAL" + cmd;
            byte[] src = cmd.getBytes();
            int len = src.length + 4;
            bb = ByteBuffer.allocate(len);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putInt(src.length);
            bb.put(src);
        } else {
            bb = ByteBuffer.wrap(cmd.getBytes());
        }
        byte[] b = bb.array();
        return this.cc.comm(b, b.length, timeouts);
    }

    private void setLog(final String s) {
        this.display.asyncExec(new Runnable(){

            @Override
            public void run() {
                BatchRunner.this.txtLog.append(String.valueOf(s) + "\n");
            }
        });
    }

    public static void main(String[] args) {
        byte[] bpsn = "87654321".getBytes();
        byte[] req = new byte[32];
        int i = 0;
        while (i < req.length) {
            req[i] = 0;
            ++i;
        }
        System.arraycopy(bpsn, 0, req, 5, bpsn.length);
        req[0] = 26;
        final byte[] res = new byte[1];
        new CommonCommand(null).interact(req, req.length, res, res.length);
        new Runnable(){

            @Override
            public void run() {
                byte cmd = res[0];
                String msg = "26 ";
                msg = cmd == 55 ? String.valueOf(msg) + ">> send success" : String.valueOf(msg) + "no response";
                System.out.println(String.valueOf(msg) + " " + cmd + "\r\n");
            }
        }.run();
    }

    public static final void sendsn(String psn, CommonCommand cc, MainFrm mf) {
        psn = "PDS5022S" + psn;
        byte[] bpsn = psn.getBytes();
        byte[] req = new byte[32];
        int i = 0;
        while (i < req.length) {
            req[i] = 0;
            ++i;
        }
        System.arraycopy(bpsn, 0, req, 5, bpsn.length);
        req[0] = 26;
        System.out.println();
        i = 0;
        while (i < req.length) {
            System.out.print(String.valueOf(Integer.toHexString(req[i] & 0xFF)) + " ");
            ++i;
        }
        System.out.println();
        byte[] res = new byte[1];
        cc.interact(req, req.length, res, res.length);
        byte cmd = res[0];
        String msg = cmd == 55 ? ">> send sn:" + psn + " success" : "no response";
        mf.logLn(msg);
    }

    public void getDefFile(String path) {
        this.setLog(MsgCenter.getString("MainFrm.debug"));
        String cmd = "READTXT";
        ByteBuffer bb = ByteBuffer.allocate(cmd.length() + 4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(cmd.length());
        bb.put(cmd.getBytes());
        this.getFileRunner = new GetFileRunner(path, bb.array());
        new Thread(this.getFileRunner).start();
    }

    public void setDefFile(final String path) {
        new Thread(new Runnable(){

            @Override
            public void run() {
                BatchRunner.this.cc.write("WRITETXT", path);
                BatchRunner.this.setLog("write finish");
            }
        }).start();
    }

    public void update(String content) {
        String cmd = "WRITESN" + content;
        ByteBuffer bb = ByteBuffer.allocate(cmd.length() + 4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(cmd.length());
        bb.put(cmd.getBytes());
        final byte[] req = bb.array();
        new Thread(new Runnable(){

            @Override
            public void run() {
                BatchRunner.this.cc.interact(req, req.length, null, 0);
                BatchRunner.this.setLog("write finish");
            }
        }).start();
    }

    private class CTVRefreshRunner
    implements Runnable {
        private CTVRefreshRunner() {
        }

        @Override
        public void run() {
            if (BatchRunner.this.ctv != null && !BatchRunner.this.ctv.getTable().isDisposed()) {
                BatchRunner.this.ctv.refresh();
            }
        }
    }

    private class CheckRebootRunner
    implements Runnable {
        private CheckRebootRunner() {
        }

        @Override
        public void run() {
            if (BatchRunner.this.mf.isAutoErase()) {
                byte[] req = new byte[]{27};
                byte[] res = new byte[1];
                BatchRunner.this.cc.interact(req, req.length, res, res.length);
                byte cmd = res[0];
                String msg = "\n>>>> ";
                msg = cmd == 55 ? String.valueOf(msg) + MsgCenter.getString("BR.ersuccess") : String.valueOf(msg) + MsgCenter.getString("BR.erfail");
                BatchRunner.this.txtLog.append(String.valueOf(msg) + " " + cmd + "\r\n");
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                req = new byte[]{25};
                BatchRunner.this.cc.interact(req, req.length, res, res.length);
                msg = "\n>>>> ";
                msg = cmd == 55 ? String.valueOf(msg) + MsgCenter.getString("BR.ersuccess") : String.valueOf(msg) + MsgCenter.getString("BR.erfail");
                BatchRunner.this.txtLog.append(String.valueOf(msg) + " " + cmd + "\r\n");
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (BatchRunner.this.mf.getCheckReboot().getSelection()) {
                BatchRunner.this.reboot();
            }
        }
    }

    public class ClcRunner
    implements Runnable {
        private CommonCommand cc;
        private boolean cancel;
        private boolean isAddHead;
        private String path;

        public ClcRunner(CommonCommand cc, boolean isAddHead, String path) {
            this.cc = cc;
            this.isAddHead = isAddHead;
            this.path = path;
        }

        public void setcnacel() {
            this.cancel = true;
        }

        @Override
        public void run() {
            try {
                BatchRunner.this.sc.init();
                if (BatchRunner.this.sc.comm("$Output,1,,794#".getBytes()).equals("")) {
                    BatchRunner.this.setLog("serical do not connected");
                    return;
                }
                String startCom = "STARTCLCS";
                String s = BatchRunner.this.writeRead(startCom, this.isAddHead, 3000);
                if (s == null || !s.equals("$CLCSTART#")) {
                    BatchRunner.this.setLog("enter LCL fail,checked then restart");
                    return;
                }
                BatchRunner.this.setLog("enter LCL success");
                try {
                    BatchRunner.this.setLog(startCom);
                    s = "STARTCLCT";
                    while (!this.cancel) {
                        BatchRunner.this.setLog("s:" + s);
                        s = BatchRunner.this.writeRead(s, this.isAddHead, 180000);
                        BatchRunner.this.setLog("r:" + s);
                        if (s.equals("") || s.equals("$CLCEND#")) {
                            BatchRunner.this.setLog("CLCEND");
                            if (this.isAddHead) {
                                BatchRunner.this.getDefFile(this.path);
                            }
                            break;
                        }
                        if (!s.startsWith("$") && !s.endsWith("#")) {
                            BatchRunner.this.setLog("error");
                            break;
                        }
                        s = "STARTCLCG" + BatchRunner.this.sc.comm(s.getBytes());
                    }
                }
                catch (Exception e) {
                    BatchRunner.this.setLog(e.getMessage());
                }
            }
            finally {
                BatchRunner.this.mf.setClcEnable(true);
                BatchRunner.this.sc.release();
            }
        }
    }

    public class DebugRunner
    implements Runnable {
        private String path;
        private String fileFormat;

        public DebugRunner(String path, String fileFormat) {
            this.path = path;
            this.fileFormat = fileFormat;
        }

        @Override
        public void run() {
            BatchRunner.this.usbCom.doGetTxt(this.path, this.fileFormat);
        }
    }

    private class GetFileRunner
    implements Runnable {
        private String path;
        private byte[] cmd;

        public GetFileRunner(String path, byte[] cmd) {
            this.path = path;
            this.cmd = cmd;
        }

        @Override
        public void run() {
            BatchRunner.this.usbCom.getFile(this.path, this.cmd);
        }
    }

    private class HandleEventRunner
    implements Runnable {
        private DLEvent e;

        public void setDLEvent(DLEvent e) {
            this.e = e;
        }

        @Override
        public void run() {
            int value = this.e.intValue;
            switch (this.e.type) {
                case 1: {
                    BatchRunner.this.progressBar.setSelection(value);
                    break;
                }
                case 2: {
                    BatchRunner.this.progressBar.setMaximum(value);
                    BatchRunner.this.progressLabel.setText(String.valueOf(MsgCenter.getString("BR.injecting")) + BatchRunner.this.df.getType() + " ...");
                    break;
                }
                case 3: {
                    break;
                }
                case 4: {
                    break;
                }
                case 5: {
                    BatchRunner.this.txtLog.append(String.valueOf(this.e.data.toString()) + "\n");
                    break;
                }
                case 6: {
                    byte[] commands = (byte[])this.e.data;
                    BatchRunner.this.txtLog.append("\n");
                    int length = commands.length;
                    int i = 0;
                    while (i < length) {
                        BatchRunner.this.txtLog.append("0x" + Integer.toHexString(commands[i] & 0xFF) + " ");
                        ++i;
                    }
                    BatchRunner.this.txtLog.append("\n");
                    break;
                }
                case 7: {
                    IDevice[] ids = (IDevice[])this.e.data;
                    BatchRunner.this.cobUsb.removeAll();
                    int length = ids.length;
                    int i = 0;
                    while (i < length) {
                        String sn = ids[i].getSerialNumber();
                        if (sn == null) {
                            sn = "?";
                        }
                        BatchRunner.this.cobUsb.add(String.valueOf(i + 1) + ".(SN:" + sn + ")");
                        ++i;
                    }
                    BatchRunner.this.cobUsb.select(0);
                    System.out.println("refresh");
                }
            }
        }
    }

    private class ResopneThread
    extends Thread {
        private CommonCommand cc;
        private byte command;

        public ResopneThread(CommonCommand cc, byte command) {
            this.cc = cc;
            this.command = command;
        }

        @Override
        public void run() {
            byte[] req = new byte[]{this.command};
            final byte[] res = new byte[1];
            this.cc.interact(req, req.length, res, res.length);
            BatchRunner.this.display.asyncExec(new Runnable(){

                @Override
                public void run() {
                    byte cmd = res[0];
                    String msg = "\n>>>> ";
                    msg = cmd == 55 ? String.valueOf(msg) + MsgCenter.getString("BR.ersuccess") : String.valueOf(msg) + MsgCenter.getString("BR.erfail");
                    BatchRunner.this.txtLog.append(String.valueOf(msg) + " " + cmd + "\r\n");
                }
            });
        }
    }

    private class SendSNRunner
    implements Runnable {
        private CommonCommand cc;

        public SendSNRunner(CommonCommand cc) {
            this.cc = cc;
        }

        @Override
        public void run() {
            byte[] bpsn = "09080005".getBytes();
            byte[] req = new byte[32];
            int i = 0;
            while (i < req.length) {
                req[i] = 0;
                ++i;
            }
            System.arraycopy(bpsn, 0, req, 5, bpsn.length);
            req[0] = 26;
            final byte[] res = new byte[1];
            this.cc.interact(req, req.length, res, res.length);
            BatchRunner.this.display.asyncExec(new Runnable(){

                @Override
                public void run() {
                    byte cmd = res[0];
                    String msg = cmd == 55 ? ">> send success" : "no response";
                    BatchRunner.this.txtLog.append(msg);
                }
            });
        }
    }

    public class StartUSBRunner
    implements Runnable {
        private CommonCommand cc;

        public StartUSBRunner(CommonCommand cc) {
            this.cc = cc;
        }

        @Override
        public void run() {
            byte[] req = new byte[]{21};
            final byte[] res = new byte[1];
            this.cc.interact(req, req.length, res, res.length);
            BatchRunner.this.display.asyncExec(new Runnable(){

                @Override
                public void run() {
                    byte cmd = res[0];
                    String msg = "21 ";
                    msg = cmd == 55 ? String.valueOf(msg) + ">> " + MsgCenter.getString("BR.success") : String.valueOf(msg) + MsgCenter.getString("BR.fail");
                    BatchRunner.this.txtLog.append(String.valueOf(msg) + " " + cmd + "\r\n");
                }
            });
        }
    }

    public class TRunner
    implements Runnable {
        private CommonCommand cc;
        private boolean cancel;
        private String path;

        public TRunner(CommonCommand cc) {
            this.cc = cc;
        }

        public void setcnacel() {
            this.cancel = true;
        }

        public void setPath(String path) {
            this.path = path;
        }

        private void saveFile(String s) {
            String fileName = s.substring(0, 20).trim();
            String content = s.substring(20);
            if (!this.path.equals("") && !this.path.endsWith("\\")) {
                this.path = String.valueOf(this.path) + "\\";
            }
            this.path = String.valueOf(this.path) + fileName;
            File f = new File(this.path);
            BatchRunner.this.setLog("File Save Path:" + this.path);
            try {
                FileWriter fw = new FileWriter(f);
                fw.write(content);
                fw.flush();
                fw.close();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                BatchRunner.this.sc.init();
                if (BatchRunner.this.sc.comm("$Output,1,,794#".getBytes()).equals("")) {
                    BatchRunner.this.setLog("serical do not connected");
                    return;
                }
                String startCom = "STARTCHECK";
                BatchRunner.this.setLog(startCom);
                byte[] req = startCom.getBytes();
                String s = this.cc.comm(req, req.length, 3000);
                if (s == null || !s.equals("$AMPDSTART#")) {
                    BatchRunner.this.setLog("AMPDSTART fail");
                    return;
                }
                BatchRunner.this.setLog("AMPDSTART success");
                try {
                    req = "STARTCHECT".getBytes();
                    while (!this.cancel) {
                        BatchRunner.this.setLog("s:" + new String(req));
                        s = this.cc.comm(req, req.length, 180000);
                        if (s.equals("") || s.startsWith("$CHECKEND#")) {
                            int index = s.indexOf("#");
                            BatchRunner.this.setLog("r:" + s.substring(0, index));
                            this.saveFile(s.substring(index + 1));
                            break;
                        }
                        BatchRunner.this.setLog("r:" + s);
                        if (!s.startsWith("$") && !s.endsWith("#")) {
                            BatchRunner.this.setLog("error");
                            break;
                        }
                        s = "STARTCLCG" + BatchRunner.this.sc.comm(s.getBytes());
                        req = s.getBytes();
                    }
                }
                catch (Exception e) {
                    BatchRunner.this.setLog(e.getMessage());
                }
            }
            finally {
                BatchRunner.this.mf.setTEnable(true);
                BatchRunner.this.sc.release();
            }
        }
    }
}

