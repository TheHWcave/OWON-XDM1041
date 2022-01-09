/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.events.DisposeEvent
 *  org.eclipse.swt.events.DisposeListener
 *  org.eclipse.swt.events.ModifyEvent
 *  org.eclipse.swt.events.ModifyListener
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
package com.owon.uppersoft.patch.direct;

import com.owon.uppersoft.common.comm.DLEvent;
import com.owon.uppersoft.common.comm.DLListener;
import com.owon.uppersoft.common.comm.IPref;
import com.owon.uppersoft.common.comm.job.model.DataItem;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.patch.direct.EnterDownloadMode;
import com.owon.uppersoft.patch.direct.EnterDownloadMode_COM;
import com.owon.uppersoft.patch.direct.EnterDownloadMode_USB;
import com.owon.uppersoft.patch.direct.PatchProcess;
import com.owon.uppersoft.patch.direct.Reg;
import com.owon.uppersoft.patch.direct.USBConfirm;
import com.owon.uppersoft.patch.direct.serial.SerialPortUtil;
import com.owon.uppersoft.patch.direct.util.ZipAndDe;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
    private Combo combo;
    private Text txtPath;
    private Combo combo_lan;
    private Button btnUSB;
    private Text info;
    private Text patchText;
    private Shell shell;
    private Button startPatchButton;
    private Reg reg;
    private EnterDownloadMode dm;
    private String strCom;
    private Label label;
    private Button[] steps;
    private int curStep;
    private PatchProcess pp;
    private String usbok;
    private String usbbad;
    private boolean usbgood = false;
    private HandleEventRunner handleEventRunner = new HandleEventRunner();
    private Label plbl;
    private Label prelbl;
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
        this.reg = new Reg();
        this.reg.load();
        this.curStep = 0;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        this.shell.setLayout((Layout)gridLayout);
        Composite titleCom = new Composite((Composite)this.shell, 0);
        titleCom.setLayout((Layout)new FillLayout());
        GridData gd_titleCom = new GridData(4, 0x1000000, true, false);
        gd_titleCom.widthHint = 550;
        gd_titleCom.heightHint = 60;
        titleCom.setLayoutData((Object)gd_titleCom);
        this.patchText = new Text(titleCom, 74);
        Composite operateCom = new Composite((Composite)this.shell, 0);
        operateCom.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 5;
        operateCom.setLayout((Layout)gridLayout_1);
        Label languageLabel = new Label(operateCom, 0);
        languageLabel.setText("Language: ");
        this.combo_lan = new Combo(operateCom, 8);
        this.label = new Label(operateCom, 0);
        GridData gd_label = new GridData(4, 0x1000000, false, false);
        gd_label.widthHint = 56;
        this.label.setLayoutData((Object)gd_label);
        this.combo = new Combo(operateCom, 8);
        this.combo.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                SinglePatchFrame.this.strCom = SinglePatchFrame.this.combo.getText().trim();
            }
        });
        this.combo.setLayoutData((Object)new GridData(16384, 0x1000000, true, false));
        new Label(operateCom, 0);
        this.plbl = new Label(operateCom, 0);
        GridData gd_plbl = new GridData(4, 0x1000000, false, false, 2, 1);
        gd_plbl.widthHint = 231;
        this.plbl.setLayoutData((Object)gd_plbl);
        this.prelbl = new Label(operateCom, 131072);
        this.prelbl.setLayoutData((Object)new GridData(16384, 0x1000000, false, false, 2, 1));
        new Label(operateCom, 0);
        if (this.reg.isNeedSN()) {
            this.txtPath = new Text(operateCom, 133120);
            this.txtPath.addModifyListener(new ModifyListener(){

                public void modifyText(ModifyEvent e) {
                    String psn = SinglePatchFrame.this.txtPath.getText();
                    boolean b = ZipAndDe.findMatchSN(SinglePatchFrame.this.txtPath, psn);
                    SinglePatchFrame.this.startPatchButton.setEnabled(b);
                }
            });
            GridData gd_txtPath = new GridData(4, 0x1000000, false, false);
            gd_txtPath.widthHint = 108;
            this.txtPath.setLayoutData((Object)gd_txtPath);
        }
        Composite selectCom = new Composite((Composite)this.shell, 0);
        selectCom.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 4;
        gridLayout_2.marginHeight = 0;
        selectCom.setLayout((Layout)gridLayout_2);
        this.btnUSB = new Button(selectCom, 0);
        this.btnUSB.setVisible(false);
        this.btnUSB.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        this.startPatchButton = new Button(selectCom, 0);
        this.startPatchButton.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        int step = this.reg.getStep();
        this.steps = new Button[step];
        this.steps[0] = this.startPatchButton;
        int i = 1;
        while (i < step) {
            this.steps[i] = new Button(selectCom, 0);
            this.steps[i].setEnabled(false);
            this.steps[i].setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
            final String path = "bundles" + i;
            this.steps[i].addSelectionListener((SelectionListener)new SelectionAdapter(){

                public void widgetSelected(SelectionEvent e) {
                    SinglePatchFrame.this.startPatch(path, "");
                }
            });
            ++i;
        }
        Composite infoCom = new Composite((Composite)this.shell, 0);
        infoCom.setLayout((Layout)new FillLayout());
        GridData gd_infoCom = new GridData(4, 0x1000000, true, false);
        gd_infoCom.heightHint = 30;
        infoCom.setLayoutData((Object)gd_infoCom);
        this.info = new Text(infoCom, 74);
        this.combo_lan.setFocus();
        this.customContents();
    }

    private void customContents() {
        Locale l;
        this.setMachineType(this.reg.getType());
        if (this.reg.getDownModel() == 0) {
            this.label.setVisible(false);
            this.combo.setVisible(false);
        } else {
            this.label.setVisible(true);
            this.combo.setVisible(true);
            this.loadPorts();
        }
        this.prelbl.setText(this.reg.getSN());
        if (this.reg.isNeedSN()) {
            this.startPatchButton.setEnabled(false);
        } else {
            this.startPatchButton.setEnabled(true);
        }
        this.dm = this.reg.getDownModel() == 0 ? new EnterDownloadMode_USB(this) : new EnterDownloadMode_COM(this);
        this.pp = new PatchProcess(this, this, this.dm);
        this.localize();
        Locale[] localeArray = IPref.LOCALES;
        int n = IPref.LOCALES.length;
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
                    Locale.setDefault(IPref.LOCALES[idx]);
                }
                SinglePatchFrame.this.localize();
            }
        });
        this.btnUSB.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                USBConfirm.reinstallUSB(SinglePatchFrame.this.shell, MsgCenter.getBundle());
            }
        });
        this.shell.addDisposeListener(new DisposeListener(){

            public void widgetDisposed(DisposeEvent e) {
                SinglePatchFrame.this.pp.cancel();
                SinglePatchFrame.this.dm.cancel();
            }
        });
        this.startPatchButton.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                String psn = "";
                if (SinglePatchFrame.this.reg.isNeedSN()) {
                    psn = SinglePatchFrame.this.txtPath.getText();
                }
                SinglePatchFrame.this.startPatch("bundles", psn);
            }
        });
    }

    private void startPatch(String path, String psn) {
        File f = new File(path);
        if (!f.exists() || f.isDirectory()) {
            this.setInfo(MsgCenter.getBundle().getString("DI.invalidPath"));
            return;
        }
        ZipAndDe zad = new ZipAndDe();
        List<DataItem> dfs = zad.loadZipFile(f, new File("public.key"), psn, this.machineType);
        this.pp.startPatch(zad, dfs, psn);
    }

    public static String filtExt(String name) {
        return name.substring(0, name.lastIndexOf("."));
    }

    private void loadPorts() {
        List<String> list = SerialPortUtil.loadAvailablePort();
        String[] coms = new String[list.size()];
        this.combo.setItems(list.toArray(coms));
        this.combo.select(0);
        this.strCom = this.combo.getText().trim();
    }

    protected void localize() {
        ResourceBundle bundle = MsgCenter.getBundle();
        this.usbok = bundle.getString("DI.usbok");
        this.usbbad = bundle.getString("DI.usbbad");
        this.shell.setText(String.valueOf(bundle.getString("DI.title")) + " " + "1.9.9");
        this.startPatchButton.setText(bundle.getString("DI.update"));
        this.usbok = bundle.getString("DI.usbok");
        this.usbbad = bundle.getString("DI.usbbad");
        this.btnUSB.setText(this.usbgood ? this.usbok : this.usbbad);
        this.patchText.setText(bundle.getString("DI.titleInfo"));
        this.plbl.setText(bundle.getString("DI.plbl"));
        this.label.setText(bundle.getString("DI.port"));
        int i = 1;
        while (i < this.reg.getStep()) {
            this.steps[i].setText(bundle.getString("DI.next"));
            ++i;
        }
    }

    @Override
    public void handleEvent(DLEvent e) {
        this.handleEventRunner.setDLEvent(e);
        this.shell.getDisplay().syncExec((Runnable)this.handleEventRunner);
    }

    public void appendInfo(final String txt) {
        if (!this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new Runnable(){

                @Override
                public void run() {
                    SinglePatchFrame.this.info.append(txt);
                }
            });
        }
    }

    public void setInfo(final String txt) {
        if (!this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new Runnable(){

                @Override
                public void run() {
                    SinglePatchFrame.this.info.setText(txt);
                }
            });
        }
    }

    public void setPatchButtonEnable(final boolean enabled) {
        if (!this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new Runnable(){

                @Override
                public void run() {
                    int i = 0;
                    while (i < SinglePatchFrame.this.steps.length) {
                        SinglePatchFrame.this.steps[SinglePatchFrame.this.curStep].setEnabled(false);
                        ++i;
                    }
                    if (enabled) {
                        SinglePatchFrame.this.steps[SinglePatchFrame.this.curStep].setEnabled(enabled);
                    }
                }
            });
        }
    }

    private void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public Reg getReg() {
        return this.reg;
    }

    public String getCom() {
        return this.strCom;
    }

    public void nextStep() {
        ++this.curStep;
        if (this.curStep == this.steps.length) {
            this.curStep = 0;
            this.setInfo(MsgCenter.getBundle().getString("DI.FinishAll"));
        }
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
                    SinglePatchFrame.this.setInfo(sb.toString());
                    break;
                }
                case 2: {
                    if (value <= 0) break;
                    this.range = value;
                    break;
                }
                case 3: {
                    SinglePatchFrame.this.setInfo(bundle.getString("DI.finish"));
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

