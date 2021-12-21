/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.ei;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesAccess {
    public static Properties loadProperties(File file) {
        Properties properties;
        block7: {
            FileInputStream inputStream = null;
            Properties p = new Properties();
            try {
                inputStream = new FileInputStream(file);
                p.load(inputStream);
                p.list(System.out);
                properties = p;
                if (inputStream == null) break block7;
            }
            catch (Throwable throwable) {
                try {
                    if (inputStream != null) {
                        ((InputStream)inputStream).close();
                    }
                    throw throwable;
                }
                catch (IOException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                    return null;
                }
                catch (Exception ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                    return null;
                }
            }
            ((InputStream)inputStream).close();
        }
        return properties;
    }

    public static void storeProperties(Properties p, File file) {
        FileOutputStream outputStream = null;
        try {
            try {
                outputStream = new FileOutputStream(file);
                p.store(outputStream, "");
            }
            finally {
                if (outputStream != null) {
                    ((OutputStream)outputStream).close();
                }
            }
        }
        catch (IOException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        PropertiesAccess.loadProperties(new File("c:/a.bin"));
    }
}

