/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.patch.direct;

import com.owon.uppersoft.common.preference.PropertiesUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Reg {
    private String type = "osc";
    private int downModel = 1;
    private boolean isNeedSN = false;
    private String SN = "1231412341241";
    private boolean isEraseFLASH = true;
    private boolean isEraseBOOT = true;
    private boolean isEraseTABLE;
    private boolean isEraseBAK;
    private int waitTime = 500;
    private int eraseBootTime;
    private int eraseTableTime;
    private int eraseBackupTime;
    private int step = 3;
    private String ini = "proper.ini";

    public void load() {
        Properties p = new Properties();
        try {
            FileInputStream is = new FileInputStream(this.ini);
            p.load(is);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.type = p.getProperty("type");
        this.downModel = PropertiesUtil.loadInt(p, "downmodel");
        this.isNeedSN = PropertiesUtil.loadBoolean(p, "isNeedSN");
        this.SN = p.getProperty("sn");
        this.isEraseFLASH = PropertiesUtil.loadBoolean(p, "isEraseFLASH");
        this.isEraseBOOT = PropertiesUtil.loadBoolean(p, "isEraseBOOT");
        this.isEraseTABLE = PropertiesUtil.loadBoolean(p, "isEraseTABLE");
        this.isEraseBAK = PropertiesUtil.loadBoolean(p, "isEraseBAK");
        this.waitTime = PropertiesUtil.loadInt(p, "waitTime");
        this.eraseBootTime = PropertiesUtil.loadInt(p, "eraseBootTime");
        this.eraseTableTime = PropertiesUtil.loadInt(p, "eraseTableTime");
        this.eraseBackupTime = PropertiesUtil.loadInt(p, "eraseBackupTime");
        this.step = PropertiesUtil.loadInt(p, "step");
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDownModel() {
        return this.downModel;
    }

    public void setDownModel(int downModel) {
        this.downModel = downModel;
    }

    public boolean isNeedSN() {
        return this.isNeedSN;
    }

    public void setNeedSN(boolean isNeedSN) {
        this.isNeedSN = isNeedSN;
    }

    public String getSN() {
        return this.SN;
    }

    public void setSN(String sn) {
        this.SN = sn;
    }

    public boolean isEraseFLASH() {
        return this.isEraseFLASH;
    }

    public void setEraseFLASH(boolean isEraseFLASH) {
        this.isEraseFLASH = isEraseFLASH;
    }

    public boolean isEraseBOOT() {
        return this.isEraseBOOT;
    }

    public void setEraseBOOT(boolean isEraseBOOT) {
        this.isEraseBOOT = isEraseBOOT;
    }

    public int getWaitTime() {
        return this.waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public boolean isEraseTABLE() {
        return this.isEraseTABLE;
    }

    public void setEraseTABLE(boolean isEraseTABLE) {
        this.isEraseTABLE = isEraseTABLE;
    }

    public boolean isEraseBAK() {
        return this.isEraseBAK;
    }

    public void setEraseBAK(boolean isEraseBAK) {
        this.isEraseBAK = isEraseBAK;
    }

    public int getEraseBootTime() {
        return this.eraseBootTime;
    }

    public void setEraseBootTime(int eraseBootTime) {
        this.eraseBootTime = eraseBootTime;
    }

    public int getEraseTableTime() {
        return this.eraseTableTime;
    }

    public void setEraseTableTime(int eraseTableTime) {
        this.eraseTableTime = eraseTableTime;
    }

    public int getEraseBackupTime() {
        return this.eraseBackupTime;
    }

    public void setEraseBackupTime(int eraseBackupTime) {
        this.eraseBackupTime = eraseBackupTime;
    }

    public int getStep() {
        return this.step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}

