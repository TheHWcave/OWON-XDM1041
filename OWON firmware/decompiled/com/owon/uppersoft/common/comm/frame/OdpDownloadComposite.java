/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.layout.RowData
 *  org.eclipse.swt.layout.RowLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.DirectoryDialog
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.comm.Propert;
import com.owon.uppersoft.common.comm.frame.MainFrm;
import com.owon.uppersoft.common.comm.frame.OdpDownloadByComm;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.io.File;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class OdpDownloadComposite {
    private Composite com;
    private Text txPath;
    private Text text;
    private Text text_1;
    private Button button;
    private Button button_2;
    private Button btnSave;
    private Button button_4;
    private MainFrm frm;
    private OdpDownloadByComm odbc;
    private Button button_6;

    public Composite getComposite() {
        return this.com;
    }

    public void setFrm(MainFrm frm) {
        this.frm = frm;
    }

    public OdpDownloadComposite(Composite parent) {
        this.com = new Composite(parent, 0);
        this.com.setLayout((Layout)new GridLayout());
        Group gpFile = new Group(this.com, 0);
        RowLayout rl_gpFile = new RowLayout(256);
        rl_gpFile.marginBottom = 0;
        rl_gpFile.marginTop = 0;
        gpFile.setLayout((Layout)rl_gpFile);
        gpFile.setLayoutData((Object)new GridData(4, 0x1000000, false, false, 1, 1));
        gpFile.setText("get TXT");
        gpFile.setVisible(false);
        Label lbPath = new Label((Composite)gpFile, 0);
        lbPath.setText(MsgCenter.getString("ODP.path"));
        this.txPath = new Text((Composite)gpFile, 2048);
        this.txPath.setLayoutData((Object)new RowData(277, -1));
        this.txPath.setText(Propert.getInstance().get("odptxtpath"));
        Button btnRead = new Button((Composite)gpFile, 0);
        btnRead.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dd = new DirectoryDialog(OdpDownloadComposite.this.com.getShell());
                String s = dd.open();
                if (s == null) {
                    return;
                }
                File f = new File(s);
                if (!f.exists()) {
                    return;
                }
                Propert.getInstance().set("odptxtpath", s);
                OdpDownloadComposite.this.txPath.setText(s);
            }
        });
        btnRead.setText(MsgCenter.getString("DF.browse"));
        this.btnSave = new Button((Composite)gpFile, 0);
        this.btnSave.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                OdpDownloadComposite.this.getTxt();
            }
        });
        this.btnSave.setText(MsgCenter.getString("ODP.read"));
        Group group = new Group(this.com, 0);
        group.setLayoutData((Object)new GridData(4, 0x1000000, false, false, 1, 1));
        group.setLayout((Layout)new GridLayout(3, false));
        this.button = new Button((Composite)group, 32);
        this.button.setText("OS");
        this.text = new Text((Composite)group, 2048);
        this.text.setLayoutData((Object)new GridData(4, 0x1000000, false, false, 1, 1));
        Button button_1 = new Button((Composite)group, 0);
        button_1.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                FileDialog fd = new FileDialog(OdpDownloadComposite.this.com.getShell());
                fd.setFilterExtensions(new String[]{"*.*", "*.bin"});
                String s = fd.open();
                if (s == null) {
                    return;
                }
                File f = new File(s);
                if (!f.exists()) {
                    return;
                }
                OdpDownloadComposite.this.text.setText(s);
            }
        });
        button_1.setText(MsgCenter.getString("DF.browse"));
        this.button_2 = new Button((Composite)group, 32);
        this.button_2.setText("TXT");
        this.text_1 = new Text((Composite)group, 2048);
        this.text_1.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Button button_3 = new Button((Composite)group, 0);
        button_3.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                FileDialog fd = new FileDialog(OdpDownloadComposite.this.com.getShell());
                fd.setFilterExtensions(new String[]{"*.*", "*.txt"});
                String s = fd.open();
                if (s == null) {
                    return;
                }
                File f = new File(s);
                if (!f.exists()) {
                    return;
                }
                OdpDownloadComposite.this.text_1.setText(s);
            }
        });
        button_3.setText(MsgCenter.getString("DF.browse"));
        this.button_4 = new Button((Composite)group, 0);
        this.button_4.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                OdpDownloadComposite.this.download();
            }
        });
        this.button_4.setText(MsgCenter.getString("ODP.start"));
        Button button_5 = new Button((Composite)group, 0);
        button_5.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                if (OdpDownloadComposite.this.odbc != null) {
                    OdpDownloadComposite.this.odbc.setStop();
                }
            }
        });
        button_5.setText(MsgCenter.getString("ODP.stop"));
        this.button_6 = new Button((Composite)group, 32);
        this.button_6.setSelection(true);
        this.button_6.setVisible(false);
    }

    private void getTxt() {
        this.odbc = new OdpDownloadByComm(this, this.frm.getTxtLog(), this.frm.getPort());
        this.odbc.getTxt(this.txPath.getText());
        this.btnSave.setEnabled(false);
    }

    private void download() {
        this.odbc = new OdpDownloadByComm(this, this.frm.getTxtLog(), this.frm.getPort());
        if (this.getOsSelection()) {
            this.odbc.addItems("OS", 5, this.getOsSelection(), this.getOsPath());
        }
        if (this.getTxtSelection()) {
            this.odbc.addItems("TXT", 2, this.getTxtSelection(), this.getTxtPath());
        }
        this.odbc.setWriteNewVer(this.button_6.getSelection());
        this.odbc.start();
        this.button_4.setEnabled(false);
    }

    public boolean getOsSelection() {
        return this.button.getSelection();
    }

    public String getOsPath() {
        return this.text.getText().trim();
    }

    public boolean getTxtSelection() {
        return this.button_2.getSelection();
    }

    public String getTxtPath() {
        return this.text_1.getText().trim();
    }

    public void enable() {
        Display.getDefault().asyncExec(new Runnable(){

            @Override
            public void run() {
                OdpDownloadComposite.this.button_4.setEnabled(true);
                OdpDownloadComposite.this.btnSave.setEnabled(true);
            }
        });
    }
}

