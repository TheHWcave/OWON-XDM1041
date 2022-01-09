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
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class TdsComposite {
    private Composite com;
    private int index;
    private Text[] txt = new Text[4];
    private Text txtVer;
    private Text txtSn;
    private Text txtType;
    private Composite composite;
    private Label lbVer;
    private Label lbType;
    private Label lbSn;

    public Composite getComposite() {
        return this.com;
    }

    public TdsComposite(Composite parent) {
        this.com = new Composite(parent, 0);
        this.com.setLayout((Layout)new GridLayout());
        Group tdsGroup = new Group(this.com, 0);
        GridData gd_tdsGroup = new GridData(4, 0x1000000, true, false);
        gd_tdsGroup.heightHint = 108;
        tdsGroup.setLayoutData((Object)gd_tdsGroup);
        GridLayout gl_tdsGroup = new GridLayout();
        gl_tdsGroup.marginWidth = 10;
        gl_tdsGroup.marginHeight = 10;
        gl_tdsGroup.numColumns = 4;
        tdsGroup.setLayout((Layout)gl_tdsGroup);
        Button btnRead = new Button((Composite)tdsGroup, 16);
        btnRead.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                TdsComposite.this.txt[0].setEnabled(true);
                TdsComposite.this.txt[1].setEnabled(false);
                TdsComposite.this.composite.setEnabled(false);
                TdsComposite.this.index = 0;
            }
        });
        btnRead.setText(MsgCenter.getString("TC.read"));
        Label label_1 = new Label((Composite)tdsGroup, 0);
        label_1.setText(MsgCenter.getString("TC.path"));
        this.txt[0] = new Text((Composite)tdsGroup, 2048);
        this.txt[0].setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        Button btnBrowse = new Button((Composite)tdsGroup, 0);
        btnBrowse.setText(MsgCenter.getString("TC.browse"));
        Button btnWrite = new Button((Composite)tdsGroup, 16);
        btnWrite.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                TdsComposite.this.txt[0].setEnabled(false);
                TdsComposite.this.txt[1].setEnabled(true);
                TdsComposite.this.composite.setEnabled(false);
                TdsComposite.this.index = 1;
            }
        });
        btnWrite.setText(MsgCenter.getString("TC.write"));
        Label label = new Label((Composite)tdsGroup, 0);
        label.setText(MsgCenter.getString("TC.path"));
        this.txt[1] = new Text((Composite)tdsGroup, 2048);
        this.txt[1].setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Button button_2 = new Button((Composite)tdsGroup, 0);
        button_2.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                FileDialog dd = new FileDialog(TdsComposite.this.com.getShell());
                String path = TdsComposite.this.txt[1].getText();
                if (!path.equals("")) {
                    dd.setFilterPath(path);
                }
                if ((path = dd.open()) == null) {
                    return;
                }
                TdsComposite.this.txt[1].setText(path);
            }
        });
        button_2.setText(MsgCenter.getString("TC.browse"));
        Button btnWriteVer = new Button((Composite)tdsGroup, 16);
        btnWriteVer.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                TdsComposite.this.txt[0].setEnabled(false);
                TdsComposite.this.txt[1].setEnabled(false);
                TdsComposite.this.composite.setEnabled(true);
                TdsComposite.this.index = 2;
            }
        });
        btnWriteVer.setText(MsgCenter.getString("TC.writever"));
        this.composite = new Composite((Composite)tdsGroup, 0);
        this.composite.setLayout((Layout)new GridLayout(6, false));
        GridData gd_composite = new GridData(4, 0x1000000, false, false, 3, 1);
        gd_composite.heightHint = 25;
        this.composite.setLayoutData((Object)gd_composite);
        this.lbType = new Label(this.composite, 0);
        this.lbType.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        this.lbType.setText(MsgCenter.getString("TC.type"));
        this.txtType = new Text(this.composite, 2048);
        this.txtType.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.lbVer = new Label(this.composite, 0);
        this.lbVer.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        this.lbVer.setText(MsgCenter.getString("TC.ver"));
        this.txtVer = new Text(this.composite, 2048);
        this.txtVer.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.lbSn = new Label(this.composite, 0);
        this.lbSn.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        this.lbSn.setText(MsgCenter.getString("TC.sn"));
        this.txtSn = new Text(this.composite, 2048);
        this.txtSn.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        btnBrowse.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dd = new DirectoryDialog(TdsComposite.this.com.getShell());
                String path = TdsComposite.this.txt[0].getText();
                if (!path.equals("")) {
                    dd.setFilterPath(path);
                }
                if ((path = dd.open()) == null) {
                    return;
                }
                TdsComposite.this.txt[0].setText(path);
            }
        });
        this.txt[0].setEnabled(false);
        this.txt[1].setEnabled(false);
        this.composite.setEnabled(true);
        this.index = 2;
        btnWriteVer.setSelection(true);
    }

    public String getFilePath() {
        return this.txt[0].getText();
    }

    public int getIndex() {
        return this.index;
    }

    public String getText() {
        if (this.index >= 2) {
            StringBuilder sb = new StringBuilder();
            String s = this.txtType.getText();
            if (s.equalsIgnoreCase("")) {
                return s;
            }
            sb.append("TYPE=" + s + ";");
            s = this.txtVer.getText();
            if (s.equalsIgnoreCase("")) {
                return s;
            }
            sb.append("VERSION=" + s + ";");
            s = this.txtSn.getText();
            if (s.equalsIgnoreCase("")) {
                return s;
            }
            sb.append("SN=" + s + ";");
            return sb.toString();
        }
        return this.txt[this.index].getText();
    }
}

