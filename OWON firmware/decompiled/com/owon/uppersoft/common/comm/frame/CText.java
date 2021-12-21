/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class CText
extends Text {
    public CText(Composite parent, int style) {
        super(parent, style);
    }

    public void append(String s) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        super.append("[" + df.format(new Date()) + "]" + s);
    }

    public void checkSubclass() {
    }
}

