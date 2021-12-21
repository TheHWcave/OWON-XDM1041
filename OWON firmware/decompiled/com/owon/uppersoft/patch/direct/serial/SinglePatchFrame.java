/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.events.DisposeEvent
 *  org.eclipse.swt.events.DisposeListener
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.graphics.Point
 *  org.eclipse.swt.graphics.Rectangle
 *  org.eclipse.swt.layout.FillLayout
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.patch.direct.serial;

import com.owon.uppersoft.common.comm.DLEvent;
import com.owon.uppersoft.common.comm.DLListener;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.USBConfirm;
import com.owon.uppersoft.patch.direct.serial.PatchProcess;
import com.owon.uppersoft.patch.direct.serial.SerialPortUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SinglePatchFrame
implements DLListener {
    private Label lbModel;
    private Label plbl;
    private Button startPatchButton;
    private Text txtSN;
    public static final Locale[] Locales = new Locale[]{Locale.ENGLISH, Locale.SIMPLIFIED_CHINESE};
    private Combo combo_lan;
    private Button btnUSB;
    private Text info;
    private Combo combo;
    private Text patchText;
    private Shell shell;
    private Label comLabel;
    private String filePath = "";
    private PatchProcess pp;
    private ResourceBundle bundle;
    private String usbok;
    private String usbbad;
    private boolean usbgood = false;
    private HandleEventRunner handleEventRunner = new HandleEventRunner();
    private String machineType;

    public Shell getShell() {
        return this.shell;
    }

    public static void main(String[] args) {
        try {
            SinglePatchFrame window = new SinglePatchFrame();
            window.open();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open() {
        Display display = Display.getDefault();
        this.createContents();
        this.shell.pack();
        Rectangle r = this.shell.getDisplay().getBounds();
        Point p = this.shell.getSize();
        this.shell.setLocation(r.width - p.x >> 1, r.height - p.y >> 1);
        this.shell.open();
        while (!this.shell.isDisposed()) {
            if (display.readAndDispatch()) continue;
            display.sleep();
        }
    }

    protected void createContents() {
        this.shell = new Shell();
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        this.shell.setLayout((Layout)gridLayout);
        Composite titleCom = new Composite((Composite)this.shell, 0);
        titleCom.setLayout((Layout)new FillLayout());
        GridData gd_titleCom = new GridData(4, 4, true, false);
        gd_titleCom.widthHint = 600;
        gd_titleCom.heightHint = 70;
        titleCom.setLayoutData((Object)gd_titleCom);
        this.patchText = new Text(titleCom, 74);
        Composite operateCom = new Composite((Composite)this.shell, 0);
        operateCom.setLayoutData((Object)new GridData(4, 4, true, false));
        GridLayout gl_operateCom = new GridLayout();
        gl_operateCom.numColumns = 5;
        operateCom.setLayout((Layout)gl_operateCom);
        Label languageLabel = new Label(operateCom, 0);
        languageLabel.setLayoutData((Object)new GridData());
        languageLabel.setText("Language: ");
        this.combo_lan = new Combo(operateCom, 8);
        this.combo_lan.setLayoutData((Object)new GridData(4, 0x1000000, false, false));
        this.comLabel = new Label(operateCom, 131072);
        GridData gd_comLabel = new GridData(4, 0x1000000, false, false);
        gd_comLabel.widthHint = 60;
        this.comLabel.setLayoutData((Object)gd_comLabel);
        this.combo = new Combo(operateCom, 8);
        this.combo.setLayoutData((Object)new GridData(4, 0x1000000, false, false, 1, 1));
        this.btnUSB = new Button(operateCom, 0);
        GridData gd_btnUSB = new GridData(200, -1);
        gd_btnUSB.horizontalAlignment = 4;
        this.btnUSB.setLayoutData((Object)gd_btnUSB);
        Composite infoCom = new Composite((Composite)this.shell, 0);
        infoCom.setLayout((Layout)new FillLayout());
        GridData gd_infoCom = new GridData(4, 4, true, true);
        gd_infoCom.heightHint = 30;
        infoCom.setLayoutData((Object)gd_infoCom);
        this.info = new Text(infoCom, 74);
        this.plbl = new Label(operateCom, 0);
        GridData gd_plbl = new GridData(16384, 0x1000000, false, false, 2, 1);
        gd_plbl.widthHint = 250;
        this.plbl.setLayoutData((Object)gd_plbl);
        this.lbModel = new Label(operateCom, 0);
        GridData gd_lbModel = new GridData();
        this.lbModel.setLayoutData((Object)gd_lbModel);
        this.txtSN = new Text(operateCom, 2048);
        GridData gd_txtSN = new GridData(4, 0x1000000, true, false);
        this.txtSN.setLayoutData((Object)gd_txtSN);
        this.txtSN.setVisible(false);
        this.startPatchButton = new Button(operateCom, 0);
        GridData gd_startPatchButton = new GridData(4, 0x1000000, false, false);
        gd_startPatchButton.widthHint = 230;
        this.startPatchButton.setLayoutData((Object)gd_startPatchButton);
        this.loadPorts();
        this.setModel();
        this.txtSN.setFocus();
    }

    private void setModel() {
        File f = new File("model");
        if (f.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                this.setMachineType(br.readLine().trim());
                this.lbModel.setText(br.readLine().trim());
                br.close();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadPorts() {
        Locale l;
        this.pp = new PatchProcess(this, this);
        this.localize();
        List<String> list = SerialPortUtil.loadAvailablePort();
        String[] coms = new String[list.size()];
        this.combo.setItems(list.toArray(coms));
        this.combo.select(0);
        this.btnUSB.setText(this.usbbad);
        Locale[] localeArray = Locales;
        int n = Locales.length;
        int n2 = 0;
        while (n2 < n) {
            l = localeArray[n2];
            this.combo_lan.add(l.getDisplayName(l));
            ++n2;
        }
        l = Locale.getDefault();
        this.combo_lan.setText(l.getDisplayName(l));
        this.combo_lan.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                int idx = SinglePatchFrame.this.combo_lan.getSelectionIndex();
                if (idx >= 0) {
                    Locale.setDefault(Locales[idx]);
                }
                SinglePatchFrame.this.localize();
            }
        });
        this.btnUSB.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                USBConfirm.reinstallUSB(SinglePatchFrame.this.shell, SinglePatchFrame.this.bundle);
            }
        });
        this.shell.addDisposeListener(new DisposeListener(){

            public void widgetDisposed(DisposeEvent e) {
                SinglePatchFrame.this.pp.closeCOM();
            }
        });
        this.startPatchButton.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                String com;
                if (SinglePatchFrame.this.txtSN.isVisible()) {
                    String txtName = SinglePatchFrame.this.txtSN.getText().trim();
                    if (txtName.length() < 7) {
                        SinglePatchFrame.this.setInfo(SinglePatchFrame.this.bundle.getString("DI.invalidPath"));
                        return;
                    }
                    String fileFolder = txtName.substring(0, 4);
                    String fileName = txtName.substring(4);
                    SinglePatchFrame.this.filePath = "txt\\" + fileFolder + "\\" + fileName + ".txt";
                    File f = new File(SinglePatchFrame.this.filePath);
                    if (!f.exists()) {
                        SinglePatchFrame.this.setInfo(SinglePatchFrame.this.bundle.getString("DI.invalidPath"));
                        return;
                    }
                } else {
                    SinglePatchFrame.this.filePath = "";
                }
                if ((com = SinglePatchFrame.this.combo.getText().trim()).length() == 0) {
                    return;
                }
                Thread t = new Thread(new Runnable(){

                    @Override
                    public void run() {
                        SinglePatchFrame.this.pp.startPatch(com, SinglePatchFrame.this.machineType, SinglePatchFrame.this.filePath);
                    }
                });
                t.start();
            }
        });
    }

    public ResourceBundle getBundle() {
        return this.bundle;
    }

    protected void localize() {
        this.bundle = MsgCenter.getBundle();
        this.usbok = this.bundle.getString("DI.usbok");
        this.usbbad = this.bundle.getString("DI.usbbad");
        this.shell.setText(String.valueOf(this.bundle.getString("DI.title")) + " " + "1.9.9");
        this.usbok = this.bundle.getString("DI.usbok");
        this.usbbad = this.bundle.getString("DI.usbbad");
        this.btnUSB.setText(this.usbgood ? this.usbok : this.usbbad);
        this.comLabel.setText(this.bundle.getString("DI.port"));
        this.patchText.setText(this.bundle.getString("DI.titleInfo"));
        this.startPatchButton.setText(this.bundle.getString("DI.update"));
        this.plbl.setText(this.bundle.getString("DI.plbl"));
    }

    @Override
    public void handleEvent(DLEvent e) {
        this.handleEventRunner.setDLEvent(e);
        this.shell.getDisplay().syncExec((Runnable)this.handleEventRunner);
    }

    public void appendInfo(final String txt) {
        this.shell.getDisplay().asyncExec(new Runnable(){

            @Override
            public void run() {
                SinglePatchFrame.this.info.append(txt);
            }
        });
    }

    public void setInfo(final String txt) {
        this.shell.getDisplay().asyncExec(new Runnable(){

            @Override
            public void run() {
                SinglePatchFrame.this.info.setText(txt);
            }
        });
    }

    public void setPatchButtonEnable(final boolean enabled) {
        this.shell.getDisplay().asyncExec(new Runnable(){

            @Override
            public void run() {
                SinglePatchFrame.this.txtSN.setText("");
                SinglePatchFrame.this.startPatchButton.setEnabled(enabled);
            }
        });
    }

    private void setMachineType(String machineType) {
        this.machineType = machineType;
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
                    SinglePatchFrame.this.setInfo(sb.toString());
                    break;
                }
                case 2: {
                    if (value <= 0) break;
                    this.range = value;
                    break;
                }
                case 3: {
                    SinglePatchFrame.this.setInfo(SinglePatchFrame.this.bundle.getString("DI.finish"));
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

