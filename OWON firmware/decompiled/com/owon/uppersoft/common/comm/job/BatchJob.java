/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job;

import com.owon.uppersoft.common.comm.job.IBatchJob;
import com.owon.uppersoft.common.comm.job.MSOTYPE;
import com.owon.uppersoft.common.comm.job.OSCTYPE;
import com.owon.uppersoft.common.comm.job.model.FileDataFile;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class BatchJob
implements IBatchJob {
    public static BatchJob msobj;
    public static BatchJob oscbj;
    private String typeName;
    private List<FileDataFile> list = new LinkedList<FileDataFile>();

    static {
        OSCTYPE[] oscs;
        MSOTYPE[] las;
        msobj = new BatchJob();
        MSOTYPE[] mSOTYPEArray = las = MSOTYPE.values();
        int n = las.length;
        int n2 = 0;
        while (n2 < n) {
            MSOTYPE la = mSOTYPEArray[n2];
            msobj.addDataFile(new FileDataFile(la.name(), la.value(), "", 16384));
            ++n2;
        }
        msobj.setTypeName(MsgCenter.getString("BatchJob.la"));
        oscbj = new BatchJob();
        OSCTYPE[] oSCTYPEArray = oscs = OSCTYPE.values();
        int n3 = oscs.length;
        n = 0;
        while (n < n3) {
            OSCTYPE osc = oSCTYPEArray[n];
            oscbj.addDataFile(new FileDataFile(osc.name(), osc.value(), "", 16384));
            ++n;
        }
        oscbj.setTypeName(MsgCenter.getString("BatchJob.osc"));
    }

    public void addDataFile(FileDataFile df) {
        if (df != null) {
            this.list.add(df);
        }
    }

    @Override
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() {
        return this.typeName;
    }

    public List<FileDataFile> getList() {
        return this.list;
    }

    public void clearStatus() {
        for (FileDataFile df : this.list) {
            df.setStatus(0);
        }
    }

    public void clearAll() {
        for (FileDataFile df : this.list) {
            df.setStatus(0);
            df.setSelected(false);
            df.setPath("");
        }
    }

    public Properties exportAsProperties() {
        Properties p = new Properties();
        for (FileDataFile df : this.list) {
            p.setProperty(df.getType(), df.getPath());
        }
        return p;
    }

    public void importFromProperties(Properties p) {
        for (FileDataFile df : this.list) {
            String fp = p.getProperty(df.getType(), "");
            df.setPath(fp);
            df.setSelected(true);
        }
    }

    public void importFromDir(File file) {
        this.clearAll();
        File[] files = file.listFiles();
        for (FileDataFile df : this.list) {
            String type = df.getType();
            int count = 0;
            File[] fileArray = files;
            int n = files.length;
            int n2 = 0;
            while (n2 < n) {
                block8: {
                    block7: {
                        File f = fileArray[n2];
                        String name = f.getName().toLowerCase();
                        int index = name.lastIndexOf(".");
                        if (index > 0) {
                            name = name.substring(0, index);
                        }
                        if (!name.contains(type)) break block7;
                        if (type.equalsIgnoreCase("os") && name.contains("bios")) break block8;
                        df.setPath(f.getAbsolutePath());
                        df.setSelected(true);
                        ++count;
                    }
                    if (count > 1) {
                        df.setStatus(3);
                    }
                }
                ++n2;
            }
        }
    }

    public void importFromFile(File f) {
        String name = f.getName().toLowerCase();
        int index = name.lastIndexOf(".");
        if (index > 0) {
            name = name.substring(0, index);
        }
        for (FileDataFile df : this.list) {
            String type = df.getType();
            if (!name.contains(type) || type.equalsIgnoreCase("os") && name.contains("bios")) continue;
            df.setPath(f.getAbsolutePath());
            df.setSelected(true);
            break;
        }
    }
}

