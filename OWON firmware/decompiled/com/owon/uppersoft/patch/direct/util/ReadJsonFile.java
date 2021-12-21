/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.sf.json.JSONObject
 */
package com.owon.uppersoft.patch.direct.util;

import com.owon.uppersoft.common.comm.frame.txtedit.IShowMessage;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.json.JSONObject;

public class ReadJsonFile {
    public static String getLastedVer() {
        File f = new File(".");
        String[] s = f.list();
        String result = null;
        int i = 0;
        String[] stringArray = s;
        int n = s.length;
        int n2 = 0;
        while (n2 < n) {
            String string = stringArray[n2];
            if (string.contains("txtedit") && string.endsWith(".json")) {
                Pattern p = Pattern.compile("\\d{8}");
                Matcher m = p.matcher(string);
                if (m.find()) {
                    int find = Integer.parseInt(m.group());
                    if (find > i) {
                        i = find;
                        result = string;
                    }
                } else if (result == null) {
                    result = string;
                }
            }
            ++n2;
        }
        return result;
    }

    public static JSONObject getConfig(IShowMessage view) {
        String name = ReadJsonFile.getLastedVer();
        File f = new File(name);
        if (!f.exists()) {
            view.showInfo(MsgCenter.getString("txtedit.exitst"));
            return null;
        }
        String s = ReadJsonFile.readToString(f);
        if (s == null) {
            view.showInfo("The OS does not support UTF-8");
            return null;
        }
        try {
            return JSONObject.fromObject((Object)s);
        }
        catch (Exception exception) {
            view.showInfo(MsgCenter.getString("txtedit.nojson"));
            return null;
        }
    }

    public static String readToString(String s) {
        return ReadJsonFile.readToString(new File(s));
    }

    public static String readToString(File file) {
        String encoding = "UTF-8";
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String s = new String(filecontent, encoding);
            return s.substring(s.indexOf("{"));
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

