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
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.comm.frame.BeanMulti;
import com.owon.uppersoft.common.comm.frame.CGroup;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class MultimeterComposite {
    private Composite com;
    private String[] lang;
    private String[] func;
    private Text txManu;
    private Text txModel;
    private Text txVer;
    private Text txSn;
    private Text txPath;
    private BeanMulti mul;
    private CGroup gpLang;
    private CGroup gpFunc;
    private Button specialSW;

    public Composite getComposite() {
        return this.com;
    }

    public MultimeterComposite(Composite parent) {
        this.com = new Composite(parent, 0);
        this.com.setLayout((Layout)new GridLayout());
        Group gpFile = new Group(this.com, 0);
        RowLayout rl_gpFile = new RowLayout(256);
        rl_gpFile.marginBottom = 0;
        rl_gpFile.marginTop = 0;
        gpFile.setLayout((Layout)rl_gpFile);
        gpFile.setLayoutData((Object)new GridData(4, 0x1000000, false, false, 1, 1));
        Label lbPath = new Label((Composite)gpFile, 0);
        lbPath.setText(MsgCenter.getString("ODP.path"));
        this.txPath = new Text((Composite)gpFile, 2048);
        this.txPath.setLayoutData((Object)new RowData(277, -1));
        Button btnRead = new Button((Composite)gpFile, 0);
        btnRead.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                MultimeterComposite.this.mul = new BeanMulti();
                FileDialog fd = new FileDialog(MultimeterComposite.this.com.getShell());
                String s = fd.open();
                if (s == null) {
                    return;
                }
                File f = new File(s);
                if (!f.exists()) {
                    return;
                }
                MultimeterComposite.this.txPath.setText(s);
                MultimeterComposite.this.mul.read(f);
                MultimeterComposite.this.txVer.setText(MultimeterComposite.this.mul.getVer());
                MultimeterComposite.this.txSn.setText(MultimeterComposite.this.mul.getSn());
                MultimeterComposite.this.txModel.setText(MultimeterComposite.this.mul.getModel());
                MultimeterComposite.this.txManu.setText(MultimeterComposite.this.mul.getManu());
                MultimeterComposite.this.gpLang.setValue(MultimeterComposite.this.mul.getLang());
                MultimeterComposite.this.gpFunc.setValue(MultimeterComposite.this.mul.getFunc());
                MultimeterComposite.this.specialSW.setSelection(MultimeterComposite.this.mul.getSpecialSW() == 1);
            }
        });
        btnRead.setText(MsgCenter.getString("ODP.read"));
        Button btnSave = new Button((Composite)gpFile, 0);
        btnSave.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                if (MultimeterComposite.this.mul == null) {
                    return;
                }
                MultimeterComposite.this.mul.setVer(MultimeterComposite.this.txVer.getText());
                MultimeterComposite.this.mul.setSn(MultimeterComposite.this.txSn.getText());
                MultimeterComposite.this.mul.setModel(MultimeterComposite.this.txModel.getText());
                MultimeterComposite.this.mul.setManu(MultimeterComposite.this.txManu.getText());
                MultimeterComposite.this.mul.setLang(MultimeterComposite.this.gpLang.getValue());
                MultimeterComposite.this.mul.setFunc(MultimeterComposite.this.gpFunc.getValue());
                MultimeterComposite.this.mul.setSpecialSW((byte)(MultimeterComposite.this.specialSW.getSelection() ? 1 : 0));
                MultimeterComposite.this.mul.save(MultimeterComposite.this.txPath.getText());
            }
        });
        btnSave.setText(MsgCenter.getString("ODP.save"));
        Group gpModel = new Group(this.com, 0);
        gpModel.setLayout((Layout)new GridLayout(4, false));
        GridData gd_gpModel = new GridData(4, 0x1000000, true, false);
        gd_gpModel.heightHint = 100;
        gpModel.setLayoutData((Object)gd_gpModel);
        Label lbManu = new Label((Composite)gpModel, 0);
        lbManu.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbManu.setText(MsgCenter.getString("ODP.manu"));
        this.txManu = new Text((Composite)gpModel, 2048);
        GridData gd_txManu = new GridData(4, 0x1000000, false, false, 1, 1);
        gd_txManu.widthHint = 128;
        this.txManu.setLayoutData((Object)gd_txManu);
        Label lbModel = new Label((Composite)gpModel, 0);
        lbModel.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbModel.setText(MsgCenter.getString("ODP.model"));
        this.txModel = new Text((Composite)gpModel, 2048);
        GridData gd_txModel = new GridData(4, 0x1000000, false, false, 1, 1);
        gd_txModel.widthHint = 128;
        this.txModel.setLayoutData((Object)gd_txModel);
        Label lbVer = new Label((Composite)gpModel, 0);
        lbVer.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbVer.setText(MsgCenter.getString("ODP.ver"));
        this.txVer = new Text((Composite)gpModel, 2048);
        this.txVer.setLayoutData((Object)new GridData(4, 0x1000000, false, false, 1, 1));
        Label lbSn = new Label((Composite)gpModel, 0);
        lbSn.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbSn.setText(MsgCenter.getString("ODP.sn"));
        this.txSn = new Text((Composite)gpModel, 2048);
        this.txSn.setLayoutData((Object)new GridData(4, 0x1000000, false, false, 1, 1));
        this.lang = new String[]{"LAG.Chn", "LAG.TChn", "LAG.En", "LAG.Pol", "LAG.Rus", "LAG.Gem", "LAG.Span", "LAG.Fre", "LAG.Por", "LAG.Ita", "LAG.Jap", "LAG.Kor"};
        this.gpLang = new CGroup(this.com, 0, MsgCenter.getString("ODP.language"), this.lang);
        this.gpLang.setLayoutData(new GridData(4, 4, true, false, 1, 1));
        this.func = new String[]{"XDM.fun.net", "XDM.fun.wifi"};
        this.gpFunc = new CGroup(this.com, 0, MsgCenter.getString("ODP.function"), this.func);
        this.gpFunc.setLayoutData(new GridData(4, 4, true, false, 1, 1));
        this.specialSW = new Button((Composite)this.gpFunc, 32);
        this.specialSW.setText(MsgCenter.getString("XDM.specialsw"));
    }
}

