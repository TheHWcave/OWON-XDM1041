/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.dialogs.MessageDialog
 *  org.eclipse.swt.events.ModifyEvent
 *  org.eclipse.swt.events.ModifyListener
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.DirectoryDialog
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame.vds;

import com.owon.uppersoft.common.comm.frame.txtedit.IShowMessage;
import com.owon.uppersoft.common.comm.frame.vds.IRefreshCom;
import com.owon.uppersoft.common.comm.frame.vds.VdsPresenter;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.io.FileInputStream;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Xds40Composite
implements IShowMessage,
IRefreshCom {
    private Composite com;
    private Combo combo;
    private Text readTxt;
    private Text writeTxt;
    private VdsPresenter presenter;
    private Text text;

    public Composite getComposite() {
        return this.com;
    }

    public Xds40Composite(final Composite parent) {
        this.com = new Composite(parent, 0);
        this.presenter = new VdsPresenter(this, this);
        this.com.setLayout((Layout)new GridLayout(1, false));
        Group group = new Group(this.com, 0);
        group.setLayout((Layout)new GridLayout(3, false));
        group.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Label label_2 = new Label((Composite)group, 0);
        label_2.setText(MsgCenter.getString("vds.port"));
        this.combo = new Combo((Composite)group, 8);
        this.combo.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.combo.addModifyListener(new ModifyListener(){

            public void modifyText(ModifyEvent arg0) {
                Xds40Composite.this.presenter.setCom(Xds40Composite.this.combo.getText());
            }
        });
        Button button = new Button((Composite)group, 0);
        button.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                Xds40Composite.this.presenter.refreshCom();
            }
        });
        button.setText(MsgCenter.getString("vds.refresh"));
        Group readGroup = new Group(this.com, 0);
        readGroup.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        GridLayout gl_readGroup = new GridLayout();
        gl_readGroup.marginWidth = 10;
        gl_readGroup.marginHeight = 10;
        gl_readGroup.numColumns = 5;
        readGroup.setLayout((Layout)gl_readGroup);
        Label label_3 = new Label((Composite)readGroup, 0);
        label_3.setText("Txt");
        Label label_1 = new Label((Composite)readGroup, 0);
        label_1.setText(MsgCenter.getString("TC.path"));
        this.readTxt = new Text((Composite)readGroup, 2056);
        this.readTxt.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        Button btnReadBrowse = new Button((Composite)readGroup, 0);
        btnReadBrowse.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dd = new DirectoryDialog(Xds40Composite.this.com.getShell());
                String path = Xds40Composite.this.readTxt.getText();
                if (!path.equals("")) {
                    dd.setFilterPath(path);
                }
                if ((path = dd.open()) == null) {
                    return;
                }
                Xds40Composite.this.readTxt.setText(path);
            }
        });
        btnReadBrowse.setText(MsgCenter.getString("TC.browse"));
        Button button_1 = new Button((Composite)readGroup, 0);
        button_1.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                if (Xds40Composite.this.combo.getSelectionIndex() < 0) {
                    Xds40Composite.this.showInfo(MsgCenter.getString("vds.nousb"));
                    return;
                }
                if (Xds40Composite.this.readTxt.getText().length() == 0) {
                    Xds40Composite.this.showInfo(MsgCenter.getString("vds.nullpath"));
                    return;
                }
                Xds40Composite.this.presenter.readXdsTxt(":UPLoad:TXT?", Xds40Composite.this.readTxt.getText().trim());
            }
        });
        button_1.setText(MsgCenter.getString("vds.read"));
        Group writeGroup = new Group(this.com, 0);
        writeGroup.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        GridLayout gl_writeGroup = new GridLayout();
        gl_writeGroup.marginWidth = 10;
        gl_writeGroup.marginHeight = 10;
        gl_writeGroup.numColumns = 5;
        writeGroup.setLayout((Layout)gl_writeGroup);
        Label lbWriteTxt = new Label((Composite)writeGroup, 0);
        lbWriteTxt.setText("Txt");
        Label lbWritePath = new Label((Composite)writeGroup, 0);
        lbWritePath.setText(MsgCenter.getString("TC.path"));
        this.writeTxt = new Text((Composite)writeGroup, 2056);
        this.writeTxt.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        Button btnWriteBrowse = new Button((Composite)writeGroup, 0);
        btnWriteBrowse.setText(MsgCenter.getString("TC.browse"));
        btnWriteBrowse.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                FileDialog dd = new FileDialog(parent.getShell());
                String path = Xds40Composite.this.writeTxt.getText();
                if (!path.equals("")) {
                    dd.setFilterPath(path);
                }
                if ((path = dd.open()) == null) {
                    return;
                }
                Xds40Composite.this.writeTxt.setText(path);
            }
        });
        Button btnWrite = new Button((Composite)writeGroup, 0);
        btnWrite.setText(MsgCenter.getString("vds.write"));
        Group scriptGroup = new Group(this.com, 0);
        scriptGroup.setLayout((Layout)new GridLayout(3, false));
        scriptGroup.setLayoutData((Object)new GridData(4, 0x1000000, false, false, 1, 1));
        scriptGroup.setVisible(false);
        Label lbSn = new Label((Composite)scriptGroup, 0);
        lbSn.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbSn.setText("SN:");
        this.text = new Text((Composite)scriptGroup, 2048);
        this.text.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Button button_2 = new Button((Composite)scriptGroup, 0);
        button_2.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                if (Xds40Composite.this.combo.getSelectionIndex() < 0) {
                    Xds40Composite.this.showInfo(MsgCenter.getString("vds.nousb"));
                    return;
                }
                if (Xds40Composite.this.text.getText().length() == 0) {
                    Xds40Composite.this.showInfo(MsgCenter.getString("vds.nullpath"));
                    return;
                }
                Xds40Composite.this.presenter.writeImage(":DOWNload:SCRIpt ", Xds40Composite.this.text.getText().trim(), true);
            }
        });
        button_2.setText(MsgCenter.getString("vds.write"));
        btnWrite.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                if (Xds40Composite.this.combo.getSelectionIndex() < 0) {
                    Xds40Composite.this.showInfo(MsgCenter.getString("vds.nousb"));
                    return;
                }
                if (Xds40Composite.this.writeTxt.getText().length() == 0) {
                    Xds40Composite.this.showInfo(MsgCenter.getString("vds.nullpath"));
                    return;
                }
                String path = Xds40Composite.this.writeTxt.getText().trim();
                try {
                    FileInputStream fis = new FileInputStream(path);
                    byte[] b = new byte[fis.available()];
                    fis.read(b);
                    Xds40Composite.this.text.setText(new String(b, 40, 20).trim());
                    fis.close();
                    Xds40Composite.this.presenter.writeImage(":DOWNload:SCRIpt ", Xds40Composite.this.text.getText().trim(), false);
                    Thread.sleep(100L);
                    Xds40Composite.this.presenter.writeXdsTxt(":DOWNload:TXT ", b);
                }
                catch (Exception exception) {}
            }
        });
    }

    @Override
    public void refreshCom(List<String> list) {
        this.combo.removeAll();
        for (String s : list) {
            if (s.equalsIgnoreCase("")) continue;
            this.combo.add(s);
        }
        this.combo.select(0);
    }

    @Override
    public void showInfo(String s) {
        MessageDialog.openInformation((Shell)this.com.getShell(), (String)"", (String)s);
    }
}

