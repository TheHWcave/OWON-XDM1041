/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.selfupgrade;

public class Line {
    private String key;
    private String value;

    public Line(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(this.key) + "=" + this.value + ";\n";
    }
}

