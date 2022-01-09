/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.custom.StackLayout
 *  org.eclipse.swt.events.FocusAdapter
 *  org.eclipse.swt.events.FocusEvent
 *  org.eclipse.swt.events.FocusListener
 *  org.eclipse.swt.events.KeyAdapter
 *  org.eclipse.swt.events.KeyEvent
 *  org.eclipse.swt.events.KeyListener
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.layout.RowLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.commjob.instance;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PrototypePortSettingFrame {
    protected Text txtPort;
    protected Text txtIP;
    protected Composite serialInfoCom;
    protected Composite usbInfoCom;
    protected Composite lanInfoCom;
    protected Label serialInfoLabel;
    protected Composite infoCom;
    protected Label availablePortsLabel;
    protected Button refreshButton;
    protected Combo usbCombo;
    protected Label stopbitsLabel;
    protected Label parityLabel;
    protected Label dataBitsLabel;
    protected Label baudLabel;
    protected Combo stopBitsCombo;
    protected Combo parityCombo;
    protected Combo dataBitsCombo;
    protected Combo baudRateCombo;
    protected Label portTypeLabel;
    protected Combo portTypeCombo;
    protected Button btnOK;
    protected Shell shell;
    protected StackLayout stackLayout;
    protected Label label;
    protected Composite portCom;

    public Shell getShell() {
        return this.shell;
    }

    public PrototypePortSettingFrame(Shell parent) {
        this.shell = new Shell(parent, 67680);
        this.createContents();
    }

    public Object open() {
        this.shell.pack();
        this.shell.open();
        Display display = this.shell.getDisplay();
        while (!this.shell.isDisposed()) {
            if (display.readAndDispatch()) continue;
            display.sleep();
        }
        return null;
    }

    protected void createContents() {
        GridLayout gridLayout = new GridLayout();
        this.shell.setLayout((Layout)gridLayout);
        this.portCom = new Composite((Composite)this.shell, 0);
        GridLayout gridLayout_3 = new GridLayout();
        gridLayout_3.numColumns = 2;
        this.portCom.setLayout((Layout)gridLayout_3);
        GridData gd_portCom = new GridData(4, 4, true, false);
        this.portCom.setLayoutData((Object)gd_portCom);
        this.portTypeLabel = new Label(this.portCom, 0);
        GridData gd_portTypeLabel = new GridData();
        gd_portTypeLabel.horizontalIndent = 5;
        this.portTypeLabel.setLayoutData((Object)gd_portTypeLabel);
        this.portTypeCombo = new Combo(this.portCom, 8);
        this.portTypeCombo.setLayoutData((Object)new GridData());
        this.infoCom = new Composite(this.portCom, 0);
        GridData gd_infoCom = new GridData(4, 0x1000000, true, false, 2, 1);
        this.infoCom.setLayoutData((Object)gd_infoCom);
        this.stackLayout = new StackLayout();
        this.infoCom.setLayout((Layout)this.stackLayout);
        this.usbInfoCom = new Composite(this.infoCom, 0);
        GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 4;
        this.usbInfoCom.setLayout((Layout)gridLayout_1);
        this.availablePortsLabel = new Label(this.usbInfoCom, 0);
        this.availablePortsLabel.setText("");
        this.usbCombo = new Combo(this.usbInfoCom, 8);
        this.usbCombo.setLayoutData((Object)new GridData(120, -1));
        this.refreshButton = new Button(this.usbInfoCom, 0);
        this.refreshButton.setLayoutData((Object)new GridData(80, -1));
        this.lanInfoCom = new Composite(this.infoCom, 0);
        GridLayout gridLayout_5 = new GridLayout();
        gridLayout_5.numColumns = 4;
        this.lanInfoCom.setLayout((Layout)gridLayout_5);
        Label labIP = new Label(this.lanInfoCom, 0);
        labIP.setText("IP:");
        this.txtIP = new Text(this.lanInfoCom, 2048);
        this.txtIP.addFocusListener((FocusListener)new FocusAdapter(){

            public void focusLost(FocusEvent e) {
                String p = PrototypePortSettingFrame.this.txtIP.getText();
                String m = "^(22[0-3]|2[0-1]\\d|1\\d{2}|[1-9]\\d|[1-9])\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)$";
                p.matches(m);
            }
        });
        this.txtIP.addKeyListener((KeyListener)new KeyAdapter(){

            public void keyPressed(KeyEvent e) {
                if ("0123456789.".indexOf(e.character) < 0) {
                    if (e.keyCode == 8 || e.keyCode == 127) {
                        return;
                    }
                    e.doit = false;
                }
            }
        });
        GridData gd_txtIP = new GridData(4, 0x1000000, false, false);
        gd_txtIP.widthHint = 134;
        this.txtIP.setLayoutData((Object)gd_txtIP);
        Label labPort = new Label(this.lanInfoCom, 0);
        labPort.setLayoutData((Object)new GridData());
        labPort.setText("port:");
        this.txtPort = new Text(this.lanInfoCom, 2048);
        this.txtPort.addKeyListener((KeyListener)new KeyAdapter(){

            public void keyPressed(KeyEvent e) {
                if ("0123456789".indexOf(e.character) < 0) {
                    if (e.keyCode == 8 || e.keyCode == 127) {
                        return;
                    }
                    e.doit = false;
                }
            }
        });
        GridData gd_txtPort = new GridData(4, 0x1000000, false, false);
        gd_txtPort.widthHint = 54;
        this.txtPort.setLayoutData((Object)gd_txtPort);
        this.serialInfoCom = new Composite(this.infoCom, 0);
        GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 8;
        this.serialInfoCom.setLayout((Layout)gridLayout_2);
        this.baudLabel = new Label(this.serialInfoCom, 0);
        this.baudRateCombo = new Combo(this.serialInfoCom, 8);
        this.dataBitsLabel = new Label(this.serialInfoCom, 0);
        this.dataBitsCombo = new Combo(this.serialInfoCom, 8);
        this.parityLabel = new Label(this.serialInfoCom, 0);
        this.parityCombo = new Combo(this.serialInfoCom, 8);
        this.stopbitsLabel = new Label(this.serialInfoCom, 0);
        this.stopBitsCombo = new Combo(this.serialInfoCom, 8);
        Label label_1 = new Label(this.usbInfoCom, 0);
        label_1.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        this.stackLayout.topControl = this.lanInfoCom;
        Composite btnBar = new Composite((Composite)this.shell, 0);
        RowLayout rowLayout = new RowLayout();
        rowLayout.pack = false;
        rowLayout.fill = true;
        rowLayout.spacing = 15;
        btnBar.setLayout((Layout)rowLayout);
        GridData gd_btnBar = new GridData(131072, 4, true, false);
        btnBar.setLayoutData((Object)gd_btnBar);
        this.btnOK = new Button(btnBar, 0);
    }

    protected void customCMDGroupSDS() {
    }

    public static void main_hide(String[] args) {
        try {
            Shell s = new Shell();
            PrototypePortSettingFrame ps = new PrototypePortSettingFrame(s);
            s.open();
            ps.open();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

