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
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.comm.job.model.FileDataFile;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class EncryptionComposite {
    private Text txtPath;
    private Text txtMark;
    private Composite com;

    public Composite getComposite() {
        return this.com;
    }

    public EncryptionComposite(Composite parent) {
        this.com = new Composite(parent, 0);
        this.com.setLayout((Layout)new GridLayout());
        Group encGroup = new Group(this.com, 0);
        encGroup.setText(MsgCenter.getString("EC.encryption"));
        GridData gd_encGroup = new GridData(4, 0x1000000, true, false);
        gd_encGroup.heightHint = 108;
        encGroup.setLayoutData((Object)gd_encGroup);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 10;
        gridLayout.marginHeight = 10;
        gridLayout.numColumns = 3;
        encGroup.setLayout((Layout)gridLayout);
        Label label = new Label((Composite)encGroup, 0);
        label.setText(MsgCenter.getString("EC.prompt"));
        this.txtMark = new Text((Composite)encGroup, 2048);
        GridData gd_txtMark = new GridData(4, 0x1000000, true, false);
        this.txtMark.setLayoutData((Object)gd_txtMark);
        new Label((Composite)encGroup, 0);
        Label label_1 = new Label((Composite)encGroup, 0);
        label_1.setText(MsgCenter.getString("EC.path"));
        this.txtPath = new Text((Composite)encGroup, 2048);
        GridData gd_txtPath = new GridData(4, 0x1000000, true, false);
        this.txtPath.setLayoutData((Object)gd_txtPath);
        Button btnBrowse = new Button((Composite)encGroup, 0);
        btnBrowse.setText(MsgCenter.getString("EC.browse"));
        btnBrowse.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                FileDialog fd = new FileDialog(EncryptionComposite.this.com.getShell());
                String path = fd.open();
                if (path == null) {
                    return;
                }
                EncryptionComposite.this.txtPath.setText(path);
            }
        });
        this.txtMark.setTextLimit(27);
    }

    public FileDataFile getDataFile() {
        FileDataFile df = new FileDataFile("en", 19, this.txtPath.getText(), 16384);
        return df;
    }

    public String getMark() {
        return this.txtMark.getText();
    }
}

