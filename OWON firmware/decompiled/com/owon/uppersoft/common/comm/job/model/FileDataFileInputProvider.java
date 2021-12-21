/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.comm.job.model;

import com.owon.uppersoft.common.comm.job.model.InputProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

public class FileDataFileInputProvider
implements InputProvider {
    private File file;
    private FileInputStream fis;

    public FileDataFileInputProvider(File file) {
        this.setFile(file);
    }

    @Override
    public InputStream openInput() {
        this.closeInput();
        try {
            this.fis = new FileInputStream(this.file);
            return this.fis;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPath() {
        return this.file.getPath();
    }

    @Override
    public long getFileLength() {
        return this.file.length();
    }

    public FileChannel openChannel() {
        return ((FileInputStream)this.openInput()).getChannel();
    }

    @Override
    public void closeInput() {
        if (this.fis != null) {
            try {
                this.fis.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.fis = null;
    }
}

