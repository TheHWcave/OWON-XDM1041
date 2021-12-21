/*
 * Decompiled with CFR 0.152.
 */
package com.owon.uppersoft.common.update;

import com.owon.uppersoft.common.update.DownloadFile;
import com.owon.uppersoft.common.update.UpdateFrame;
import com.owon.uppersoft.common.utils.FileUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class UpdateTask
implements Runnable {
    private byte[] buf = new byte[10240];
    private boolean isCancel = false;
    private UpdateFrame frame;
    private List<DownloadFile> files;
    private String url;

    public UpdateTask(UpdateFrame frame, String url, List<DownloadFile> files) {
        this.frame = frame;
        this.url = url;
        this.files = files;
    }

    @Override
    public void run() {
        try {
            URL server = new URL(this.url);
            for (DownloadFile file : this.files) {
                if (this.isCancel) {
                    return;
                }
                if (this.getFile(server, file)) continue;
                this.frame.downloadFailed();
                return;
            }
            if (this.isCancel) {
                return;
            }
            this.frame.downloadFinished();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean getFile(URL server, DownloadFile downloadFile) {
        String relativePath = downloadFile.getRelativePath();
        try {
            BufferedInputStream bis = null;
            HttpURLConnection httpUrl = null;
            URL url = new URL(server, downloadFile.getRelativePath());
            httpUrl = (HttpURLConnection)url.openConnection();
            this.frame.setCurrentDownloadFile(downloadFile, url);
            File localFile = new File(this.frame.getDownloadTempDir(), relativePath);
            httpUrl.setReadTimeout(1000);
            httpUrl.connect();
            String sHeader = httpUrl.getHeaderField("Content-Length");
            if (sHeader == null) {
                return false;
            }
            long fileLength = Long.parseLong(sHeader);
            if (fileLength <= 0L) {
                return false;
            }
            downloadFile.setFileLength(fileLength);
            long receive = 0L;
            FileUtil.checkPath(localFile);
            RandomAccessFile raf = new RandomAccessFile(localFile, "rw");
            raf.seek(receive);
            int size = 0;
            bis = new BufferedInputStream(httpUrl.getInputStream());
            while (true) {
                if ((size = bis.read(this.buf)) == -1) {
                    raf.close();
                    bis.close();
                    httpUrl.disconnect();
                    if (downloadFile.getRetrived() >= fileLength) break;
                    return false;
                }
                if (this.isCancel) {
                    raf.close();
                    bis.close();
                    httpUrl.disconnect();
                    this.onCancel(localFile);
                    return false;
                }
                raf.write(this.buf, 0, size);
                downloadFile.addToRetrived(size);
                this.frame.updateProgress();
                Thread.sleep(0L);
            }
            downloadFile.setLocalTempFile(localFile);
            return true;
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void backTransmitFromDisturb(HttpURLConnection httpUrl, long offset) {
        httpUrl.setRequestProperty("User-Agent", "NetFox");
        httpUrl.setRequestProperty("RANGE", "bytes=" + offset + "-");
    }

    private void onCancel(File file) {
        file.delete();
    }

    public void setCancel() {
        this.isCancel = true;
    }

    public boolean isStop() {
        return this.isCancel;
    }
}

