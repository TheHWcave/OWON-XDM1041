/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.Listener
 *  org.eclipse.swt.widgets.Menu
 *  org.eclipse.swt.widgets.MenuItem
 */
package com.owon.uppersoft.common.action;

import com.owon.uppersoft.common.action.ItemVisitor;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class ItemGroup<T>
implements Listener {
    public static final int PUSH = 8;
    public static final int RADIO = 16;
    private List<T> tlist;
    private MenuItem mi;
    private Menu m;
    private ItemVisitor<T> iv;
    private int style;

    public ItemGroup(ItemVisitor<T> iv, int style) {
        this.iv = iv;
        this.style = this.validStyle(style);
        this.tlist = new LinkedList<T>();
    }

    protected int validStyle(int s) {
        if (s != 8 && s != 16) {
            return 8;
        }
        return s;
    }

    public void add(T t) {
        this.tlist.add(t);
    }

    public MenuItem fill(Menu parent) {
        this.mi = new MenuItem(parent, 64);
        this.m = new Menu(this.mi);
        this.mi.setMenu(this.m);
        for (T t : this.tlist) {
            MenuItem it = new MenuItem(this.m, this.style);
            it.setData(t);
            it.addListener(13, (Listener)this);
            it.setText(this.iv.getText(t));
        }
        return this.mi;
    }

    public void select(T t) {
        if ((this.style & 0x10) == 0) {
            return;
        }
        MenuItem[] menuItemArray = this.m.getItems();
        int n = menuItemArray.length;
        int n2 = 0;
        while (n2 < n) {
            MenuItem it = menuItemArray[n2];
            Object tt = it.getData();
            if (t.equals(tt)) {
                it.setSelection(true);
            } else {
                it.setSelection(false);
            }
            ++n2;
        }
    }

    public void handleEvent(Event event) {
        MenuItem mi = (MenuItem)event.widget;
        if (!mi.getSelection()) {
            return;
        }
        Object t = mi.getData();
        this.iv.invoke(t);
    }
}

