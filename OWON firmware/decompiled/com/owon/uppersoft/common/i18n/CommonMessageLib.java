/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.i18n;

import com.owon.uppersoft.common.utils.StringPool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public class CommonMessageLib {
    private static final String COMMONMSGLIB = "com.owon.uppersoft.common.i18n.CommonMsgLib";
    public static final String COMMONMSGLIB2 = "com.owon.uppersoft.common.i18n.CommonMsgLib2";
    private static ResourceBundle msgBundle;

    static {
        CommonMessageLib.updateLocale();
    }

    public static void updateLocale() {
        msgBundle = ResourceBundle.getBundle(COMMONMSGLIB);
    }

    public static ResourceBundle getDefaultResourceBundle() {
        return msgBundle;
    }

    public static void main(String[] args) {
        Locale locale = Locale.CHINA;
        ArrayList<String> list = new ArrayList<String>();
        list.add(COMMONMSGLIB);
        list.add("com.owon.uppersoft.common.i18n.LAMsgLib");
        list.add("com.owon.uppersoft.common.i18n.OSCMsgLib");
        for (String bundle : list) {
            String path = String.valueOf(bundle.substring(bundle.lastIndexOf(".") + 1)) + "_" + locale + ".txt";
            CommonMessageLib.ascii2nativeByResourceBundle(path, bundle, locale, "UTF-8");
        }
    }

    public static void ascii2nativeByResourceBundle(String name, String bundle, Locale locale, String charset) {
        CommonMessageLib.ascii2nativeByResourceBundle(new File(name), bundle, locale, charset);
    }

    public static void ascii2nativeByResourceBundle(File f, String bundle, Locale locale, String charset) {
        ResourceBundle rb = ResourceBundle.getBundle(bundle, locale);
        Enumeration<String> enties = rb.getKeys();
        try (OutputStreamWriter osr = null;){
            osr = new OutputStreamWriter((OutputStream)new FileOutputStream(f), charset);
            while (enties.hasMoreElements()) {
                String key = enties.nextElement();
                String value = rb.getString(key);
                osr.append(String.valueOf(key) + " = " + value + StringPool.LINE_SEPARATOR);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String ascii2native_DIY(String str) {
        String hex = "0123456789ABCDEF";
        StringBuffer buf = new StringBuffer();
        int i = 0;
        while (i < str.length()) {
            char c = str.charAt(i);
            if (c == '\\' && i + 1 <= str.length() && str.charAt(i + 1) == '\\') {
                buf.append("\\\\");
                ++i;
            } else if (c == '\\' && i + 6 <= str.length() && str.charAt(i + 1) == 'u') {
                String sub = str.substring(i + 2, i + 6).toUpperCase();
                int i0 = hex.indexOf(sub.charAt(0));
                int i1 = hex.indexOf(sub.charAt(1));
                int i2 = hex.indexOf(sub.charAt(2));
                int i3 = hex.indexOf(sub.charAt(3));
                if (i0 < 0 || i1 < 0 || i2 < 0 || i3 < 0) {
                    buf.append("\\u");
                    ++i;
                } else {
                    byte[] data = new byte[]{CommonMessageLib.i2b(i1 + i0 * 16), CommonMessageLib.i2b(i3 + i2 * 16)};
                    try {
                        buf.append(new String(data, "UTF-16BE").toString());
                    }
                    catch (Exception exception) {
                        buf.append("\\u" + sub);
                    }
                    i += 5;
                }
            } else {
                buf.append(c);
            }
            ++i;
        }
        return buf.toString();
    }

    public static final int b2i(byte b) {
        return b < 0 ? 256 + b : b;
    }

    public static final byte i2b(int i) {
        return (byte)(i > 127 ? i - 256 : i);
    }
}

