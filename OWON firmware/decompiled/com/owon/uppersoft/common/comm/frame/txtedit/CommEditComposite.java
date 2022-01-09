/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.RowData
 *  org.eclipse.swt.layout.RowLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame.txtedit;

import com.owon.uppersoft.common.comm.frame.txtedit.ICommEditPresenter;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.io.File;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class CommEditComposite
extends Composite {
    private ICommEditPresenter presenter;
    private Button button;
    private Combo combo;
    private Text txPath;
    private Button btnSave;

    public CommEditComposite(final Composite parent, int style, final ICommEditPresenter presneter) {
        super(parent, style);
        this.presenter = presneter;
        RowLayout rl_gpFile = new RowLayout(256);
        rl_gpFile.marginBottom = 0;
        rl_gpFile.marginTop = 0;
        this.setLayout((Layout)rl_gpFile);
        this.setLayoutData(new GridData(4, 0x1000000, false, false, 1, 1));
        this.button = new Button((Composite)this, 0);
        this.button.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                presneter.init();
                CommEditComposite.this.init();
            }
        });
        this.button.setText(MsgCenter.getString("txtedit.cfg"));
        this.combo = new Combo((Composite)this, 8);
        this.combo.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                CommEditComposite.this.showConfig();
            }
        });
        Label lbPath = new Label((Composite)this, 0);
        lbPath.setText(MsgCenter.getString("ODP.path"));
        this.txPath = new Text((Composite)this, 2048);
        this.txPath.setLayoutData((Object)new RowData(497, -1));
        Button btnRead = new Button((Composite)this, 0);
        btnRead.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                FileDialog fd = new FileDialog(parent.getShell());
                String s = fd.open();
                if (s == null) {
                    return;
                }
                File f = new File(s);
                if (!f.exists()) {
                    return;
                }
                CommEditComposite.this.txPath.setText(s);
                String se = CommEditComposite.this.presenter.loadData(f);
                String[] ss = CommEditComposite.this.combo.getItems();
                int i = 0;
                while (i < ss.length) {
                    if (ss[i].equalsIgnoreCase(se)) {
                        CommEditComposite.this.combo.select(i);
                    }
                    ++i;
                }
            }
        });
        btnRead.setText(MsgCenter.getString("ODP.read"));
        this.btnSave = new Button((Composite)this, 0);
        this.btnSave.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                CommEditComposite.this.presenter.save(CommEditComposite.this.txPath.getText().trim());
            }
        });
        this.btnSave.setText(MsgCenter.getString("ODP.save"));
        this.btnSave.setEnabled(false);
    }

    public void init() {
        this.combo.removeAll();
        String[] items = this.presenter.getModel();
        if (items.length > 0) {
            this.combo.setItems(items);
            this.combo.select(0);
            this.showConfig();
        }
    }

    private void showConfig() {
        String s = this.combo.getText();
        this.presenter.initView(s);
    }

    public void enableSave(boolean b) {
        this.btnSave.setEnabled(b);
    }
}

