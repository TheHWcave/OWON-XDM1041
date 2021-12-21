/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.dialogs.MessageDialog
 *  org.eclipse.swt.events.DisposeEvent
 *  org.eclipse.swt.events.DisposeListener
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.omap;

import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.omap.ScriptEntry;
import com.owon.uppersoft.omap.SendBin;
import com.owon.uppersoft.patch.direct.serial.SerialPortUtil;
import com.owon.uppersoft.tftp.tftpClient;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MainFrm {
    public static final Locale[] Locales = new Locale[]{Locale.ENGLISH, Locale.SIMPLIFIED_CHINESE};
    private Shell shell;
    private ScriptEntry downloadSe;
    private ScriptEntry spq4chbootSe;
    private SendBin sb;
    private Combo combo;
    private Label lbPort;
    private Text text_1;
    private Button btnStart;
    private String port;
    private Composite composite_1;
    private Label lbIP;
    private Text text_2;
    private Label lbGate;
    private Label lbSvr;
    private Text text_3;
    private Text text_4;
    private Combo combo_1;
    private Combo combo_2;
    private Label lbSeril;
    private Text text;
    private Button btnAutoAdd;
    private Button btnErase;

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

    public MainFrm getFrm() {
        return this;
    }

    protected void createContents() {
        this.shell = new Shell();
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        this.shell.setLayout((Layout)gridLayout);
        this.shell.addDisposeListener(new DisposeListener(){

            public void widgetDisposed(DisposeEvent e) {
                MainFrm.this.release();
            }
        });
        this.shell.setSize(600, 500);
        Composite composite = new Composite((Composite)this.shell, 0);
        composite.setLayout((Layout)new GridLayout(3, false));
        composite.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.lbIP = new Label(composite, 0);
        this.lbIP.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        this.text_2 = new Text(composite, 2048);
        this.text_2.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        new Label(composite, 0);
        this.lbGate = new Label(composite, 0);
        this.lbGate.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        this.text_3 = new Text(composite, 2048);
        this.text_3.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        new Label(composite, 0);
        this.lbSvr = new Label(composite, 0);
        this.lbSvr.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        this.text_4 = new Text(composite, 2048);
        this.text_4.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        new Label(composite, 0);
        this.lbSeril = new Label(composite, 0);
        this.lbSeril.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        this.text = new Text(composite, 2048);
        this.text.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.btnAutoAdd = new Button(composite, 32);
        this.btnAutoAdd.setSelection(true);
        this.btnAutoAdd.setVisible(false);
        this.composite_1 = new Composite((Composite)this.shell, 0);
        this.composite_1.setLayoutData((Object)new GridData(4, 4, false, true, 1, 1));
        this.composite_1.setLayout((Layout)new GridLayout(6, false));
        this.lbPort = new Label(this.composite_1, 0);
        this.lbPort.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        this.lbPort.setText(MsgCenter.getString("MainFrm.lblNewLabel.text_1"));
        this.combo = new Combo(this.composite_1, 8);
        this.combo.setLayoutData((Object)new GridData(4, 0x1000000, false, false, 1, 1));
        this.combo_2 = new Combo(this.composite_1, 8);
        this.combo_2.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.combo_1 = new Combo(this.composite_1, 8);
        this.combo_1.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.btnStart = new Button(this.composite_1, 0);
        this.btnStart.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                MainFrm.this.prograss("u-boot.bin");
            }
        });
        this.btnErase = new Button(this.composite_1, 0);
        this.btnErase.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                MainFrm.this.prograss("eraseall.bin");
            }
        });
        this.text_1 = new Text(this.composite_1, 2568);
        GridData gd_text_1 = new GridData(4, 4, true, true, 6, 1);
        gd_text_1.widthHint = 234;
        this.text_1.setLayoutData((Object)gd_text_1);
        this.localize();
        this.initPort();
        this.initCombo();
        this.initIP();
    }

    private void prograss(String binFile) {
        boolean b;
        if (this.combo_1.getSelectionIndex() == -1 || this.combo_2.getSelectionIndex() == -1) {
            MessageDialog.openError((Shell)this.shell, (String)"", (String)"version no selected");
            return;
        }
        String ipaddr = this.text_2.getText();
        String gatewayip = this.text_3.getText();
        String serverip = this.text_4.getText();
        String serial = this.text.getText();
        String type = this.combo_2.getText();
        String ver = this.combo_1.getText();
        String path = String.valueOf(type) + "/" + ver;
        String fullVer = String.valueOf(type) + "-" + ver;
        boolean bl = b = tftpClient.getFile(serverip, String.valueOf(type) + "/download/" + binFile, "b") && tftpClient.getFile(serverip, String.valueOf(path) + "/spq4chboot.txt", "s") && tftpClient.getFile(serverip, String.valueOf(path) + "/download.txt", "d") && tftpClient.getFile(serverip, String.valueOf(type) + "/download/evm.bin", "e");
        if (!b) {
            this.logLn("check tftp server is running then restart\n");
            return;
        }
        this.downloadSe = new ScriptEntry(new File("d"));
        this.spq4chbootSe = new ScriptEntry(new File("s"));
        this.downloadSe.update(ipaddr, gatewayip, serverip, serial, path, type, fullVer);
        this.spq4chbootSe.update(ipaddr, gatewayip, serverip, serial, path, type, fullVer);
        this.release();
        this.text_1.setText("");
        this.logLn("START...");
        this.port = this.combo.getText();
        this.sb = new SendBin(this.port, this.getFrm());
        this.sb.sendFiles();
    }

    private boolean serialAdd(String s) {
        int len = s.length();
        try {
            int n = Integer.parseInt(s.substring(len - 4)) + 1;
            String end = Integer.toString(n);
            int i = end.length();
            while (i < 4) {
                end = "0" + end;
                ++i;
            }
            this.text.setText(String.valueOf(s.substring(0, len - 4)) + end);
            return true;
        }
        catch (Exception exception) {
            MessageDialog.openError((Shell)this.shell, (String)"", (String)"last 4 char is not digit");
            return false;
        }
    }

    private void initIP() {
        ScriptEntry initSe = new ScriptEntry(new File("download.txt"));
        this.text_2.setText(initSe.get("ipaddr"));
        this.text_3.setText(initSe.get("gatewayip"));
        this.text_4.setText(initSe.get("serverip"));
    }

    private void initCombo() {
        try {
            FileReader fr = new FileReader("omap.txt");
            BufferedReader br = new BufferedReader(fr);
            this.combo_2.setItems(br.readLine().split(","));
            this.combo_1.setItems(br.readLine().split(","));
            br.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initPort() {
        List<String> list = SerialPortUtil.loadAvailablePort();
        String[] coms = new String[list.size()];
        this.combo.setItems(list.toArray(coms));
        this.combo.select(0);
    }

    protected void customizeContents() {
    }

    protected void localize() {
        this.shell.setText(String.valueOf(MsgCenter.getString("OM.title")) + "1.9.9");
        this.lbPort.setText(MsgCenter.getString("MainFrm.splab"));
        this.btnStart.setText(MsgCenter.getString("MainFrm.start"));
        this.btnErase.setText(MsgCenter.getString("MainFrm.erase"));
        this.lbIP.setText(MsgCenter.getString("OM.ip"));
        this.lbGate.setText(MsgCenter.getString("OM.gate"));
        this.lbSvr.setText(MsgCenter.getString("OM.svr"));
        this.lbSeril.setText(MsgCenter.getString("OM.seril"));
        this.btnAutoAdd.setText(MsgCenter.getString("OM.autoadd"));
    }

    public void logLn(final String s) {
        if (!this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new Runnable(){

                @Override
                public void run() {
                    MainFrm.this.text_1.append(s);
                }
            });
        }
    }

    public void enableBtn(final boolean b) {
        System.out.println(b);
        if (!this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new Runnable(){

                @Override
                public void run() {
                    MainFrm.this.btnStart.setEnabled(b);
                }
            });
        }
    }

    private void release() {
        if (this.sb != null) {
            this.sb.close();
            this.sb = null;
        }
    }

    public ScriptEntry getScriptEntry(int index) {
        if (index == 3) {
            return this.downloadSe;
        }
        return this.spq4chbootSe;
    }
}

