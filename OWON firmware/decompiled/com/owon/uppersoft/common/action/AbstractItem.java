/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.action;

import com.owon.uppersoft.common.aspect.Localizable2;
import java.util.ResourceBundle;

public class AbstractItem
implements Localizable2 {
    private String id;
    private int type;
    private String text;

    public AbstractItem(String id, int type) {
        this.setId(id);
        this.setType(type);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void localize(ResourceBundle bundle) {
        String id = this.getId();
        if (id == null || id.length() == 0) {
            return;
        }
        String text = bundle.getString("MF." + id);
        this.setText(text);
    }
}

