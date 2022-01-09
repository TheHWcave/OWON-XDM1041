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
import com.owon.uppersoft.common.comm.frame.vds.WriteItem;
import com.owon.uppersoft.common.comm.frame.vds.WriteType;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.util.ArrayList;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class VdsComposite
implements IShowMessage,
IRefreshCom {
    private Composite com;
    private Combo combo;
    private Text txt;
    private VdsPresenter presenter;

    public Composite getComposite() {
        return this.com;
    }

    public VdsComposite(Composite parent) {
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
                VdsComposite.this.presenter.setCom(VdsComposite.this.combo.getText());
            }
        });
        Button button = new Button((Composite)group, 0);
        button.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                VdsComposite.this.presenter.refreshCom();
            }
        });
        button.setText(MsgCenter.getString("vds.refresh"));
        Group tdsGroup = new Group(this.com, 0);
        tdsGroup.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        GridLayout gl_tdsGroup = new GridLayout();
        gl_tdsGroup.marginWidth = 10;
        gl_tdsGroup.marginHeight = 10;
        gl_tdsGroup.numColumns = 5;
        tdsGroup.setLayout((Layout)gl_tdsGroup);
        Label label_3 = new Label((Composite)tdsGroup, 0);
        label_3.setText("Txt");
        Label label_1 = new Label((Composite)tdsGroup, 0);
        label_1.setText(MsgCenter.getString("TC.path"));
        this.txt = new Text((Composite)tdsGroup, 2056);
        this.txt.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        Button btnBrowse = new Button((Composite)tdsGroup, 0);
        btnBrowse.setText(MsgCenter.getString("TC.browse"));
        Button button_1 = new Button((Composite)tdsGroup, 0);
        button_1.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                if (VdsComposite.this.combo.getSelectionIndex() < 0) {
                    VdsComposite.this.showInfo(MsgCenter.getString("vds.nousb"));
                    return;
                }
                if (VdsComposite.this.txt.getText().length() == 0) {
                    VdsComposite.this.showInfo(MsgCenter.getString("vds.nullpath"));
                    return;
                }
                VdsComposite.this.presenter.readFile(":UPLoad:TXT?", VdsComposite.this.txt.getText().trim());
            }
        });
        button_1.setText(MsgCenter.getString("vds.read"));
        Group group_1 = new Group(this.com, 0);
        group_1.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        group_1.setLayout((Layout)new GridLayout(1, false));
        final ArrayList<WriteItem> items = new ArrayList<WriteItem>();
        items.add(new WriteItem((Composite)group_1, WriteType.OS));
        items.add(new WriteItem((Composite)group_1, WriteType.FPGA));
        items.add(new WriteItem((Composite)group_1, WriteType.TXT));
        items.add(new WriteItem((Composite)group_1, WriteType.ZIP));
        Button button_2 = new Button((Composite)group_1, 0);
        button_2.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        button_2.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                if (VdsComposite.this.combo.getSelectionIndex() < 0) {
                    VdsComposite.this.showInfo(MsgCenter.getString("vds.nousb"));
                    return;
                }
                VdsComposite.this.presenter.writeFiles(items);
            }
        });
        button_2.setText(MsgCenter.getString("vds.write"));
        btnBrowse.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dd = new DirectoryDialog(VdsComposite.this.com.getShell());
                String path = VdsComposite.this.txt.getText();
                if (!path.equals("")) {
                    dd.setFilterPath(path);
                }
                if ((path = dd.open()) == null) {
                    return;
                }
                VdsComposite.this.txt.setText(path);
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

