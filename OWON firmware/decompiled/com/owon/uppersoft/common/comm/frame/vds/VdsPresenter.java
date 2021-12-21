/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.frame.vds;

import com.owon.uppersoft.common.comm.frame.txtedit.IShowMessage;
import com.owon.uppersoft.common.comm.frame.vds.IRefreshCom;
import com.owon.uppersoft.common.comm.frame.vds.VisaCommunication;
import com.owon.uppersoft.common.comm.frame.vds.WriteItem;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.omap.ZImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

public class VdsPresenter {
    private IShowMessage showMessage;
    private IRefreshCom refreshCom;
    private VisaCommunication visa;
    private String txtFileName;

    public VdsPresenter(IShowMessage showMessage, IRefreshCom refreshCom) {
        this.showMessage = showMessage;
        this.refreshCom = refreshCom;
        this.visa = new VisaCommunication();
    }

    public void refreshCom() {
        System.out.println("refresh");
        List<String> list = this.visa.findSrc();
        this.refreshCom.refreshCom(list);
    }

    public void setCom(String s) {
        if (s.equalsIgnoreCase("")) {
            return;
        }
        this.visa.open(s);
        String sn = s.split("::")[3];
        this.txtFileName = sn.equalsIgnoreCase("") ? "\\unknown.bin" : "\\" + sn + ".bin";
    }

    public void sendCmd(String cmd) {
        byte[] send = cmd.getBytes();
        this.visa.write(send, 0, send.length);
        byte[] newarr = new byte[1024];
        int ii = this.visa.read(newarr, 0, newarr.length);
        if (ii > 0) {
            System.out.println(new String(newarr, 0, ii));
        }
    }

    public void readFile(String cmd, String path) {
        byte[] send = cmd.getBytes();
        this.visa.write(send, 0, send.length);
        byte[] newarr = new byte[102400];
        int ii = this.visa.read(newarr, 0, newarr.length);
        if (ii > 10 && newarr[0] == 35) {
            int len = newarr[1] - 48;
            String result = new String(newarr, 2, len);
            int num = Integer.parseInt(result);
            System.out.println(ii);
            try {
                path = String.valueOf(path) + this.txtFileName;
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(newarr, len + 2, num);
                fos.flush();
                fos.close();
                this.showMessage.showInfo(String.valueOf(MsgCenter.getString("vds.readfinish")) + path);
            }
            catch (IOException e) {
                e.printStackTrace();
                this.showMessage.showInfo(MsgCenter.getString("vds.errComm"));
            }
        }
    }

    public void writeFiles(List<WriteItem> items) {
        Iterator<WriteItem> it = items.iterator();
        try {
            block2: while (it.hasNext()) {
                WriteItem wi = it.next();
                if (!this.writeFile(wi)) continue;
                wi.setStatus(false);
                int i = 0;
                while (i < 10) {
                    this.visa.writeString(":UPLoad:COMPlete:" + wi.getName() + "?");
                    if (this.visa.readString().trim().equalsIgnoreCase("1->")) {
                        wi.setStatus(true);
                        continue block2;
                    }
                    Thread.sleep(200L);
                    ++i;
                }
            }
        }
        catch (Exception exception) {}
    }

    public boolean writeFile(WriteItem wi) {
        if (!wi.isEnable() || wi.getPath().equalsIgnoreCase("")) {
            return false;
        }
        try {
            FileInputStream fis = new FileInputStream(wi.getPath());
            int len = fis.available() + 100;
            int lenlen = String.valueOf(len).length();
            String cmd = "#" + lenlen + len;
            byte[] tmp = cmd.getBytes();
            int i = tmp.length + len;
            ByteBuffer bb = ByteBuffer.allocate(i);
            byte[] b = new byte[len];
            b[0] = (byte)wi.getType();
            fis.read(b, 100, len - 100);
            bb.put(tmp).put(b);
            this.visa.write(bb.array(), 0, i);
            fis.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            this.showMessage.showInfo(MsgCenter.getString("vds.errComm"));
            return false;
        }
    }

