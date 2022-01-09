/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Propert {
    private static Propert propert;
    private Properties p;
    private boolean isEdit;
    private final String pref = "configuration/com.owon.uppersoft.tools/prop.pref";

    public static Propert getInstance() {
        if (propert == null) {
            propert = new Propert();
            propert.init();
        }
        return propert;
    }

    private void init() {
        this.p = new Properties();
        this.initItem();
        File f = new File("configuration/com.owon.uppersoft.tools/prop.pref");
        if (!f.exists()) {
            return;
        }
        try {
            FileInputStream is = new FileInputStream(f);
            this.p.load(is);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initItem() {
        this.p.setProperty("autoerase", "1");
        this.p.setProperty("debugpath", "");
        this.p.setProperty("odptxtpath", "");
        this.p.setProperty("debugpath", "");
    }

    public String get(String key) {
        return this.p.getProperty(key);
    }

    public void set(String key, String value) {
        this.p.setProperty(key, value);
        this.isEdit = true;
    }

    public void save() {
        if (!this.isEdit) {
            return;
        }
        try {
            File f = new File("configuration/com.owon.uppersoft.tools/prop.pref");
            File parent = f.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            System.out.print(f.getAbsolutePath());
            FileOutputStream is = new FileOutputStream(f);
            this.p.store(is, "tools propert");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

