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
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame.vds;

import com.owon.uppersoft.common.comm.frame.vds.WriteType;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class WriteItem {
    private Text text;
    private Button button_3;
    private WriteType wt;
    private Label lblNewLabel;

    public WriteItem(final Composite parent, WriteType wt) {
        this.wt = wt;
        Composite group_1 = new Composite(parent, 0);
        group_1.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        group_1.setLayout((Layout)new GridLayout(5, false));
        this.button_3 = new Button(group_1, 32);
        this.button_3.setText(wt.name());
        Label label = new Label(group_1, 0);
        label.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        label.setText(MsgCenter.getString("TC.path"));
        this.text = new Text(group_1, 2056);
        this.text.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Button button_4 = new Button(group_1, 0);
        button_4.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                FileDialog dd = new FileDialog(parent.getShell());
                String path = WriteItem.this.text.getText();
                if (!path.equals("")) {
                    dd.setFilterPath(path);
                }
                if ((path = dd.open()) == null) {
                    return;
                }
                WriteItem.this.text.setText(path);
            }
        });
        button_4.setText(MsgCenter.getString("TC.browse"));
        this.lblNewLabel = new Label(group_1, 0);
        this.lblNewLabel.setText("  ?  ");
    }

    public String getPath() {
        return this.text.getText();
    }

    public boolean isEnable() {
        return this.button_3.getSelection();
    }

    public int getType() {
        return this.wt.value();
    }

    public String getName() {
        return this.wt.name();
    }

    public void setStatus(boolean b) {
        if (b) {
            this.lblNewLabel.setText("SUCC");
        } else {
            this.lblNewLabel.setText("FAIL");
        }
    }
}

