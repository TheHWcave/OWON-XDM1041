/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.omap.usb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.Element;

public class DownloadItem {
    private String cmd;
    private long add;
    private long fileLen;
    private String fileName;
    private InputStream is;

    public static void main(String[] args) {
    }

    public DownloadItem(Element n, String path) {
        if (n.hasAttribute("address")) {
            this.add = Long.decode(n.getAttribute("address"));
        }
        if (n.hasAttribute("cmd")) {
            this.cmd = n.getAttribute("cmd");
        }
        if (n.hasAttribute("fileName")) {
            this.fileName = String.valueOf(path) + n.getAttribute("fileName");
            try {
                this.is = new FileInputStream(this.fileName);
                this.fileLen = this.is.available();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this.toString());
    }

    public String getCmd() {
        return this.cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public long getAdd() {
        return this.add;
    }

    public void setAdd(long add) {
        this.add = add;
    }

    public long getFileLen() {
        return this.fileLen;
    }

    public void setFileLen(long fileLen) {
        this.fileLen = fileLen;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int read(byte[] b) {
        try {
            return this.is.read(b);
        }
        catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public byte[] readFile() {
        try {
            byte[] b = new byte[this.is.available()];
            this.is.read(b);
            this.is.close();
            return b;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("cmd:" + this.cmd + ",");
        sb.append("add:" + Long.toHexString(this.add) + ",");
        sb.append("fileName:" + this.fileName + ",");
        sb.append("fileLen:" + Long.toHexString(this.fileLen));
        return sb.toString();
    }
}

