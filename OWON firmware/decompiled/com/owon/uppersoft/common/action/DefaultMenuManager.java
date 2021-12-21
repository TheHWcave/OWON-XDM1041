/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Menu
 *  org.eclipse.swt.widgets.MenuItem
 */
package com.owon.uppersoft.common.action;

import com.owon.uppersoft.common.action.AbstractItem;
import com.owon.uppersoft.common.action.DefaultAction;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class DefaultMenuManager
extends AbstractItem {
    private MenuItem mi;
    private Menu menu;

    public DefaultMenuManager(String id) {
        this(id, 0);
    }

    public DefaultMenuManager(String id, int type) {
        super(id, type);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        if (this.mi != null) {
            this.mi.setText(text);
        }
    }

    public void addSeparator() {
        if (this.menu != null) {
            new MenuItem(this.menu, 2);
        }
    }

    public MenuItem add(DefaultAction da) {
        MenuItem mm = null;
        if (this.menu != null) {
            mm = da.fill(this.menu);
        }
        return mm;
    }

    public Menu fill(Menu m) {
        this.mi = new MenuItem(m, 64);
        this.menu = new Menu(this.mi);
        this.mi.setMenu(this.menu);
        return this.menu;
    }
}

