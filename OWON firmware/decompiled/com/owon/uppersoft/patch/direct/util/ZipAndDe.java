/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.patch.direct.util;

import com.owon.uppersoft.common.comm.job.MSOTYPE;
import com.owon.uppersoft.common.comm.job.OSCTYPE;
import com.owon.uppersoft.common.comm.job.model.DataItem;
import com.owon.uppersoft.common.comm.job.model.XDataItem;
import com.owon.uppersoft.common.comm.job.model.XDataItemInputProvider;
import com.owon.uppersoft.common.comm.job.model.ZipDataItem;
import com.owon.uppersoft.patch.direct.util.ZipEncrypt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.Key;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.crypto.Cipher;
import org.eclipse.swt.widgets.Text;

public class ZipAndDe {
    private ZipFile bundleszf;
    private ZipFile txszf;

    public void abstractAndDecrypt(List<DataItem> dfs, File destdir) {
        destdir.mkdirs();
        try {
            for (DataItem di : dfs) {
                String n = di.getType();
                ByteBuffer bb = di.initbb();
                if (bb == null) {
                    System.err.println(di + " return null by initbb");
                    break;
                }
                FileChannel fc = new FileOutputStream(new File(destdir, n)).getChannel();
                while (di.fillbb(bb)) {
                    if (!bb.hasRemaining()) continue;
                    fc.write(bb);
                }
                fc.close();
                di.termbb();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.closeZip();
    }

    public List<DataItem> loadZipFile(File file, File keyfile, String psn, String type) {
        LinkedList<DataItem> dfs = new LinkedList<DataItem>();
        if (!file.exists() || file.isDirectory()) {
            return dfs;
        }
        try {
            this.bundleszf = new ZipFile(file);
            Enumeration<? extends ZipEntry> zfe = this.bundleszf.entries();
            ZipEntry ze = this.bundleszf.getEntry("readme.txt");
            if (ze == null) {
                return dfs;
            }
            Properties p = new Properties();
            p.load(this.bundleszf.getInputStream(ze));
            ZipEncrypt en = new ZipEncrypt();
            Key k = en.getKey(keyfile);
            Cipher dc = en.createCipher(k, 2);
            while (zfe.hasMoreElements()) {
                XDataItem di;
                ze = zfe.nextElement();
                String name = ze.getName();
                if (name.equals("readme.txt")) continue;
                if (type.equalsIgnoreCase("mso")) {
                    MSOTYPE mt = null;
                    try {
                        mt = MSOTYPE.valueOf(name);
                    }
                    catch (RuntimeException e) {
                        e.printStackTrace();
                        continue;
                    }
                    if (mt == null) continue;
                    long len = Long.parseLong((String)p.get(name));
                    di = new XDataItem(mt.name(), mt.value(), len, new XDataItemInputProvider(this.bundleszf, ze), dc, 2064, 2064);
                    dfs.add(di);
                    continue;
                }
                OSCTYPE ot = null;
                try {
                    ot = OSCTYPE.valueOf(name);
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                    continue;
                }
                if (ot == null) continue;
                long len = Long.parseLong((String)p.get(name));
                di = new XDataItem(ot.name(), ot.value(), len, new XDataItemInputProvider(this.bundleszf, ze), dc, 2064, 2064);
                dfs.add(di);
            }
        }
        catch (ZipException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (!psn.equalsIgnoreCase("")) {
            this.addTX(dfs, psn, type);
        }
        for (DataItem di : dfs) {
            System.out.println(di);
        }
        return dfs;
    }

    public static String getPreAndLst2(String psn) {
        if (psn.length() != 7) {
            return null;
        }
        String pre = psn.substring(0, 4);
        String lst = psn.substring(4, 7);
        String n = String.valueOf(pre) + "/" + lst + ".txt";
        return n;
    }

    public static String getPreAndLst(String psn) {
        if (psn.length() > 8 || psn.length() < 7) {
            return null;
        }
        String pre = psn.substring(0, 4);
        String lst = psn.substring(4);
        String n = String.valueOf(pre) + "/" + lst + ".txt";
        return n;
    }

    public static String getPreAndLst1(String psn) {
        if (psn.length() != 8) {
            return null;
        }
        String pre = psn.substring(0, 4);
        String mid = psn.substring(4, 5);
        String lst = psn.substring(5, 8);
        String n = mid.equalsIgnoreCase("0") ? lst : String.valueOf(mid) + lst;
        n = String.valueOf(pre) + "/" + n + ".txt";
        return n;
    }

    public static boolean findMatchSN(Text txt, String psn) {
        String name = ZipAndDe.getPreAndLst(psn);
        if (name == null) {
            return false;
        }
        try {
            ZipFile zf = new ZipFile("txs.zip");
            boolean b = zf.getEntry(name) != null;
            zf.close();
            return b;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void addTX(List<DataItem> dfs, String psn, String type) {
        String name = ZipAndDe.getPreAndLst(psn);
        if (name == null) {
            return;
        }
        try {
            this.txszf = new ZipFile("txs.zip");
            ZipEntry ze = this.txszf.getEntry(name);
            if (ze == null) {
                return;
            }
            if (type.equalsIgnoreCase("osc")) {
                OSCTYPE ot = OSCTYPE.tx;
                ZipDataItem di = new ZipDataItem(ot.name(), ot.value(), new XDataItemInputProvider(this.txszf, ze), 16384);
                dfs.add(di);
            } else {
                MSOTYPE ot = MSOTYPE.tx;
                ZipDataItem di = new ZipDataItem(ot.name(), ot.value(), new XDataItemInputProvider(this.txszf, ze), 16384);
                dfs.add(di);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeZip() {
        try {
            if (this.bundleszf != null) {
                this.bundleszf.close();
                this.bundleszf = null;
            }
            if (this.txszf != null) {
                this.txszf.close();
                this.txszf = null;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

