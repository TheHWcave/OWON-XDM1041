/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.update;

import java.io.File;

public class DownloadFile {
    private String relativePath;
    private long retrived;
    private long fileLength;
    private File localTempFile;
    private boolean isZip;
    private String destFile;

    public DownloadFile(String destFile, String relativePath) {
        this.relativePath = relativePath;
        this.destFile = destFile;
        this.retrived = 0L;
    }

    public String getDestFile() {
        return this.destFile;
    }

    public boolean isZip() {
        return this.isZip;
    }

    public void setZip(boolean isZip) {
        this.isZip = isZip;
    }

    public String getRelativePath() {
        return this.relativePath;
    }

    public long getRetrived() {
        return this.retrived;
    }

    public void addToRetrived(long incr) {
        this.retrived += incr;
    }

    public long getFileLength() {
        return this.fileLength;
    }

    public void setFileLength(long length) {
        this.fileLength = length;
    }

    public void setLocalTempFile(File localTempFile) {
        this.localTempFile = localTempFile;
    }

    public File getLocalTempFile() {
        return this.localTempFile;
    }

    public String getPercent() {
        if (this.fileLength <= 0L) {
            return "";
        }
        double percent = (double)this.retrived / (double)this.fileLength * 100.0;
        return String.format("%d %%", (int)percent);
    }
}

