/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job;

import java.util.HashSet;

public class OrderContentBean {
    private String machineType;
    private int num;
    private int sum;
    private String attributeCode;
    private HashSet<String> verStr = new HashSet();

    public String getMachineType() {
        return this.machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public int getSum() {
        return this.sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getAttributeCode() {
        return this.attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    public HashSet<String> getVerStr() {
        return this.verStr;
    }

    public void setVerStr(HashSet<String> verStr) {
        this.verStr = verStr;
    }

    public String toString() {
        return "machineType=" + this.machineType + "  " + "num=" + this.num + "  " + "attributeCode=" + this.attributeCode;
    }
}

