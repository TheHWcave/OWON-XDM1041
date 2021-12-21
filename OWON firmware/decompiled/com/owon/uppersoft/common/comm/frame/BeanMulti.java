/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.frame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BeanMulti {
    private byte[] content;
    private String manu;
    private String model;
    private String ver;
    private String sn;
    private int lang;
    private int func;
    private byte specialSW;

    public byte getSpecialSW() {
        return this.specialSW;
    }

    public void setSpecialSW(byte specialSW) {
        this.specialSW = specialSW;
    }

    public String getManu() {
        return this.manu;
    }

    public void setManu(String manu) {
        this.manu = manu;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVer() {
        return this.ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getLang() {
        return this.lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public int getFunc() {
        return this.func;
    }

    public void setFunc(int func) {
        this.func = func;
    }

    public void read(File f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            this.content = new byte[fis.available()];
            fis.read(this.content);
            this.ver = new String(this.content, 20, 20);
            this.sn = new String(this.content, 40, 40);
            this.model = new String(this.content, 80, 20);
            this.manu = new String(this.content, 100, 20);
            this.func = this.getInt(120, 19);
            this.lang = this.getInt(140, 20);
            this.specialSW = this.content[160];
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String s) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(s));
            this.copyWithLen(20, 20, this.ver);
            this.copyWithLen(40, 40, this.sn);
            this.copyWithLen(80, 20, this.model);
            this.copyWithLen(100, 20, this.manu);
            this.setInt(this.func, 120, 19);
            this.setInt(this.lang, 140, 20);
            this.content[160] = this.specialSW;
            fos.write(this.content);
            fos.flush();
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyWithLen(int start, int len, String s) {
        ByteBuffer bb = ByteBuffer.allocate(len);
        byte[] b = s.getBytes();
        if (b.length < len) {
            len = b.length;
        }
        bb.put(s.getBytes(), 0, len);
        b = bb.array();
        System.arraycopy(b, 0, this.content, start, b.length);
    }

    public int getInt(int start, int len) {
        int value = 0;
        int i = 0;
        while (i < len) {
            value += this.content[start + i] << i;
            ++i;
        }
        return value;
    }

    public void setInt(int value, int start, int len) {
        int i = 0;
        while (i < len) {
            this.content[start + i] = (byte)(value >> i & 1);
            ++i;
        }
    }
}

