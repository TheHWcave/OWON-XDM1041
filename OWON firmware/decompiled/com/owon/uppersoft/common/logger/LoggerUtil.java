/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class LoggerUtil {
    public static Logger getFileLogger(String className, Level level, String dirPath) {
        Logger logger = Logger.getLogger(className);
        StreamHandler sh = null;
        SimpleFormatter formatter = new SimpleFormatter();
        File logDir = new File(dirPath);
        logDir.mkdirs();
        try {
            sh = new StreamHandler(new FileOutputStream(new File(logDir, className)), formatter);
            sh.setLevel(level);
            logger.addHandler(sh);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        logger.setLevel(level);
        return logger;
    }

    public static Logger getConsoleLogger(String className, Level level) {
        Logger logger = Logger.getLogger(className);
        SimpleFormatter formatter = new SimpleFormatter();
        StreamHandler sh = new StreamHandler(System.out, formatter){

            @Override
            public synchronized void publish(LogRecord record) {
                super.publish(record);
                this.flush();
            }

            @Override
            public void close() {
                this.flush();
            }
        };
        sh.setLevel(level);
        logger.addHandler(sh);
        logger.setLevel(level);
        return logger;
    }
}

