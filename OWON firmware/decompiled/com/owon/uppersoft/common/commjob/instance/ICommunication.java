/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.commjob.instance;

public interface ICommunication {
    public static final int RWTimeOut = 1000;

    public int read(byte[] var1, int var2, int var3);

    public int write(byte[] var1, int var2, int var3);

    public boolean open(Object[] var1);

    public void close();

    public boolean isConnected();
}

