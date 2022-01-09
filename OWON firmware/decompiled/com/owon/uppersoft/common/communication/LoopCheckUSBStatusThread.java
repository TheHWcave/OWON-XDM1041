/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.common.communication;

import com.owon.uppersoft.common.communication.IUSBStatus;
import com.owon.uppersoft.common.communication.PopupWindow;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class LoopCheckUSBStatusThread
implements Runnable {
    private boolean loop;
    private Display display;
    private UpdateUSBStatus updateUSBStatus;
    private boolean status;
    private IUSBStatus usbStatus;
    private PopupWindow popup;
    private Thread t;

    public LoopCheckUSBStatusThread(Shell shell, IUSBStatus usbStatus) {
        this.display = shell.getDisplay();
        this.usbStatus = usbStatus;
        this.updateUSBStatus = new UpdateUSBStatus();
        this.popup = new PopupWindow(shell);
    }

    public void turnOnCheck() {
        if (this.t != null && this.t.isAlive()) {
            return;
        }
        this.loop = true;
        this.t = new Thread(this);
        this.t.start();
    }

    public void turnOffCheck() {
        if (this.t == null || !this.t.isAlive()) {
            return;
        }
        this.loop = false;
        this.t.stop();
    }

    @Override
    public void run() {
        this.status = this.usbStatus.checkStatus();
        this.display.asyncExec((Runnable)this.updateUSBStatus);
        if (this.usbStatus.shouldPopupCheck()) {
            this.popup.updateStatus(this.status);
        }
        boolean t = this.status;
        while (this.loop) {
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.status = this.usbStatus.checkStatus();
            if (t == this.status) continue;
            this.display.asyncExec((Runnable)this.updateUSBStatus);
            if (this.usbStatus.shouldPopupCheck()) {
                this.popup.updateStatus(this.status);
            }
            t = this.status;
        }
    }

    class UpdateUSBStatus
    implements Runnable {
        UpdateUSBStatus() {
        }

        @Override
        public void run() {
            LoopCheckUSBStatusThread.this.usbStatus.usbStatus(LoopCheckUSBStatusThread.this.status);
        }
    }
}

