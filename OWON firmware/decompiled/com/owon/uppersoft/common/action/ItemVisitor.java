/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.action;

public interface ItemVisitor<T> {
    public void invoke(T var1);

    public String getText(T var1);
}

