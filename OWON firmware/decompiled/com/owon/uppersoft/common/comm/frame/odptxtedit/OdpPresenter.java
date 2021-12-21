/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.sf.json.JSONObject
 */
package com.owon.uppersoft.common.comm.frame.odptxtedit;

import com.owon.uppersoft.common.comm.frame.odptxtedit.OdpComposite;
import com.owon.uppersoft.common.comm.frame.odptxtedit.OdpModel;
import com.owon.uppersoft.common.comm.frame.txtedit.ICommEditPresenter;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.util.ReadJsonFile;
import java.io.File;
import net.sf.json.JSONObject;

public class OdpPresenter
implements ICommEditPresenter {
    OdpModel model = new OdpModel();
    OdpComposite view;
    private JSONObject config;

    public OdpPresenter(OdpComposite view) {
        this.view = view;
        this.init();
    }

    @Override
    public boolean initView(String series) {
        if (this.config == null) {
            return false;
        }
        int i = 0;
        while (i < series.length()) {
            int end = series.length() - i;
            JSONObject j = this.config.optJSONObject(series.substring(0, end));
            if (j != null) {
                this.view.init(j);
                return true;
            }
            ++i;
        }
        this.view.showInfo(MsgCenter.getString("txtedit.noadptaer"));
        return false;
    }

    @Override
    public void save(String path) {
        this.view.save(this.model);
        this.model.save(path);
        this.view.showInfo("success");
    }

    public JSONObject getConfig() {
        return this.config;
    }

    @Override
    public void init() {
        this.config = ReadJsonFile.getConfig(this.view);
        if (this.config.isNullObject()) {
            this.view.showInfo(MsgCenter.getString("txtedit.noadptaer"));
        }
    }

    @Override
    public String[] getModel() {
        return new String[]{"ODP"};
    }

    @Override
    public void read(String s) {
    }

    @Override
    public String loadData(File f) {
        this.model.read(f);
        String series = this.model.getSeries();
        if (this.initView(series)) {
            this.view.init(this.model);
            this.view.enableSave(true);
        } else {
            this.view.enableSave(false);
        }
        return series;
    }
}

