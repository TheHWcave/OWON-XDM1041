/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job;

import com.owon.uppersoft.common.comm.job.CompareParm;
import com.owon.uppersoft.common.comm.job.OperateXdsExcel;
import com.owon.uppersoft.common.comm.job.OrderContentBean;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ProgressData {
    private String path;
    private List<OrderContentBean> beans;
    private CompareParm cp;
    public String orderNum;

    public ProgressData(String path, CompareParm cp) {
        this.path = path;
        this.beans = new ArrayList<OrderContentBean>();
        this.cp = cp;
    }

    public List<OrderContentBean> getBeans() {
        return this.beans;
    }

    public String[] getString() {
        String[] str = new String[this.beans.size()];
        int i = 0;
        while (i < this.beans.size()) {
            str[i] = this.beans.get(i).getAttributeCode();
            ++i;
        }
        return str;
    }

    public void parseExcel() {
        File file = new File(this.path);
        OperateXdsExcel operateXdsExcel = new OperateXdsExcel(file);
        int rows = operateXdsExcel.getRow(0);
        String[] s = operateXdsExcel.getExcel(0, 0, 0).split(MsgCenter.getString("XDS.colon"));
        this.orderNum = s[1];
        int i = 2;
        while (i < rows - 2) {
            String str = operateXdsExcel.getExcel(0, 1, i).trim();
            if (str.equals(MsgCenter.getString("XDS.osc"))) {
                OrderContentBean bean = new OrderContentBean();
                bean.setMachineType(operateXdsExcel.getExcel(0, 2, i).trim());
                bean.setAttributeCode(operateXdsExcel.getExcel(0, 6, i).trim());
                bean.setNum(Integer.parseInt(operateXdsExcel.getExcel(0, 4, i)));
                bean.setSum(Integer.parseInt(operateXdsExcel.getExcel(0, 4, i)));
                this.beans.add(bean);
            }
            ++i;
        }
        operateXdsExcel.close();
    }

    public OrderContentBean getBean(String input) {
        int i = 0;
        while (i < this.beans.size()) {
            OrderContentBean bean = this.beans.get(i);
            String orderAttr = bean.getAttributeCode();
            if (this.cp.compareID(input, orderAttr)) {
                return bean;
            }
            ++i;
        }
        return null;
    }

    public boolean compareID(String input, String orderAttr) {
        return this.cp.compareID(input, orderAttr);
    }

    public boolean compareSN(String input, String orderAttr) {
        return this.cp.compareSN(input, orderAttr);
    }

    public boolean compareTxtVer(String sign, String txtver) {
        return this.cp.compareTxtVer(sign, txtver);
    }

    public int saveVersion(OrderContentBean bean, String input) {
        if (input.length() > 16) {
            return 3;
        }
        int num = bean.getNum();
        boolean isTrue = this.isContain(bean.getVerStr(), input);
        if (!isTrue && num > 0) {
            bean.setNum(--num);
            bean.getVerStr().add(input);
            return 0;
        }
        if (!isTrue && num <= 0) {
            return 2;
        }
        return 1;
    }

    public boolean isContain(HashSet<String> ver, String input) {
        Iterator<String> iterator = ver.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().equals(input)) continue;
            return true;
        }
        return false;
    }

    public boolean isHas() {
        int i = 0;
        while (i < this.beans.size()) {
            OrderContentBean bean = this.beans.get(i);
            if (bean.getNum() > 0) {
                return true;
            }
            ++i;
        }
        return false;
    }
}

