/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job;

public class CompareParm {
    private static int u_or_a = 12;
    private static int u_nor_a = 8;
    private String[] txtVersionType = new String[]{"07200", "10205", "10204", "10203", "10202", "10201", "10200", "20202", "20201", "20200", "30200"};
    private int[] txtVersionByte = new int[]{8, 12, 8, 12, 8, 12, 8, 8, 12, 8, 8};
    private int[] txtVersionSampleRate = new int[]{1, 2, 2, 1, 1, 1, 1, 1, 2, 2, 3};

    public boolean compareID(String id, String signature) {
        String s2;
        if (id.length() > 19) {
            return false;
        }
        String[] str1 = id.split("-");
        String[] str2 = signature.split("-");
        String s1 = String.valueOf(str1[0].substring(1, 4)) + "-" + (str1[1].equals("A") ? 12 : 8) + "-" + this.getStr(str1, "V") + "-" + this.getStr(str1, "AG");
        return s1.equals(s2 = String.valueOf(str2[0].substring(0, 1).equals("X") ? str2[0].substring(4, 7) : str2[0].substring(3, 6)) + "-" + this.getAorU(str2[0]) + "-" + this.getStr(str2, "V") + "-" + this.getStr(str2, "AG"));
    }

    private int getAorU(String s) {
        if (s.contains("A")) {
            return u_or_a;
        }
        if (s.contains("U")) {
            return u_or_a;
        }
        return u_nor_a;
    }

    public boolean compareSN(String sn, String signature) {
        String[] str2 = signature.split("-");
        String str1 = null;
        if (sn.contains("A") && sn.contains("X")) {
            str1 = sn.substring(0, 8);
        } else if (!sn.contains("A") && sn.contains("X")) {
            str1 = sn.substring(0, 7);
        }
        if (sn.contains("U") && sn.contains("N")) {
            str1 = sn.substring(0, 7);
        } else if (!sn.contains("U") && sn.contains("N")) {
            str1 = sn.substring(0, 6);
        }
        return str1.equals(str2[0]);
    }

    public String getStr(String[] str, String s) {
        String[] stringArray = str;
        int n = str.length;
        int n2 = 0;
        while (n2 < n) {
            String string = stringArray[n2];
            if (string.equals(s)) {
                return string;
            }
            ++n2;
        }
        return null;
    }

    public boolean compareTxtVer(String txtVersion, String id) {
        String txtVer = txtVersion.substring(1, 6);
        int index = this.getIndex(txtVer);
        if (index == -1) {
            return false;
        }
        String bandWidth1 = txtVersion.substring(1, 4);
        int byte1 = this.txtVersionByte[index];
        int rate1 = this.txtVersionSampleRate[index];
        String bandWidth2 = id.substring(1, 4);
        int byte2 = this.getAorU(id.substring(5, 6));
        int rate2 = Integer.valueOf(id.substring(0, 1));
        return bandWidth1.equals(bandWidth2) && byte1 == byte2 && rate1 == rate2;
    }

    private int getIndex(String str) {
        int i = 0;
        while (i < this.txtVersionType.length) {
            if (str.equals(this.txtVersionType[i])) {
                return i;
            }
            ++i;
        }
        return -1;
    }
}

