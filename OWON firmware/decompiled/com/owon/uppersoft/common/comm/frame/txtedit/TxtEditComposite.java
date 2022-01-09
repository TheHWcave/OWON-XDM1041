/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.sf.json.JSONArray
 *  net.sf.json.JSONObject
 *  org.eclipse.jface.dialogs.MessageDialog
 *  org.eclipse.swt.events.MouseAdapter
 *  org.eclipse.swt.events.MouseEvent
 *  org.eclipse.swt.events.MouseListener
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.layout.RowLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame.txtedit;

import com.owon.uppersoft.common.comm.frame.txtedit.CommEditComposite;
import com.owon.uppersoft.common.comm.frame.txtedit.IShowMessage;
import com.owon.uppersoft.common.comm.frame.txtedit.TxtEditModel;
import com.owon.uppersoft.common.comm.frame.txtedit.TxtEditPresenter;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TxtEditComposite
implements IShowMessage {
    private Composite com;
    private Text txManu;
    private Text txModel;
    private Text txVer;
    private Text txSn;
    private Text txClientversion;
    private Text txClientsum;
    private Group gpLang;
    private Group gpFunc;
    private Button[] btnLanguage;
    private Button[] btnFunction;
    private Combo cobType;
    private Shell shell;
    private Combo cobDefLang;
    private Combo cobTxtVer;
    private TxtEditPresenter presenter;
    private Label label;
    private Text text;
    private Button button_1;
    private CommEditComposite head;

    public Composite getComposite() {
        return this.com;
    }

    public TxtEditComposite(Composite parent) {
        this.shell = parent.getShell();
        this.com = new Composite(parent, 0);
        this.com.setLayout((Layout)new GridLayout());
        this.presenter = new TxtEditPresenter(this);
        this.head = new CommEditComposite(this.com, 0, this.presenter);
        Group gpModel = new Group(this.com, 0);
        gpModel.setLayout((Layout)new GridLayout(6, false));
        GridData gd_gpModel = new GridData(4, 4, true, false);
        gd_gpModel.heightHint = 140;
        gpModel.setLayoutData((Object)gd_gpModel);
        Label lbManu = new Label((Composite)gpModel, 0);
        lbManu.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbManu.setText(MsgCenter.getString("ODP.manu"));
        this.txManu = new Text((Composite)gpModel, 2048);
        GridData gd_txManu = new GridData(4, 0x1000000, true, false, 1, 1);
        gd_txManu.widthHint = 128;
        this.txManu.setLayoutData((Object)gd_txManu);
        Label lbModel = new Label((Composite)gpModel, 0);
        lbModel.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbModel.setText(MsgCenter.getString("txtedit.model"));
        this.txModel = new Text((Composite)gpModel, 2048);
        GridData gd_txModel = new GridData(4, 0x1000000, true, false, 1, 1);
        gd_txModel.widthHint = 128;
        this.txModel.setLayoutData((Object)gd_txModel);
        Label lbClientversion = new Label((Composite)gpModel, 0);
        lbClientversion.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbClientversion.setText(MsgCenter.getString("ODP.clientversion"));
        this.txClientversion = new Text((Composite)gpModel, 2048);
        this.txClientversion.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Label lbVer = new Label((Composite)gpModel, 0);
        lbVer.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbVer.setText(MsgCenter.getString("ODP.ver"));
        this.txVer = new Text((Composite)gpModel, 2048);
        this.txVer.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Label lbSn = new Label((Composite)gpModel, 0);
        lbSn.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbSn.setText(MsgCenter.getString("ODP.sn"));
        this.txSn = new Text((Composite)gpModel, 2048);
        this.txSn.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Label lbClientsum = new Label((Composite)gpModel, 0);
        lbClientsum.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbClientsum.setText(MsgCenter.getString("ODP.clientsum"));
        this.txClientsum = new Text((Composite)gpModel, 2048);
        this.txClientsum.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Label lbType = new Label((Composite)gpModel, 0);
        lbType.setLayoutData((Object)new GridData(16384, 1024, false, false, 1, 1));
        lbType.setText(MsgCenter.getString("ODP.type"));
        this.cobType = new Combo((Composite)gpModel, 0);
        this.cobType.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Label lbDefLang = new Label((Composite)gpModel, 0);
        lbDefLang.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbDefLang.setText(MsgCenter.getString("txtedit.deflang"));
        this.cobDefLang = new Combo((Composite)gpModel, 2056);
        this.cobDefLang.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Label lbTxtVer = new Label((Composite)gpModel, 0);
        lbTxtVer.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbTxtVer.setText(MsgCenter.getString("txtedit.intver"));
        this.cobTxtVer = new Combo((Composite)gpModel, 2048);
        this.cobTxtVer.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.label = new Label((Composite)gpModel, 0);
        this.label.addMouseListener((MouseListener)new MouseAdapter(){

            public void mouseDown(MouseEvent e) {
                if (e.count > 3 && e.button == 3) {
                    TxtEditComposite.this.genMac();
                }
            }
        });
        this.label.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        this.label.setText(MsgCenter.getString("txtedit.macadd"));
        this.text = new Text((Composite)gpModel, 2048);
        this.text.setEditable(false);
        this.text.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.button_1 = new Button((Composite)gpModel, 32);
        this.button_1.setText(MsgCenter.getString("txtedit.stay"));
        new Label((Composite)gpModel, 0);
        new Label((Composite)gpModel, 0);
        new Label((Composite)gpModel, 0);
        this.gpLang = new Group(this.com, 0);
        this.gpLang.setLayout((Layout)new RowLayout(256));
        this.gpLang.setLayoutData((Object)new GridData(4, 4, true, false, 1, 1));
        this.gpLang.setText(MsgCenter.getString("ODP.language"));
        this.gpFunc = new Group(this.com, 0);
        this.gpFunc.setLayout((Layout)new RowLayout(256));
        this.gpFunc.setLayoutData((Object)new GridData(4, 4, true, false, 1, 1));
        this.gpFunc.setText(MsgCenter.getString("ODP.function"));
        this.btnLanguage = new Button[80];
        int i = 0;
        while (i < this.btnLanguage.length) {
            this.btnLanguage[i] = new Button((Composite)this.gpLang, 32);
            this.btnLanguage[i].setVisible(false);
            this.btnLanguage[i].setText("");
            ++i;
        }
        this.btnFunction = new Button[80];
        i = 0;
        while (i < this.btnFunction.length) {
            this.btnFunction[i] = new Button((Composite)this.gpFunc, 32);
            this.btnFunction[i].setVisible(false);
            this.btnFunction[i].setText("");
            ++i;
        }
        this.head.init();
    }

    public void init(JSONObject jo) {
        if (jo == null) {
            return;
        }
        try {
            JSONArray intModel = jo.getJSONArray("intmodel");
            this.cobType.removeAll();
            int i = 0;
            while (i < intModel.size()) {
                this.cobType.add(intModel.getString(i));
                ++i;
            }
            JSONArray txtVer = jo.getJSONArray("txtver");
            this.cobTxtVer.removeAll();
            int i2 = 0;
            while (i2 < txtVer.size()) {
                this.cobTxtVer.add(txtVer.getString(i2));
                ++i2;
            }
            this.cobDefLang.removeAll();
            JSONArray language = jo.getJSONArray("language");
            int i3 = 0;
            while (i3 < language.size()) {
                this.btnLanguage[i3].setVisible(true);
                this.btnLanguage[i3].setText(language.getString(i3));
                this.cobDefLang.add(language.getString(i3));
                ++i3;
            }
            this.cobType.select(0);
            while (i3 < 80) {
                this.btnLanguage[i3].setVisible(false);
                this.btnLanguage[i3].setText("");
                ++i3;
            }
            this.gpLang.pack();
            JSONArray function = jo.getJSONArray("function");
            i3 = 0;
            while (i3 < function.size()) {
                this.btnFunction[i3].setVisible(true);
                this.btnFunction[i3].setText(function.getString(i3));
                ++i3;
            }
            while (i3 < 80) {
                this.btnFunction[i3].setVisible(false);
                this.btnFunction[i3].setText("");
                ++i3;
            }
            this.gpFunc.pack();
            JSONArray diy = jo.optJSONArray("diy");
            if (diy != null) {
                this.doDiy(diy);
            }
        }
        catch (Exception e) {
            this.showInfo(e.getMessage());
        }
    }

    /*
     * Unable to fully structure code
     */
    private void doDiy(JSONArray diy) {
        i = 0;
        while (i < diy.size()) {
            item = diy.getJSONObject(i);
            var4_4 = item.getString("type");
            tmp = -1;
            switch (var4_4.hashCode()) {
                case 3556653: {
                    if (var4_4.equals("text")) {
                        tmp = 1;
                    }
                    break;
                }
                case 94627080: {
                    if (var4_4.equals("check")) {
                        tmp = 1;
                    }
                    break;
                }
                case 94843278: {
                    if (!var4_4.equals("combo")) break;
                    tmp = 1;
                    break;
                }
            }
            ** switch (tmp)
lbl24:
            // 2 sources

            ++i;
        }
    }

    public void init(TxtEditModel model) {
        this.txVer.setText(model.getVer());
        this.txSn.setText(model.getSn());
        this.txModel.setText(model.getExtModel());
        this.txManu.setText(model.getManufId());
        this.setValue(this.btnLanguage, model.getLang());
        this.setValue(this.btnFunction, model.getFunc());
        this.cobType.setText(model.getIntModel());
        this.cobDefLang.select(model.getDefLanguage());
        this.cobTxtVer.setText(model.getTxtVer());
        this.byte2Mac(model);
        this.txClientversion.setText(model.getClientversion());
        this.txClientsum.setText(model.getClientSum());
    }

    public void save(TxtEditModel model) {
        if (!this.button_1.getSelection()) {
            this.genMac();
        }
        model.setVer(this.txVer.getText().trim());
        model.setSn(this.txSn.getText().trim());
        model.setExtModel(this.txModel.getText().trim());
        model.setManufId(this.txManu.getText().trim());
        this.getValue(this.btnLanguage, model.getLang());
        this.getValue(this.btnFunction, model.getFunc());
        model.setIntModel(this.cobType.getText());
        model.setDefLanguage(this.cobDefLang.getSelectionIndex());
        model.setTxtVer(this.cobTxtVer.getText());
        this.mac2Byte(model);
        model.setClientversion(this.txClientversion.getText().trim());
        model.setClientSum(this.txClientsum.getText().trim());
    }

    private void byte2Mac(TxtEditModel model) {
        byte[] macBytes = model.getMacAdd();
        String value = "";
        int i = 0;
        while (i < macBytes.length) {
            String sTemp = Integer.toHexString(0xFF & macBytes[i]);
            value = String.valueOf(value) + sTemp + ":";
            ++i;
        }
        value = value.substring(0, value.lastIndexOf(":"));
        this.text.setText(value);
    }

    private void mac2Byte(TxtEditModel model) {
        byte[] macBytes = new byte[6];
        String[] strArr = this.text.getText().split(":");
        int i = 0;
        while (i < strArr.length) {
            int value = Integer.parseInt(strArr[i], 16);
            macBytes[i] = (byte)value;
            ++i;
        }
        model.setMacAdd(macBytes);
    }

    private void setValue(Button[] btn, boolean[] b) {
        int i = 0;
        while (i < btn.length) {
            btn[i].setSelection(b[i]);
            ++i;
        }
    }

    private void getValue(Button[] btn, boolean[] b) {
        int i = 0;
        while (i < btn.length) {
            b[i] = btn[i].getSelection();
            ++i;
        }
    }

    @Override
    public void showInfo(String s) {
        MessageDialog.openConfirm((Shell)this.shell, (String)"err", (String)s);
    }

    public void genMac() {
        String pre = "4C:4C:50:";
        String s = Long.toHexString(System.currentTimeMillis()).toUpperCase();
        pre = String.valueOf(pre) + s.substring(4, 6) + ":";
        pre = String.valueOf(pre) + s.substring(6, 8) + ":";
        pre = String.valueOf(pre) + s.substring(8, 10);
        this.text.setText(pre);
    }

    public void enableSave(boolean b) {
        this.head.enableSave(b);
    }
}

