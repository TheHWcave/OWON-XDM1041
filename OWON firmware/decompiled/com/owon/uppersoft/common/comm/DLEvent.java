/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm;

import java.util.EventObject;

public class DLEvent
extends EventObject {
    private static final long serialVersionUID = 6380222070073865592L;
    public static final int PROGRESS = 1;
    public static final int PROGRESS_RANGE = 2;
    public static final int COMM_SPEED = 3;
    public static final int DOWNLOAD_SPEED = 4;
    public static final int LOG = 5;
    public static final int COMMAND_DETAIL = 6;
    public static final int REFRESHUSB = 7;
    public int type;
    public int intValue;
    public double dblValue;
    public Object data;

    public DLEvent(Object source, int type, int intValue) {
        super(source);
        this.type = type;
        this.intValue = intValue;
    }

    public DLEvent(Object source, int type, double dblValue) {
        super(source);
        this.type = type;
        this.dblValue = dblValue;
    }

    public DLEvent(Object source, int type, Object data) {
        super(source);
        this.type = type;
        this.data = data;
    }
}

