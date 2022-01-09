/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.core.runtime.IProgressMonitor
 *  org.eclipse.jface.dialogs.MessageDialog
 *  org.eclipse.jface.dialogs.ProgressMonitorDialog
 *  org.eclipse.jface.operation.IRunnableWithProgress
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.common.update;

import com.owon.uppersoft.common.i18n.CommonMessageLib;
import com.owon.uppersoft.common.update.DownloadFile;
import com.owon.uppersoft.common.update.IUpdatable;
import com.owon.uppersoft.common.update.UpdateDetection;
import com.owon.uppersoft.common.update.UpdateFrame;
import com.owon.uppersoft.common.utils.FileUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ResourceBundle;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

public class CheckUpdateFrame
extends ProgressMonitorDialog
implements IRunnableWithProgress {
    private IUpdatable iu;
    private ResourceBundle bundle;
    private Runnable r;

    public CheckUpdateFrame(IUpdatable iu) {
        super(iu.getMainShell());
        this.iu = iu;
        this.bundle = CommonMessageLib.getDefaultResourceBundle();
        this.setCancelable(true);
        try {
            this.run(true, true, this);
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        catch (InterruptedException interruptedException) {}
    }

    public void run(IProgressMonitor monitor) throws InvocationTargetException {
        block8: {
            try {
                List<DownloadFile> urls;
                monitor.beginTask("", 100);
                this.onCancel(monitor, "Info.DetectServers", 5);
                final UpdateDetection ud = new UpdateDetection(this.iu);
                final String url = ud.detectServers();
                if (url == null) {
                    this.onTerminated(monitor, "Error.InternetConnection");
                }
                this.onCancel(monitor, "Info.CompareVersion", 20);
                boolean flag = ud.isUpdate();
                this.onCancel(monitor, "", 40);
                if (!flag) {
                    this.onTerminated(monitor, "Info.DownManual");
                }
                if ((urls = ud.getDownloadFileURLs()).size() == 0) {
                    this.onCancel(monitor, "", 60);
                    FileUtil.deleteFile(ud.getLocalUpdateXML());
                    this.onCancel(monitor, "", 80);
                    this.onTerminated(monitor, "Info.NewestVersion");
                    break block8;
                }
                this.onCancel(monitor, "Info.PromptUpdate", 80);
                this.r = new Runnable(){

                    @Override
                    public void run() {
                        new UpdateFrame(ud, url, urls).open();
                    }
                };
                return;
            }
            catch (Exception exception) {
            }
            finally {
                monitor.done();
            }
        }
    }

    public boolean close() {
        Shell s;
        boolean b = super.close();
        if (this.r != null && (s = this.iu.getMainShell()) != null) {
            s.getDisplay().syncExec(this.r);
        }
        return b;
    }

    protected void onTerminated(IProgressMonitor monitor, final String key) throws Exception {
        this.r = new Runnable(){

            @Override
            public void run() {
                MessageDialog.openInformation((Shell)CheckUpdateFrame.this.getShell(), (String)"", (String)CheckUpdateFrame.this.bundle.getString(key));
            }
        };
        throw new Exception();
    }

    protected void onCancel(IProgressMonitor monitor, String key, int value) throws Exception {
        monitor.worked(value);
        if (key != null && key.length() != 0) {
            monitor.setTaskName(this.bundle.getString(key));
            monitor.subTask(this.bundle.getString(key));
        }
        if (monitor.isCanceled()) {
            this.r = null;
            throw new Exception();
        }
    }
}

