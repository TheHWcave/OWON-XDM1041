/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.DirectoryDialog
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.common.comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

public class TranslateTool {
    protected void translate(File src, File dest, String v) {
        try {
            String oln;
            BufferedReader br = new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(src), "GBK"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(dest), "GBK"));
            while ((oln = br.readLine()) != null) {
                String ln = oln.trim();
                if (ln.startsWith("@0version") || ln.startsWith("@0versionEn")) {
                    String nln;
                    int beg = ln.indexOf("V") + 1;
                    int end = ln.indexOf(";");
                    ln = ln.substring(beg, end);
                    oln = nln = oln.replace(ln, v);
                }
                bw.write(oln);
                bw.write("\r\n");
            }
            br.close();
            bw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TranslateTool().browseBatch();
    }

    public void browseBatch() {
        Shell s = new Shell();
        DirectoryDialog dd = new DirectoryDialog(s);
        String src = dd.open();
        if (src == null) {
            return;
        }
        String dest = dd.open();
        if (dest == null) {
            return;
        }
        this.batchTranslate(new File(src), new File(dest), "6.4");
    }

    public void batchTranslate(File srcdir, File destdir, String v) {
        File[] fs;
        File[] fileArray = fs = srcdir.listFiles();
        int n = fs.length;
        int n2 = 0;
        while (n2 < n) {
            File subdir = fileArray[n2];
            if (!subdir.isFile()) {
                File[] txs;
                File subdestdir = new File(destdir, subdir.getName());
                subdestdir.mkdirs();
                File[] fileArray2 = txs = subdir.listFiles();
                int n3 = txs.length;
                int n4 = 0;
                while (n4 < n3) {
                    String n5;
                    String ext;
                    File tx = fileArray2[n4];
                    if (!tx.isDirectory() && (ext = (n5 = tx.getName()).substring(n5.lastIndexOf(".") + 1, n5.length())).equalsIgnoreCase("txt")) {
                        this.translate(tx, new File(subdestdir, tx.getName()), v);
                    }
                    ++n4;
                }
            }
            ++n2;
        }
    }
}

