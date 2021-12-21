/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.selfupgrade;

import com.owon.uppersoft.selfupgrade.Entity;
import com.owon.uppersoft.selfupgrade.Line;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VDSEntity
extends Entity {
    private List<Line> lines = new ArrayList<Line>();

    public VDSEntity(ByteBuffer bb) {
        super(bb);
    }

    @Override
    public void init() {
        String s = new String(this.content);
        String[] lines = s.split(";");
        int i = 0;
        while (i < lines.length) {
            this.getLinePro(lines[i].trim());
            ++i;
        }
        this.sn = this.getLine("@0series").getValue();
        this.ver = this.getLine("@0version").getValue();
    }

    private void getLinePro(String s) {
        int index1 = s.indexOf("=");
        int index2 = s.indexOf("$");
        if (index2 < 0) {
            index2 = s.indexOf("@");
        }
        if (index1 < 0 || index2 < 0) {
            return;
        }
        String key = s.substring(index2, index1).trim();
        String value = s.substring(index1 + 1).trim();
        Line line = new Line(key, value);
        this.lines.add(line);
    }

    private Line getLine(String key) {
        for (Line line : this.lines) {
            if (!line.getKey().equalsIgnoreCase(key)) continue;
            return line;
        }
        return null;
    }

    @Override
    public void updateVer(String newVer) {
        this.getLine("@0version").setValue(newVer);
        this.updateContent();
    }

    private void updateContent() {
        Iterator<Line> it = this.lines.iterator();
        StringBuilder s = new StringBuilder();
        while (it.hasNext()) {
            Line line = it.next();
            s.append(line.toString());
        }
        this.content = s.append("END").toString().getBytes();
    }

    public static void main_hide(String[] arg) {
    }

    @Override
    protected boolean supportOSReboot() {
        return true;
    }
}

