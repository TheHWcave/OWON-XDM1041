/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.DirectoryDialog
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.patch.direct.util;

import com.owon.uppersoft.patch.direct.util.ZipEncrypt_Org;
import java.io.File;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class EncryptFrm_Org {
    private Text destdir;
    private Button btndestdir;
    private Button btndescrypt;
    private Button btnkey;
    private Button btnfile;
    private Button btnencrypt;
    private Button btnsrcdir;
    private static Text key;
    private static Text file;
    private static Text srcdir;
    protected Shell shell;

    public static void main(String[] args) {
        try {
            EncryptFrm_Org window = new EncryptFrm_Org();
            window.open();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open() {
        Display display = Display.getDefault();
        this.createContents();
        this.shell.open();
        this.shell.layout();
        while (!this.shell.isDisposed()) {
            if (display.readAndDispatch()) continue;
            display.sleep();
        }
    }

    protected void createContents() {
        this.shell = new Shell();
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        this.shell.setLayout((Layout)gridLayout);
        this.shell.setSize(500, 218);
        this.btnsrcdir = new Button((Composite)this.shell, 0);
        GridData gd_btnsrcdir = new GridData(4, 0x1000000, false, false);
        this.btnsrcdir.setLayoutData((Object)gd_btnsrcdir);
        this.btnsrcdir.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                EncryptFrm_Org.this.browseDir(EncryptFrm_Org.this.shell, srcdir);
            }
        });
        this.btnsrcdir.setText("srcdir");
        srcdir = new Text((Composite)this.shell, 2048);
        GridData gd_srcdir = new GridData(4, 0x1000000, true, false);
        srcdir.setLayoutData((Object)gd_srcdir);
        new Label((Composite)this.shell, 0);
        this.btnencrypt = new Button((Composite)this.shell, 0);
        GridData gd_btnencrypt = new GridData(4, 0x1000000, true, false);
        this.btnencrypt.setLayoutData((Object)gd_btnencrypt);
        this.btnencrypt.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                try {
                    File indir = new File(srcdir.getText());
                    String outdir = indir + "_out";
                    File outfile = new File(outdir, "encrypt.out");
                    File outkey = new File(outdir, "public.key");
                    new ZipEncrypt_Org().encryptZip(indir, outfile, outkey);
                    file.setText(outfile.getPath());
                    key.setText(outkey.getPath());
                    EncryptFrm_Org.this.destdir.setText(outdir);
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        this.btnencrypt.setText("Encrypt");
        this.btnfile = new Button((Composite)this.shell, 0);
        GridData gd_btnfile = new GridData(4, 0x1000000, false, false);
        this.btnfile.setLayoutData((Object)gd_btnfile);
        this.btnfile.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                EncryptFrm_Org.this.browseFile(EncryptFrm_Org.this.shell, file);
            }
        });
        this.btnfile.setText("file");
        file = new Text((Composite)this.shell, 2048);
        GridData gd_file = new GridData(4, 0x1000000, true, false);
        file.setLayoutData((Object)gd_file);
        this.btnkey = new Button((Composite)this.shell, 0);
        GridData gd_btnkey = new GridData(4, 0x1000000, false, false);
        this.btnkey.setLayoutData((Object)gd_btnkey);
        this.btnkey.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                EncryptFrm_Org.this.browseFile(EncryptFrm_Org.this.shell, key);
            }
        });
        this.btnkey.setText("key");
        key = new Text((Composite)this.shell, 2048);
        GridData gd_key = new GridData(4, 0x1000000, true, false);
        key.setLayoutData((Object)gd_key);
        this.btndestdir = new Button((Composite)this.shell, 0);
        this.btndestdir.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                EncryptFrm_Org.this.browseDir(EncryptFrm_Org.this.shell, EncryptFrm_Org.this.destdir);
            }
        });
        GridData gd_btndestdir = new GridData(4, 0x1000000, false, false);
        this.btndestdir.setLayoutData((Object)gd_btndestdir);
        this.btndestdir.setText("destdir");
        this.destdir = new Text((Composite)this.shell, 2048);
        GridData gd_destdir = new GridData(4, 0x1000000, true, false);
        this.destdir.setLayoutData((Object)gd_destdir);
        new Label((Composite)this.shell, 0);
        this.btndescrypt = new Button((Composite)this.shell, 0);
        GridData gd_btndescrypt = new GridData(4, 0x1000000, true, false);
        this.btndescrypt.setLayoutData((Object)gd_btndescrypt);
        this.btndescrypt.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                try {
                    File infile = new File(file.getText());
                    File inkey = new File(key.getText());
                    File outdir = new File(EncryptFrm_Org.this.destdir.getText());
                    new ZipEncrypt_Org().decryptUnzip(infile, outdir, inkey);
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        this.btndescrypt.setText("Descrypt");
    }

    protected void browseDir(Shell shell, Text txt) {
        DirectoryDialog fd = new DirectoryDialog(shell);
        String name = fd.open();
        if (name == null) {
            return;
        }
        txt.setText(name);
    }

    protected void browseFile(Shell shell, Text txt) {
        FileDialog fd = new FileDialog(shell);
        String name = fd.open();
        if (name == null) {
            return;
        }
        txt.setText(name);
    }
}

