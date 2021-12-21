/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.sf.json.JSONArray
 *  net.sf.json.JSONObject
 *  org.eclipse.jface.dialogs.MessageDialog
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm.frame.odptxtedit;

import com.owon.uppersoft.common.comm.frame.CGroup;
import com.owon.uppersoft.common.comm.frame.odptxtedit.OdpModel;
import com.owon.uppersoft.common.comm.frame.odptxtedit.OdpPresenter;
import com.owon.uppersoft.common.comm.frame.txtedit.CommEditComposite;
import com.owon.uppersoft.common.comm.frame.txtedit.IShowMessage;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.nio.ByteBuffer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class OdpComposite
implements IShowMessage {
    private Composite com;
    private String[] lang;
    private String[] func;
    private Text txManu;
    private Text txModel;
    private Text txVer;
    private Text txSn;
    private CGroup gpLang;
    private CGroup gpFunc;
    private Combo cobType;
    private Combo cobHard;
    private Group gpoem;
    private Button btnOem;
    private Button btnLogo;
    private Text[] txt;
    private Label[] lb;
    private OdpPresenter presenter;
    private CommEditComposite head;

    public Composite getComposite() {
        return this.com;
    }

    public OdpComposite(Composite parent) {
        this.com = new Composite(parent, 0);
        this.com.setLayout((Layout)new GridLayout());
        this.presenter = new OdpPresenter(this);
        this.head = new CommEditComposite(this.com, 0, this.presenter);
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
        Label lbType = new Label((Composite)gpModel, 0);
        lbType.setLayoutData((Object)new GridData(16384, 1024, false, false, 1, 1));
        lbType.setText(MsgCenter.getString("ODP.type"));
        this.cobType = new Combo((Composite)gpModel, 8);
        this.cobType.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Label lbHard = new Label((Composite)gpModel, 0);
        lbHard.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        lbHard.setText(MsgCenter.getString("ODP.hard"));
        String[] hard = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        this.cobHard = new Combo((Composite)gpModel, 8);
        this.cobHard.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.cobHard.setItems(hard);
        this.gpLang = new CGroup(this.com, 0, MsgCenter.getString("ODP.language"));
        this.gpLang.setLayoutData(new GridData(4, 4, true, false, 1, 1));
        this.gpFunc = new CGroup(this.com, 0, MsgCenter.getString("ODP.function"));
        this.gpFunc.setLayoutData(new GridData(4, 4, true, false, 1, 1));
        this.gpoem = new Group(this.com, 0);
        this.gpoem.setLayoutData((Object)new GridData(4, 0x1000000, false, false, 1, 1));
        this.gpoem.setLayout((Layout)new GridLayout(8, false));
        this.btnLogo = new Button((Composite)this.gpoem, 32);
        this.btnLogo.setText(MsgCenter.getString("ODP.logo"));
        this.btnOem = new Button((Composite)this.gpoem, 32);
        this.btnOem.setText(MsgCenter.getString("ODP.oem"));
        int size = 15;
        this.lb = new Label[size];
        this.txt = new Text[size];
        int i = 0;
        while (i < size) {
            this.lb[i] = new Label((Composite)this.gpoem, 0);
            this.lb[i].setVisible(false);
            this.txt[i] = new Text((Composite)this.gpoem, 2048);
            this.txt[i].setVisible(false);
            ++i;
        }
        this.head.init();
    }

    @Override
    public void showInfo(String s) {
        MessageDialog.openConfirm((Shell)this.com.getShell(), (String)"err", (String)s);
    }

    public void init(JSONObject jo) {
        try {
            JSONArray ja = jo.getJSONArray("type");
            this.cobType.removeAll();
            int i = 0;
            while (i < ja.size()) {
                this.cobType.add(ja.getString(i));
                ++i;
            }
            ja = jo.getJSONArray("language");
            this.lang = (String[])ja.toArray((Object[])new String[0]);
            this.gpLang.init(this.lang);
            ja = jo.getJSONArray("function");
            this.func = (String[])ja.toArray((Object[])new String[0]);
            this.gpFunc.init(this.func);
            ja = jo.getJSONArray("oemlable");
            int size = ja.size();
            int i2 = 0;
            while (i2 < size) {
                this.lb[i2].setText(ja.getString(i2));
                this.lb[i2].setVisible(true);
                this.txt[i2].setVisible(true);
                ++i2;
            }
            while (i2 < this.lb.length) {
                this.lb[i2].setVisible(false);
                this.txt[i2].setVisible(false);
                ++i2;
            }
        }
        catch (Exception e) {
            this.showInfo(e.getMessage());
        }
    }

    public void init(OdpModel odp) {
        this.txVer.setText(odp.getVer());
        this.txSn.setText(odp.getSn());
        this.txModel.setText(odp.getModel());
        this.txManu.setText(odp.getManu());
        this.gpLang.setValue(odp.getLang());
        this.gpFunc.setValue(odp.getFunc());
        this.cobType.select((int)odp.getType());
        this.cobHard.select((int)odp.getHard());
        this.btnOem.setSelection(odp.isOem());
        this.btnLogo.setSelection(odp.isLogo());
        ByteBuffer bb = odp.getOemValue();
        int i = 0;
        while (i < this.txt.length) {
            this.txt[i].setText(String.valueOf(bb.getFloat(i * 4)));
            ++i;
        }
    }

    public void save(OdpModel odp) {
        if (odp == null) {
            return;
        }
        odp.setVer(this.txVer.getText());
        odp.setSn(this.txSn.getText());
        odp.setModel(this.txModel.getText());
        odp.setManu(this.txManu.getText());
        odp.setLang(this.gpLang.getValue());
        odp.setFunc(this.gpFunc.getValue());
        odp.setType((byte)this.cobType.getSelectionIndex());
        odp.setHard((byte)this.cobHard.getSelectionIndex());
        odp.setOem(this.btnOem.getSelection());
        odp.setLogo(this.btnLogo.getSelection());
        ByteBuffer bb = odp.getOemValue();
        int i = 0;
        while (i < this.txt.length) {
            try {
                float f = Float.parseFloat(this.txt[i].getText().trim());
                bb.putFloat(i * 4, f);
            }
            catch (Exception exception) {}
            ++i;
        }
    }

    public void enableSave(boolean b) {
        this.head.enableSave(b);
    }
}

