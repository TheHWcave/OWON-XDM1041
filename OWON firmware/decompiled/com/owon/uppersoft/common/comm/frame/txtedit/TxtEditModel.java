/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.frame.txtedit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TxtEditModel {
    private byte[] content;
    private String series;
    private String ver;
    private String sn;
    private String intModel;
    private String extModel;
    private String manufId;
    private String clientversion;
    private String clientSum;
    private boolean[] func;
    private boolean[] lang;
    private int defLanguage;
    private String txtVer;
    private byte[] macAdd;

    public byte[] getMacAdd() {
        return this.macAdd;
    }

    public void setMacAdd(byte[] macAdd) {
        this.macAdd = macAdd;
    }

    public String getSeries() {
        return this.series;
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

    public String getIntModel() {
        return this.intModel;
    }

    public void setIntModel(String intModel) {
        this.intModel = intModel;
    }

    public String getExtModel() {
        return this.extModel;
    }

    public void setExtModel(String extModel) {
        this.extModel = extModel;
    }

    public String getManufId() {
        return this.manufId;
    }

    public void setManufId(String manufId) {
        this.manufId = manufId;
    }

    public boolean[] getFunc() {
        return this.func;
    }

    public void setFunc(boolean[] func) {
        this.func = func;
    }

    public boolean[] getLang() {
        return this.lang;
    }

    public void setLang(boolean[] lang) {
        this.lang = lang;
    }

    public int getDefLanguage() {
        return this.defLanguage;
    }

    public void setDefLanguage(int defLanguage) {
        this.defLanguage = defLanguage;
    }

    public String getTxtVer() {
        return this.txtVer;
    }

    public void setTxtVer(String txtVer) {
        this.txtVer = txtVer;
    }

    public String getClientversion() {
        return this.clientversion;
    }

    public void setClientversion(String clientversion) {
        this.clientversion = clientversion;
    }

    public String getClientSum() {
        return this.clientSum;
    }

    public void setClientSum(String clientSum) {
        this.clientSum = clientSum;
    }

    public void read(File f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            this.content = new byte[fis.available()];
            fis.read(this.content);
            this.series = new String(this.content, 0, 20);
            this.ver = new String(this.content, 20, 20);
            this.sn = new String(this.content, 40, 20);
            this.intModel = new String(this.content, 60, 20);
            this.extModel = new String(this.content, 80, 20);
            this.manufId = new String(this.content, 100, 20);
            this.func = new boolean[80];
            this.lang = new boolean[80];
            this.initOnOff(this.func, 120, 80);
            this.initOnOff(this.lang, 200, 80);
            this.defLanguage = this.content[280];
            this.txtVer = new String(this.content, 281, 20);
            this.macAdd = new byte[6];
            System.arraycopy(this.content, 301, this.macAdd, 0, 6);
            this.clientversion = new String(this.content, 307, 20);
            this.clientSum = new String(this.content, 327, 20);
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
            this.copyWithLen(40, 20, this.sn);
            this.copyWithLen(60, 20, this.intModel);
            this.copyWithLen(80, 20, this.extModel);
            this.copyWithLen(100, 20, this.manufId);
            this.setOnOff(this.func, 120, 80);
            this.setOnOff(this.lang, 200, 80);
            this.content[280] = (byte)this.defLanguage;
            this.copyWithLen(281, 20, this.txtVer);
            System.arraycopy(this.macAdd, 0, this.content, 301, 6);
            this.copyWithLen(307, 20, this.clientversion);
            this.copyWithLen(327, 20, this.clientSum);
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

    private void initOnOff(boolean[] value, int start, int len) {
        int i = 0;
        while (i < len) {
            value[i] = this.content[start + i] != 0;
            ++i;
        }
    }

    private void setOnOff(boolean[] value, int start, int len) {
        int i = 0;
        while (i < len) {
            this.content[start + i] = (byte)(value[i] ? 1 : 0);
            ++i;
        }
    }
}

