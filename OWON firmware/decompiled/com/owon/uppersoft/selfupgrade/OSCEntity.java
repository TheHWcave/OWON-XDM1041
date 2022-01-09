/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.selfupgrade;

import com.owon.uppersoft.selfupgrade.Entity;
import com.owon.uppersoft.selfupgrade.Line;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OSCEntity
extends Entity {
    private List<Line> lines = new ArrayList<Line>();
    private String[] txtKey = new String[]{"@1Module_SEL_VGA", "@1OEMLogo", "@1XYMode_tbase", "$NetSwitch", "$StandardSignalSwitch"};

    public OSCEntity(ByteBuffer bb) {
        super(bb);
    }

    public OSCEntity(byte[] by) {
        super(by);
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
        this.addOther();
        Line l = this.getLine("@0series");
        if (l != null) {
            this.sn = l.getValue();
            String se = "   " + this.sn;
            this.getLine("@0series").setValue(se);
            l = this.getLine("@0seriesEn");
            if (l != null) {
                l.setValue(se);
            }
        } else {
            this.sn = this.getLine("@0serial").getValue();
        }
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

    private void addOther() {
        int i = 0;
        while (i < this.txtKey.length) {
            if (this.getLine(this.txtKey[i]) == null) {
                this.lines.add(new Line(this.txtKey[i], "0"));
            }
            ++i;
        }
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
        this.getLine("@0versionEn").setValue(newVer);
        this.updateContent();
    }

    @Override
    public void udateSer(String newSer) {
        if (newSer.contains("V")) {
            newSer = newSer.replace("V", "");
        }
        this.getLine("@0series").setValue(newSer);
        if (this.getLine("@0seriesEn") != null) {
            this.getLine("@0seriesEn").setValue(newSer);
        }
        this.getLine("$trigmode control_send").setValue("0");
    }

    @Override
    public void udatelanguageSw(String SW, String DiapSW, Boolean Sw) {
        String value;
        String string = value = Sw != false ? "1" : "0";
        if (this.getLine(SW) != null && this.getLine(DiapSW) != null) {
            this.getLine(SW).setValue(value);
            this.getLine(DiapSW).setValue(value);
        }
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

    public void printlines(FileOutputStream out) {
        DataOutputStream dout = new DataOutputStream(out);
        String ct = new String(this.content);
        String[] sp = ct.split("\n");
        int i = 0;
        while (i < sp.length) {
            try {
                dout.writeBytes(sp[i]);
                dout.writeBytes("\r\n");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            ++i;
        }
    }

    public byte[] getcontent() {
        return this.content;
    }

    public static void main_hide(String[] arg) {
    }

    @Override
    protected boolean supportOSReboot() {
        return true;
    }
}

