/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.omap;

import com.owon.uppersoft.omap.ZImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptEntry {
    private String[] lines;
    private final String IP = "((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])";
    private final String Mac = "[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}";
    private String name;
    private ByteBuffer bb;

    public ScriptEntry(File f) {
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(f));
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTXT = null;
            StringBuffer sb = new StringBuffer();
            while ((lineTXT = bufferedReader.readLine()) != null) {
                sb.append(String.valueOf(lineTXT) + "\r");
            }
            read.close();
            this.lines = sb.toString().split("\r");
            this.name = f.getName().replace(".txt", ".sh");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        int i = 0;
        while (i < this.lines.length) {
            Pattern p;
            Matcher m;
            if (this.lines[i].contains(key) && (m = (p = Pattern.compile("((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])|[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}")).matcher(this.lines[i])).find()) {
                return m.group();
            }
            ++i;
        }
        return "";
    }

    public void set(String key, String replacement) {
        int i = 0;
        while (i < this.lines.length) {
            if (this.lines[i].contains(key)) {
                this.lines[i] = "setenv" + key + replacement;
            }
            ++i;
        }
    }

    public void update(String ipaddr, String gatewayip, String serverip, String serial, String path, String type, String ver) {
        this.set(" ethaddr ", this.getMacAdd());
        this.set(" ipaddr ", ipaddr);
        this.set(" gatewayip ", gatewayip);
        this.set(" serverip ", serverip);
        this.set(" serial ", serial);
        this.set(" version ", ver);
        this.set(" downpath ", path);
        this.set(" download ", String.valueOf(type) + "/download");
    }

    public void update(String serial, String ver) {
        this.set(" ethaddr ", this.getMacAdd());
        this.set(" serial ", serial);
        this.set(" version ", ver);
    }

    private String getMacAdd() {
        String rnd1 = this.byteHex((int)(Math.random() * 256.0));
        String rnd2 = this.byteHex((int)(Math.random() * 256.0));
        String rnd3 = this.byteHex((int)(Math.random() * 256.0));
        String rnd4 = this.byteHex((int)(Math.random() * 256.0));
        String rnd5 = this.byteHex((int)(Math.random() * 256.0));
        String rnd6 = this.byteHex((int)(Math.random() * 256.0));
        return String.valueOf(rnd1) + ":" + rnd2 + ":" + rnd3 + ":" + rnd4 + ":" + rnd5 + ":" + rnd6;
    }

    private String byteHex(int i) {
        char[] hexArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[2];
        int v = i & 0xFF;
        hexChars[0] = hexArray[v / 16];
        hexChars[1] = hexArray[v % 16];
        return new String(hexChars);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (i < this.lines.length) {
            sb.append(String.valueOf(this.lines[i]) + "\n");
            ++i;
        }
        return sb.toString();
    }

    public byte[] getBytes() {
        return this.toString().getBytes();
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.bb.remaining();
    }

    public void initBB() {
        this.bb = this.txt2Sh();
        this.bb.flip();
    }

    public void read(byte[] b) {
        int len = b.length;
        if (len > this.bb.remaining()) {
            len = this.bb.remaining();
        }
        this.bb.get(b, 0, len);
    }

    public int available() {
        return this.bb.remaining();
    }

    private ByteBuffer txt2Sh() {
        byte[] b = this.getBytes();
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
        return new ZImage(txt).getImage();
    }

    public void save(String fileName) {
        this.initBB();
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(this.bb.array());
            fos.flush();
            fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

