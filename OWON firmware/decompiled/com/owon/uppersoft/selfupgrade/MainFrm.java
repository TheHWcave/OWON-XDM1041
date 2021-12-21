/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.events.DisposeEvent
 *  org.eclipse.swt.events.DisposeListener
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.layout.FillLayout
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.selfupgrade;

import com.owon.uppersoft.common.comm.DLEvent;
import com.owon.uppersoft.common.comm.DLListener;
import com.owon.uppersoft.common.comm.job.model.DataItem;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.SendFile;
import com.owon.uppersoft.patch.direct.util.ZipAndDe;
import com.owon.uppersoft.selfupgrade.Entity;
import com.owon.uppersoft.selfupgrade.PatchProcess;
import com.owon.uppersoft.selfupgrade.TxDataItem;
import com.owon.uppersoft.selfupgrade.USBCommunication;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MainFrm
implements DLListener {
    private Combo cobLanguage;
    private Text txtTips;
    private USBCommunication usbCom;
    private Text txtLog;
    public static final Locale[] Locales = new Locale[]{Locale.ENGLISH, Locale.SIMPLIFIED_CHINESE};
    private Shell shell;
    private Button btnStart;
    private Label lbAttention;
    private static final boolean CUSTOM = false;
    private HandleEventRunner handleEventRunner = new HandleEventRunner();
    private Label lbNewVer;
    private Text text;
    private Button btnNewButton;
    private Button btnNewButton_1;

    public Shell getShell() {
        return this.shell;
    }

    public MainFrm() {
        this.createContents();
        this.customizeContents();
    }

    public void open() {
        this.shell.layout();
        this.shell.open();
    }

    protected void createContents() {
        Locale l;
        this.shell = new Shell();
        GridLayout gridLayout = new GridLayout();
        this.shell.setLayout((Layout)gridLayout);
        this.shell.addDisposeListener(new DisposeListener(){

            public void widgetDisposed(DisposeEvent e) {
            }
        });
        this.shell.setSize(568, 298);
        Composite comAttention = new Composite((Composite)this.shell, 0);
        comAttention.setLayoutData((Object)new GridData(4, 4, true, false));
        comAttention.setLayout((Layout)new FillLayout());
        this.lbAttention = new Label(comAttention, 0);
        Group gpTips = new Group((Composite)this.shell, 0);
        gpTips.setLayoutData((Object)new GridData(4, 0x1000000, false, false));
        gpTips.setLayout((Layout)new FillLayout());
        this.txtTips = new Text((Composite)gpTips, 2056);
        Group gpLog = new Group((Composite)this.shell, 0);
        gpLog.setLayout((Layout)new FillLayout());
        gpLog.setLayoutData((Object)new GridData(4, 4, false, true));
        this.txtLog = new Text((Composite)gpLog, 2058);
        Composite comStart = new Composite((Composite)this.shell, 0);
        GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 5;
        comStart.setLayout((Layout)gridLayout_1);
        comStart.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        Label lbLanguage = new Label(comStart, 0);
        lbLanguage.setText("language:");
        this.cobLanguage = new Combo(comStart, 8);
        Locale[] localeArray = Locales;
        int n = Locales.length;
        int n2 = 0;
        while (n2 < n) {
            l = localeArray[n2];
            this.cobLanguage.add(l.getDisplayName(l));
            ++n2;
        }
        l = Locale.getDefault();
        this.cobLanguage.setText(l.getDisplayName(l));
        this.cobLanguage.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                int idx = MainFrm.this.cobLanguage.getSelectionIndex();
                if (idx >= 0) {
                    Locale.setDefault(Locales[idx]);
                }
                MainFrm.this.localize();
            }
        });
        this.btnNewButton = new Button(comStart, 0);
        this.btnNewButton.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                MainFrm.this.startPatch("bundles1", "");
            }
        });
        this.btnNewButton.setText(MsgCenter.getString("MainFrm.btnNewButton.text"));
        this.btnNewButton_1 = new Button(comStart, 0);
        this.btnNewButton_1.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                MainFrm.this.startPatch("bundles2", "");
            }
        });
        this.btnNewButton_1.setText(MsgCenter.getString("MainFrm.btnNewButton_1.text"));
        this.btnStart = new Button(comStart, 0);
        this.btnStart.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        this.btnStart.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                MainFrm.this.enable(false);
                MainFrm.this.start();
            }
        });
        this.localize();
    }

    private void startPatch(String path, String psn) {
        File f = new File(path);
        if (!f.exists() || f.isDirectory()) {
            this.logLn(MsgCenter.getBundle().getString("DI.invalidPath"));
            return;
        }
        ZipAndDe zad = new ZipAndDe();
        List<DataItem> dfs = zad.loadZipFile(f, new File("public.key"), psn, "OSC");
        PatchProcess pp = new PatchProcess(this, this);
        pp.startPatch(dfs);
    }

    protected void customizeContents() {
        this.usbCom = new USBCommunication();
    }

    private void modifyVer(String ver) {
        if (this.usbCom.doGetTxt()) {
            Entity en = this.usbCom.getEntity();
            try {
                en.init();
            }
            catch (Exception exception) {
                this.logLn(MsgCenter.getString("SU.errinit"));
                this.enable(true);
                return;
            }
            this.logLn(String.valueOf(MsgCenter.getString("SU.SN")) + ":" + en.getSn());
            this.logLn(String.valueOf(MsgCenter.getString("SU.curver")) + ":" + en.getVer());
            this.logLn(String.valueOf(MsgCenter.getString("SU.newver")) + ":" + ver);
            en.updateVer(ver);
            if (en.supportOSReboot()) {
                SendFile sf = new SendFile(this);
                sf.reboot();
            }
            this.logLn(MsgCenter.getString("SU.reboot"));
            LinkedList<DataItem> dfs = new LinkedList<DataItem>();
            TxDataItem di = new TxDataItem("tx", 16, en, 16384);
            dfs.add(di);
            PatchProcess pp = new PatchProcess(this, this);
            pp.startPatch(dfs);
        } else {
            this.logLn(MsgCenter.getString("SU." + this.usbCom.getErrKey()));
            this.enable(true);
        }
    }

    private void start() {
        if (this.usbCom.doGetTxt()) {
            Entity en = this.usbCom.getEntity();
            try {
                en.init();
            }
            catch (Exception exception) {
                this.logLn(MsgCenter.getString("SU.errinit"));
                this.enable(true);
                return;
            }
            this.logLn(String.valueOf(MsgCenter.getString("SU.SN")) + ":" + en.getSn());
            String msg = en.getDownloadfile();
            if (msg.startsWith("err")) {
                this.logLn(MsgCenter.getString("SU." + msg));
                this.enable(true);
            } else {
                if (en.supportOSReboot()) {
                    SendFile sf = new SendFile(this);
                    sf.reboot();
                } else {
                    this.logLn(MsgCenter.getString("SU.reboot"));
                }
                this.downloadFile(en, msg);
            }
        } else {
            this.logLn(MsgCenter.getString("SU." + this.usbCom.getErrKey()));
            this.enable(true);
        }
    }

    private void downloadFile(Entity en, String file) {
        File f = new File(file);
        if (!f.exists() || f.isDirectory()) {
            this.logLn(MsgCenter.getBundle().getString("DI.invalidPath"));
            this.enable(true);
            return;
        }
        ZipAndDe zad = new ZipAndDe();
        List<DataItem> dfs = zad.loadZipFile(f, new File("public.key"), "", "osc");
        TxDataItem di = new TxDataItem("tx", 2, en, 16384);
        dfs.add(di);
        PatchProcess pp = new PatchProcess(this, this);
        pp.startPatch(dfs);
    }

    protected void localize() {
        this.shell.setText(MsgCenter.getString("SU.title"));
        this.lbAttention.setText(MsgCenter.getString("SU.attention1"));
        this.btnStart.setText(String.valueOf(MsgCenter.getString("SU.step")) + " 3");
        this.btnNewButton.setText(String.valueOf(MsgCenter.getString("SU.step")) + " 1");
        this.btnNewButton_1.setText(String.valueOf(MsgCenter.getString("SU.step")) + " 2");
    }

    public void logLn(final String s) {
        if (!this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new Runnable(){

                @Override
                public void run() {
                    MainFrm.this.txtLog.append(String.valueOf(s) + "\n");
                    MainFrm.this.txtTips.setText(s);
                }
            });
        }
    }

    public void showTips(final String s) {
        if (!this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new Runnable(){

                @Override
                public void run() {
                    MainFrm.this.txtTips.setText(s);
                }
            });
        }
    }

    public void enable(final boolean b) {
        if (!this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new Runnable(){

                @Override
                public void run() {
                    MainFrm.this.btnStart.setEnabled(b);
                }
            });
        }
    }

    @Override
    public void handleEvent(DLEvent e) {
        this.handleEventRunner.setDLEvent(e);
        this.shell.getDisplay().syncExec((Runnable)this.handleEventRunner);
    }

    private class HandleEventRunner
    implements Runnable {
        private DLEvent e;
        private int range = -1;

        public void setDLEvent(DLEvent e) {
            this.e = e;
        }

        @Override
        public void run() {
            int value = this.e.intValue;
            ResourceBundle bundle = MsgCenter.getBundle();
            switch (this.e.type) {
                case 1: {
                    double r = (double)value * 100.0 / (double)this.range;
                    String type = (String)this.e.data;
                    if (type == null) {
                        type = "next";
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Sending ");
                    sb.append(type);
                    sb.append(" ");
                    sb.append(String.format("%.2f %%", r));
                    sb.append("...");
                    MainFrm.this.showTips(sb.toString());
                    break;
                }
                case 2: {
                    if (value <= 0) break;
                    this.range = value;
                    break;
                }
                case 3: {
                    MainFrm.this.logLn(bundle.getString("DI.finish"));
                    break;
                }
                case 4: {
                    System.out.println(String.valueOf(value) + "kB/s");
                    break;
                }
                case 5: {
                    System.out.print(this.e.data + " ");
                    break;
                }
                case 6: {
                    byte[] commands = (byte[])this.e.data;
                    int length = commands.length;
                    int i = 0;
                    while (i < length) {
                        System.out.print(String.valueOf(Integer.toHexString(commands[i] & 0xFF)) + " ");
                        ++i;
                    }
                    System.out.println();
                }
            }
        }
    }
}

