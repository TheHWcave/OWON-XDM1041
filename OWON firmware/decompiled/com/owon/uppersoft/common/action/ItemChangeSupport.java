/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.action;

import com.owon.uppersoft.common.action.EventType;
import com.owon.uppersoft.common.action.ItemListener;
import java.util.LinkedList;
import java.util.List;

public class ItemChangeSupport {
    private List<ItemListener> ls = new LinkedList<ItemListener>();

    public void addItemListener(ItemListener il) {
        this.ls.add(il);
    }

    public void removeItemListener(ItemListener il) {
        this.ls.remove(il);
    }

    public void clearItemListeners() {
        this.ls.clear();
    }

    public void fireItemListener(EventType type, Object value) {
        if (value == null) {
            return;
        }
        for (ItemListener il : this.ls) {
            il.handle(type, value);
        }
    }
}

