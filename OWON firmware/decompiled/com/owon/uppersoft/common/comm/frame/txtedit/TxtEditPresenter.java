/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.sf.json.JSONArray
 *  net.sf.json.JSONObject
 */
package com.owon.uppersoft.common.comm.frame.txtedit;

import com.owon.uppersoft.common.comm.frame.txtedit.ICommEditPresenter;
import com.owon.uppersoft.common.comm.frame.txtedit.TxtEditComposite;
import com.owon.uppersoft.common.comm.frame.txtedit.TxtEditModel;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.util.ReadJsonFile;
import java.io.File;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TxtEditPresenter
implements ICommEditPresenter {
    TxtEditModel model = new TxtEditModel();
    TxtEditComposite view;
    private JSONObject config;

    public TxtEditPresenter(TxtEditComposite view) {
        this.view = view;
        this.init();
    }

    @Override
    public boolean initView(String series) {
        if (this.config.isNullObject()) {
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
        this.config = ReadJsonFile.getConfig(this.view).getJSONObject("TextEdit");
        if (this.config.isNullObject()) {
            this.view.showInfo(MsgCenter.getString("txtedit.noadptaer"));
        }
    }

    @Override
    public String[] getModel() {
        if (this.config.isNullObject()) {
            return new String[]{"null"};
        }
        JSONArray ja = this.config.names();
        return (String[])ja.toArray((Object[])new String[0]);
    }

    @Override
    public void read(String s) {
    }

    @Override
    public String loadData(File f) {
        this.model.read(f);
        String series = this.model.getSeries().trim();
        if (this.initView(series)) {
            this.view.init(this.model);
            this.view.enableSave(true);
            return series;
        }
        this.view.enableSave(false);
        return "";
    }
}

