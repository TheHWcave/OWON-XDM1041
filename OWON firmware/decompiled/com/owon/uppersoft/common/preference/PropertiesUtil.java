/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.Point
 *  org.eclipse.swt.graphics.RGB
 */
package com.owon.uppersoft.common.preference;

import com.owon.uppersoft.common.utils.StringPool;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;

public class PropertiesUtil {
    public static final String Language = "Language";
    public static final String Country = "Country";
    public static final String Variant = "Variant";

    public static boolean loadBoolean(Properties p, String key) {
        String s = p.getProperty(key, "");
        return s.equalsIgnoreCase("1");
    }

    public static void persistBoolean(Properties p, String key, boolean b) {
        String s = b ? "1" : "0";
        p.setProperty(key, s);
    }

    public static Locale loadLocale(Properties p) {
        Locale locale = Locale.ENGLISH;
        String lan = p.getProperty(Language, "");
        if (!lan.equals("")) {
            String cou = p.getProperty(Country, "");
            String var = p.getProperty(Variant, "");
            locale = new Locale(lan, cou, var);
        }
        return locale;
    }

    public static void persistLocale(Properties p, Locale locale) {
        p.setProperty(Language, locale.getLanguage());
        p.setProperty(Country, locale.getCountry());
        p.setProperty(Variant, locale.getVariant());
    }

    public static List<File> loadFileHistory(Properties p, String key, int limit) {
        ArrayList<File> list = new ArrayList<File>(limit);
        String fh = p.getProperty(key, "");
        StringTokenizer st = new StringTokenizer(fh, ";");
        int c = 0;
        while (st.hasMoreTokens()) {
            if (c == limit) break;
            File file = new File(st.nextToken());
            if (!file.exists()) continue;
            list.add(file);
            ++c;
        }
        return list;
    }

    public static void persistFileHistory(Properties p, String key, List<File> list) {
        StringBuilder sb = new StringBuilder();
        for (File file : list) {
            sb.append(file.getPath());
            sb.append(";");
        }
        p.setProperty(key, sb.toString());
    }

    public static List<String> loadStringList(Properties p, String key, String delim) {
        LinkedList<String> list = new LinkedList<String>();
        String fh = p.getProperty(key, "");
        StringTokenizer st = new StringTokenizer(fh, delim);
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        return list;
    }

    public static void persistStringList(Properties p, String key, List<String> list, String delim) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append(delim);
        }
        p.setProperty(key, sb.toString());
    }

    public static RGB loadRGB(Properties p, String key, RGB rgb) {
        if (rgb == null) {
            rgb = new RGB(255, 255, 255);
        }
        int value = 0;
        String name = p.getProperty(key, "");
        try {
            value = Integer.valueOf(name);
            rgb.red = value >> 16 & 0xFF;
            rgb.green = value >> 8 & 0xFF;
            rgb.blue = value & 0xFF;
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return rgb;
    }

    public static void persistRGB(Properties p, String key, RGB rgb) {
        int v = rgb.red << 16 | rgb.green << 8 | rgb.blue;
        p.setProperty(key, String.valueOf(v));
    }

    public static int loadInt(Properties p, String key) {
        String name = p.getProperty(key, "");
        int value = 0;
        try {
            value = Integer.valueOf(name);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void persistInt(Properties p, String key, int value) {
        p.setProperty(key, String.valueOf(value));
    }

    public static Locale forName(String name) {
        String[] strs = name.split("_");
        switch (strs.length) {
            case 1: {
                return new Locale(strs[0]);
            }
            case 2: {
                return new Locale(strs[0], strs[1]);
            }
            case 3: {
                return new Locale(strs[0], strs[1], strs[2]);
            }
        }
        return null;
    }

    public static List<Locale> forNames(String[] names) {
        LinkedList<Locale> tls = new LinkedList<Locale>();
        String[] stringArray = names;
        int n = names.length;
        int n2 = 0;
        while (n2 < n) {
            String ln = stringArray[n2];
            Locale l = PropertiesUtil.forName(ln);
            if (l != null) {
                tls.add(l);
            }
            ++n2;
        }
        return tls;
    }

    public static Point loadPoint(Properties p, String key, Point pt) {
        String v;
        String[] xy;
        if (pt == null) {
            pt = new Point(0, 0);
        }
        if ((xy = (v = p.getProperty(key, "")).split(",")).length >= 2) {
            try {
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                pt.x = x;
                pt.y = y;
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return pt;
    }

    public static void persistPoint(Properties p, String key, Point pt) {
        p.setProperty(key, String.valueOf(pt.x) + "," + pt.y);
    }

    public static int[] loadInts(Properties p, String key, int length) {
        String v = p.getProperty(key, "");
        String[] ss = v.split(",");
        if (length > 0 && ss.length < length) {
            return StringPool.EmptyIntArray;
        }
        int[] ints = new int[length];
        try {
            int i = 0;
            while (i < length) {
                ints[i] = Integer.parseInt(ss[i]);
                ++i;
            }
            return ints;
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            return StringPool.EmptyIntArray;
        }
    }

    public static void persistInts(Properties p, String key, int[] ints) {
        StringBuilder sb = new StringBuilder();
        if (ints != null && ints.length != 0) {
            int[] nArray = ints;
            int n = ints.length;
            int n2 = 0;
            while (n2 < n) {
                int i = nArray[n2];
                sb.append(i);
                sb.append(",");
                ++n2;
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        p.setProperty(key, sb.toString());
    }
}

