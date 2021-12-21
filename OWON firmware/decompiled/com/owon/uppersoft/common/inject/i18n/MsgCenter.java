/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.inject.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MsgCenter {
    private static final String BUNDLE_NAME = "com.owon.uppersoft.common.inject.i18n.MsgCenter";

    private MsgCenter() {
    }

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle(BUNDLE_NAME);
    }

    public static String getString(String key) {
        try {
            return MsgCenter.getBundle().getString(key);
        }
        catch (MissingResourceException missingResourceException) {
            return String.valueOf('!') + key + '!';
        }
    }
}

