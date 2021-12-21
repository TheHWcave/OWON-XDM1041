/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.layout.RowLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Layout
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;

public class CGroup
extends Group {
    private Button[] btns;

    public CGroup(Composite parent, int style, String title, String[] text) {
        super(parent, style);
        this.setText(title);
        this.setLayout((Layout)new RowLayout(256));
        this.btns = new Button[text.length];
        int i = 0;
        while (i < text.length) {
            this.btns[i] = new Button((Composite)this, 32);
            this.btns[i].setText(MsgCenter.getString(text[i]));
            ++i;
        }
    }

    public CGroup(Composite parent, int style, String title) {
        super(parent, style);
        this.setText(title);
        this.setLayout((Layout)new RowLayout(256));
        this.btns = new Button[20];
        int i = 0;
        while (i < this.btns.length) {
            this.btns[i] = new Button((Composite)this, 32);
            this.btns[i].setVisible(false);
            ++i;
        }
    }

    public void init(String[] text) {
        int size = text.length > 20 ? 20 : text.length;
        int i = 0;
        while (i < size) {
            this.btns[i].setText(text[i]);
            this.btns[i].setVisible(true);
            ++i;
        }
        while (i < size) {
            this.btns[i].setVisible(false);
            ++i;
        }
    }

    public void setValue(int value) {
        System.out.println(String.valueOf(value) + " " + this.btns.length);
        int i = 0;
        while (i < this.btns.length) {
            if ((value >> i & 1) == 1) {
                this.btns[i].setSelection(true);
            } else {
                this.btns[i].setSelection(false);
            }
            ++i;
        }
    }

    public int getValue() {
        int value = 0;
        int i = 0;
        while (i < this.btns.length) {
            value += (this.btns[i].getSelection() ? 1 : 0) << i;
            ++i;
        }
        return value;
    }

    public void checkSubclass() {
    }
}

