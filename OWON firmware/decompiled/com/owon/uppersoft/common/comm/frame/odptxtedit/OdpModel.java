/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.frame.odptxtedit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class OdpModel {
    private byte[] content;
    private String manu;
    private String model;
    private String ver;
    private String sn;
    private int lang;
    private int func;
    private byte type;
    private byte hard;
    private boolean oem;
    private boolean logo;
    private ByteBuffer oemValue;

    public byte getType() {
        return this.type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getHard() {
        return this.hard;
    }

    public void setHard(byte hard) {
        this.hard = hard;
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

    public boolean isOem() {
        return this.oem;
    }

    public void setOem(boolean oem) {
        this.oem = oem;
    }

    public boolean isLogo() {
        return this.logo;
    }

    public void setLogo(boolean logo) {
        this.logo = logo;
    }

    public ByteBuffer getOemValue() {
        return this.oemValue;
    }

    public void setOemValue(ByteBuffer oemValue) {
        this.oemValue = oemValue;
    }

    public void read(File f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            int leng = fis.available();
            this.content = new byte[leng];
            fis.read(this.content);
            this.ver = new String(this.content, 20, 20);
            this.sn = new String(this.content, 40, 40);
            this.model = new String(this.content, 80, 20);
            this.manu = new String(this.content, 100, 20);
            this.func = this.getInt(120, 19);
            this.lang = this.getInt(140, 20);
            this.type = this.content[160];
            this.hard = this.content[161];
            this.oem = this.getBit(6, 19);
            this.logo = this.getBit(7, 19);
            this.oemValue = ByteBuffer.allocate(60);
            this.oemValue.order(ByteOrder.LITTLE_ENDIAN);
            this.oemValue.put(this.content, 164, 60);
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
            this.content[160] = this.type;
            this.content[161] = this.hard;
            this.setBit(6, 19, this.oem);
            this.setBit(7, 19, this.logo);
            if (this.oem) {
                this.oemValue.flip();
                this.oemValue.get(this.content, 164, 60);
            }
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

    public boolean getBit(int bit, int index) {
        return (this.content[index] >> bit & 1) == 1;
    }

    public void setBit(int bit, int index, boolean b) {
        int[] c = new int[8];
        int i = 0;
        while (i < c.length) {
            c[i] = this.content[index] >> i & 1;
            ++i;
        }
        c[bit] = b ? 1 : 0;
        int sum = 0;
        int i2 = 0;
        while (i2 < c.length) {
            sum += c[i2] << i2;
            ++i2;
        }
        System.out.println(Integer.toBinaryString(sum));
        this.content[index] = (byte)sum;
    }

    public String getSeries() {
        return "ODP";
    }
}

