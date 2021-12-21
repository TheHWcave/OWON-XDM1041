/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionUtil {
    private String name;
    private String fullVer;
    private String buildVer;
    private String ver;

    public static void main(String[] args) {
        VersionUtil vu = new VersionUtil("com.owon.uppersoft.odp_1.0.9.455");
        System.out.println(vu.getVer());
    }

    public VersionUtil(String ver) {
        if (ver == null) {
            return;
        }
        Pattern pattern = Pattern.compile(".*_(\\d*\\.|v\\d*){2,}\\d*|v\\d*");
        Matcher matcher = pattern.matcher(ver);
        if (matcher.matches()) {
            String[] s = ver.split("_");
            this.name = s[0];
            this.fullVer = s[1];
            int index = this.fullVer.lastIndexOf(".");
            if (this.fullVer.startsWith("v", index + 1)) {
                this.buildVer = this.fullVer.substring(index + 2);
                this.ver = this.fullVer.substring(0, index);
            } else {
                this.ver = this.fullVer;
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullVer() {
        return this.fullVer;
    }

    public void setFullVer(String fullVer) {
        this.fullVer = fullVer;
    }

    public String getBuildVer() {
        return this.buildVer;
    }

    public void setBuildVer(String buildVer) {
        this.buildVer = buildVer;
    }

    public String getVer() {
        return this.ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getAboutVer() {
        StringBuilder sb = new StringBuilder();
        sb.append("Version:");
        sb.append(this.ver);
        sb.append(" (bulid:");
        sb.append(this.buildVer);
        sb.append(")");
        return sb.toString();
    }
}

