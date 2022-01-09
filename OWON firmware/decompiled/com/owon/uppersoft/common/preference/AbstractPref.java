/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.preference;

import com.owon.uppersoft.common.preference.PropertiesUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AbstractPref {
    protected String productId;
    protected String manufacturerId;
    protected String versionId;
    protected String internalVersionId;
    protected String updateServer;
    protected boolean updatable;
    protected String launcher;
    protected String localeDir;
    protected String exampleDir;
    protected String reinstallUSBDriverCommand;
    protected int fileHisCount;
    protected String[] localeNames;

    public static void main(String[] args) {
        AbstractPref ap = new AbstractPref();
        InputStream is = AbstractPref.class.getResourceAsStream("/com/owon/uppersoft/common/preference/default.ini");
        ap.load(is);
        System.out.println(ap.isNeutral());
        System.out.println(ap.getUpdateXML());
        System.out.println(ap.getVersionText());
    }

    protected void load(String filename) {
        this.load(new File(filename));
    }

    protected void load(File file) {
        try {
            this.load(new FileInputStream(file));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void load(InputStream is) {
        Properties p = new Properties();
        try {
            p.load(is);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.load(p);
    }

    public void load(Properties p) {
        this.productId = p.getProperty("productId");
        this.manufacturerId = p.getProperty("manufacturerId");
        this.versionId = p.getProperty("versionId");
        this.internalVersionId = p.getProperty("internalVersionId");
        this.updateServer = p.getProperty("updateServer");
        this.updatable = PropertiesUtil.loadBoolean(p, "updatable");
        this.launcher = p.getProperty("launcher");
        this.localeDir = p.getProperty("localeDir");
        this.exampleDir = p.getProperty("exampleDir");
        this.reinstallUSBDriverCommand = p.getProperty("reinstallUSBDriverCommand");
        this.fileHisCount = PropertiesUtil.loadInt(p, "fileHisCount");
        String lns = p.getProperty("availableLocales", "");
        this.localeNames = lns.split(";");
    }

    public String[] getAvailableLocaleNames() {
        return this.localeNames;
    }

    public String getProductId() {
        return this.productId;
    }

    public String getManufacturerId() {
        return this.manufacturerId;
    }

    public String getVersionId() {
        return this.versionId;
    }

    public String getInternalVersionId() {
        return this.internalVersionId;
    }

    public String getUpdateServer() {
        return this.updateServer;
    }

    public boolean getUpdatable() {
        return this.updatable;
    }

    public String getLauncher() {
        return this.launcher;
    }

    public String getExampleDir() {
        return this.exampleDir;
    }

    public int getFileHisCount() {
        return this.fileHisCount;
    }

    public String getLocaleDir() {
        return this.localeDir;
    }

    public String getUpdateXML() {
        return String.valueOf(this.manufacturerId) + "_" + this.productId + ".xml";
    }

    public String getVersionText() {
        return "Version " + this.versionId + " (build " + this.internalVersionId + ")";
    }

    public boolean isNeutral() {
        return this.manufacturerId.equalsIgnoreCase("neutral");
    }

    public String getReinstallUSBDriverCommand() {
        return this.reinstallUSBDriverCommand;
    }
}

