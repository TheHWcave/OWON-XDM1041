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
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.DirectoryDialog
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.patch.direct.util;

import com.owon.uppersoft.common.comm.job.MSOTYPE;
import com.owon.uppersoft.common.comm.job.OSCTYPE;
import com.owon.uppersoft.common.comm.job.model.DataItem;
import com.owon.uppersoft.common.comm.job.model.FileDataFileInputProvider;
import com.owon.uppersoft.common.comm.job.model.XDataItem;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.util.ZipAndDe;
import com.owon.uppersoft.patch.direct.util.ZipEncrypt;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.Key;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class EncryptFrm {
    private Text txtLog;
    private Composite mainCom;
    public static Map<String, String> cm = new HashMap<String, String>();
    private Button btndescrypt;
    private Button btnkey;
    private Button btndestfile;
    private Button btnencrypt;
    private Button btnsrcdir;
    private static Text txtkey;
    private static Text destfile;
    private static Text srcdir;
    protected Shell shell;
    private Button btnGenerateKey;
    private Combo combo;

    static {
        Properties p = new Properties();
        try {
            FileInputStream is = new FileInputStream("map.txt");
            p.load(is);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Set<Map.Entry<Object, Object>> set = p.entrySet();
        for (Map.Entry<Object, Object> me : set) {
            String k = me.getValue().toString();
            String v = me.getKey().toString();
            cm.put(k, v);
            System.out.println(String.valueOf(k) + " = " + v);
        }
    }

    public static void main(String[] args) {
        try {
            EncryptFrm window = new EncryptFrm();
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
        this.shell.setLayout((Layout)new GridLayout());
        this.shell.setSize(438, 233);
        this.mainCom = new Composite((Composite)this.shell, 0);
        this.mainCom.setLayoutData((Object)new GridData(4, 0x1000000, false, false));
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        this.mainCom.setLayout((Layout)gridLayout);
        this.btnGenerateKey = new Button(this.mainCom, 0);
        this.btnGenerateKey.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                try {
                    ZipEncrypt ze = new ZipEncrypt();
                    ze.createKey(new File(txtkey.getText()));
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        this.btnGenerateKey.setLayoutData((Object)new GridData(4, 0x1000000, false, false));
        txtkey = new Text(this.mainCom, 2048);
        txtkey.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        this.btnkey = new Button(this.mainCom, 0);
        this.btnkey.setLayoutData((Object)new GridData(4, 0x1000000, false, false));
        this.btnkey.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                EncryptFrm.this.browseFile(EncryptFrm.this.shell, txtkey);
            }
        });
        this.btnsrcdir = new Button(this.mainCom, 0);
        this.btnsrcdir.setLayoutData((Object)new GridData(4, 0x1000000, false, false));
        this.btnsrcdir.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                EncryptFrm.this.browseDir(EncryptFrm.this.shell, srcdir);
            }
        });
        srcdir = new Text(this.mainCom, 2048);
        srcdir.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 2, 1));
        this.btndestfile = new Button(this.mainCom, 0);
        this.btndestfile.setLayoutData((Object)new GridData(4, 0x1000000, false, false));
        this.btndestfile.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                EncryptFrm.this.browseFile(EncryptFrm.this.shell, destfile);
            }
        });
        destfile = new Text(this.mainCom, 2048);
        destfile.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 2, 1));
        this.btnencrypt = new Button(this.mainCom, 0);
        this.btnencrypt.setLayoutData((Object)new GridData(4, 0x1000000, false, false));
        this.btnencrypt.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                EncryptFrm.this.doEncrypt();
            }
        });
        this.btndescrypt = new Button(this.mainCom, 0);
        this.btndescrypt.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        this.btndescrypt.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                EncryptFrm.this.doDesencrypt();
            }
        });
        this.combo = new Combo(this.mainCom, 8);
        this.combo.setItems(new String[]{"OSC", "MSO"});
        this.combo.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.combo.select(0);
        destfile.setText("E:\\release\\Patch\\bundles");
        srcdir.setText("i:\\src");
        txtkey.setText("E:\\release\\Patch\\public.key");
        this.txtLog = new Text((Composite)this.shell, 2568);
        this.txtLog.setLayoutData((Object)new GridData(4, 4, true, true));
        this.localize();
    }

    public void localize() {
        ResourceBundle bundel = MsgCenter.getBundle();
        this.btndescrypt.setText(bundel.getString("DE.DE"));
        this.btnencrypt.setText(bundel.getString("DE.EN"));
        this.btnGenerateKey.setText(bundel.getString("DE.GenKey"));
        this.btnkey.setText(bundel.getString("DE.SelKey"));
        this.btnsrcdir.setText(bundel.getString("DE.SelSrc"));
        this.btndestfile.setText(bundel.getString("DE.SelDest"));
    }

    public void doEncrypt() {
        try {
            File src = new File(srcdir.getText());
            File dest = new File(destfile.getText());
            File key = new File(txtkey.getText());
            ZipEncrypt ze = new ZipEncrypt();
            Key k = ze.getKey(key);
            Cipher ec = ze.createCipher(k, 1);
            Properties p = new Properties();
            File[] fs = src.listFiles();
            LinkedList<XDataItem> dfs = new LinkedList<XDataItem>();
            File[] fileArray = fs;
            int n = fs.length;
            int n2 = 0;
            while (n2 < n) {
                File f = fileArray[n2];
                String name = f.getName();
                String n3 = cm.get(name);
                if (n3 != null) {
                    XDataItem di;
                    long len;
                    Enum ot;
                    if (this.combo.getSelectionIndex() == 0) {
                        ot = OSCTYPE.valueOf(n3);
                        if (ot != null) {
                            len = f.length();
                            this.txtLog.append(String.valueOf(n3) + "=" + name + "\n");
                            p.put(n3, String.valueOf(len));
                            di = new XDataItem(n3, ((OSCTYPE)ot).value(), len, new FileDataFileInputProvider(f), ec, 2048, 2064);
                            dfs.add(di);
                        }
                    } else {
                        ot = MSOTYPE.valueOf(n3);
                        if (ot != null) {
                            len = f.length();
                            this.txtLog.append(String.valueOf(n3) + "=" + name + "\n");
                            p.put(n3, String.valueOf(len));
                            di = new XDataItem(n3, ((MSOTYPE)ot).value(), len, new FileDataFileInputProvider(f), ec, 2048, 2064);
                            dfs.add(di);
                        }
                    }
                }
                ++n2;
            }
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dest));
            ZipEntry zen = new ZipEntry("readme.txt");
            zos.putNextEntry(zen);
            p.store(zos, "");
            for (DataItem dataItem : dfs) {
                System.out.println(dataItem);
                zos.putNextEntry(new ZipEntry(dataItem.getType()));
                ByteBuffer bb = dataItem.initbb();
                if (bb == null) {
                    System.err.println("initbb");
                    break;
                }
                while (dataItem.fillbb(bb)) {
                    if (!bb.hasRemaining()) continue;
                    zos.write(bb.array(), 0, bb.limit());
                }
                dataItem.termbb();
            }
            zos.close();
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void doDesencrypt() {
        File dest = new File(srcdir.getText());
        File src = new File(destfile.getText());
        File key = new File(txtkey.getText());
        ZipAndDe zad = new ZipAndDe();
        List<DataItem> dfs = zad.loadZipFile(src, key, "", "");
        zad.abstractAndDecrypt(dfs, dest);
    }

    public File checkPath(File file) {
        File parent;
        if (!file.exists() && (parent = file.getParentFile()) != null) {
            parent.mkdirs();
        }
        return file;
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

    public Composite getMainCom() {
        return this.mainCom;
    }
}