    public void readXdsTxt(String cmd, String path) {
        byte[] send = cmd.getBytes();
        this.visa.write(send, 0, send.length);
        byte[] newarr = new byte[10000];
        int ii = this.visa.read(newarr, 0, newarr.length);
        if (ii > 10 && newarr[0] == 35) {
            int len = newarr[1] - 48;
            String result = new String(newarr, 2, len);
            int num = Integer.parseInt(result);
            System.out.println(ii);
            try {
                path = String.valueOf(path) + this.txtFileName;
                FileOutputStream fos = new FileOutputStream(path);
                int fileLen = ii - len - 2;
                fos.write(newarr, len + 2, fileLen);
                while (fileLen < num) {
                    ii = this.visa.read(newarr, 0, newarr.length);
                    fos.write(newarr, 0, ii);
                    fileLen += ii;
                }
                fos.flush();
                fos.close();
                this.showMessage.showInfo(String.valueOf(MsgCenter.getString("vds.readfinish")) + path);
            }
            catch (IOException e) {
                e.printStackTrace();
                this.showMessage.showInfo(MsgCenter.getString("vds.errComm"));
            }
        }
    }

    public boolean writeXdsTxt(String cmd, String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            int len = fis.available();
            byte[] b = new byte[len];
            fis.read(b);
            fis.close();
            return this.writeXdsTxt(cmd, b);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean writeXdsTxt(String cmd, byte[] b) {
        try {
            int len = b.length;
            int lenlen = String.valueOf(len).length();
            cmd = String.valueOf(cmd) + "#" + lenlen + len;
            byte[] tmp = cmd.getBytes();
            int i = tmp.length + len;
            ByteBuffer bb = ByteBuffer.allocate(i);
            bb.put(tmp).put(b);
            if (this.write(bb)) {
                this.showMessage.showInfo(MsgCenter.getString("vds.writefinish"));
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.showMessage.showInfo(MsgCenter.getString("vds.errComm"));
        return false;
    }

    public boolean writeImage(String cmd, String sn, boolean isShowFinish) {
        String script = "setenv bootargs console=ttymxc0,115200 androidboot.console=ttymxc0 vmalloc=400M init=/init video=mxcfb0:dev=ldb,LDB-WXGA,if=RGB24,bpp=32 video=mxcfb1:dev=hdmi,1920x1080M@60,if=RGB24,bpp=32 ldb=sin1 audio_codec=wm8960-24M fb0base=0x27b00000 fbmem=28M,28M fec_mac=52:3F:D0:98:00:EB ldb_on_di1 androidboot.hardware=freescale androidboot.serialno=FFFFFFFF\nsetenv disp_uboot echo ************ u-boot.bin file ************\nsetenv res printenv\nsetenv start run res\nsaveenv\nrun start\nrun disp_uboot\n";
        script = script.replace("FFFFFFFF", sn);
        byte[] b = script.getBytes();
        int len = b.length;
        byte[] txt = new byte[len + 8];
        txt[0] = (byte)(len >> 24 & 0xFF);
        txt[1] = (byte)(len >> 16 & 0xFF);
        txt[2] = (byte)(len >> 8 & 0xFF);
        txt[3] = (byte)(len & 0xFF);
        txt[4] = 0;
        txt[5] = 0;
        txt[6] = 0;
        txt[7] = 0;
        System.arraycopy(b, 0, txt, 8, len);
        ByteBuffer image = new ZImage(txt).getImage();
        len = image.capacity();
        int lenlen = String.valueOf(len).length();
        cmd = String.valueOf(cmd) + "#" + lenlen + len;
        System.out.println(String.valueOf(cmd) + len);
        byte[] tmp = cmd.getBytes();
        int i = tmp.length + len;
        ByteBuffer bb = ByteBuffer.allocate(i);
        bb.put(tmp).put(image.array());
        if (this.write(bb)) {
            if (isShowFinish) {
                this.showMessage.showInfo(MsgCenter.getString("vds.writefinish"));
            }
            return true;
        }
        this.showMessage.showInfo(MsgCenter.getString("vds.errComm"));
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean write(ByteBuffer bb) {
        try {
            int len = bb.capacity();
            System.out.println(len);
            int send = 0;
            int p = 0;
            bb.flip();
            while (true) {
                if (p >= len) {
                    return true;
                }
                send = len - p > 10000 ? 10000 : len - p;
                byte[] b1 = new byte[send];
                bb.get(b1, 0, send);
                int rn = this.visa.write(b1, 0, b1.length);
                if (rn == -1) {
                    return false;
                }
                System.out.println(p += send);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

