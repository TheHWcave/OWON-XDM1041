/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.utils;

import java.util.Calendar;

public class TimeMeasure {
    long startT;
    long stopT;

    public void start() {
        this.startT = Calendar.getInstance().getTimeInMillis();
    }

    public void stop() {
        this.stopT = Calendar.getInstance().getTimeInMillis();
    }

    public long measure() {
        return this.stopT - this.startT;
    }

    public long measureInSecond() {
        return this.measure() / 1000L;
    }

    public static void main_hide(String[] args) {
        System.out.println(5);
    }
}

