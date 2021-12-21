/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.Listener
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Widget
 */
package com.owon.uppersoft.common.commjob.instance;

import com.owon.uppersoft.common.commjob.instance.IDevice;
import com.owon.uppersoft.common.commjob.instance.PrototypePortSettingFrame;
import java.util.Locale;
import javax.security.auth.login.Configuration;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

public class PortSetting
extends PrototypePortSettingFrame
implements Listener {
    protected Configuration config;
    protected String[] portType;
    protected String parityNone;
    protected final int SWT_Selection = 13;
    private SelectPortRunner selectPortRunner = new SelectPortRunner();
    private IDevice[] ids;

    public void handleEvent(Event event) {
        if (event.type != 13) {
            return;
        }
        Widget o = event.widget;
        if (o == this.btnOK) {
            return;
        }
        if (o == this.refreshButton) {
            this.refreshUSBPort();
            return;
        }
        if (o == this.portTypeCombo) {
            this.portTypeCombo.getText();
            return;
        }
    }

    public PortSetting(Shell shell, Configuration config) {
        super(shell);
        this.config = config;
        PortSetting listener = this;
        this.btnOK.addListener(13, (Listener)listener);
        this.refreshButton.addListener(13, (Listener)listener);
        this.portTypeCombo.setText("loading...");
        this.portTypeCombo.addListener(13, (Listener)listener);
        new Thread(){

            @Override
            public void run() {
                PortSetting.this.refreshUSBPort();
            }
        }.start();
    }

    private void refreshUSBPort() {
    }

    public static void main_hide(String[] args) {
        try {
            Locale.setDefault(Locale.ENGLISH);
            new Shell();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    class SelectPortRunner
    implements Runnable {
        SelectPortRunner() {
        }

        @Override
        public void run() {
            PortSetting.this.usbCombo.removeAll();
            int length = PortSetting.this.ids.length;
            int i = 0;
            while (i < length) {
                String sn = PortSetting.this.ids[i].getSerialNumber();
                if (sn == null) {
                    sn = "?";
                }
                PortSetting.this.usbCombo.add(String.valueOf(i + 1) + ". (SN: " + sn + ")");
                ++i;
            }
            PortSetting.this.usbCombo.select(0);
        }
    }
}

