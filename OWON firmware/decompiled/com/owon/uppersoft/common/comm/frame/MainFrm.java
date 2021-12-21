/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.dialogs.MessageDialog
 *  org.eclipse.jface.util.LocalSelectionTransfer
 *  org.eclipse.jface.viewers.CheckboxTableViewer
 *  org.eclipse.swt.custom.CTabFolder
 *  org.eclipse.swt.custom.CTabItem
 *  org.eclipse.swt.custom.SashForm
 *  org.eclipse.swt.dnd.DropTarget
 *  org.eclipse.swt.dnd.DropTargetAdapter
 *  org.eclipse.swt.dnd.DropTargetEvent
 *  org.eclipse.swt.dnd.DropTargetListener
 *  org.eclipse.swt.dnd.FileTransfer
 *  org.eclipse.swt.dnd.Transfer
 *  org.eclipse.swt.events.DisposeEvent
 *  org.eclipse.swt.events.DisposeListener
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.graphics.Device
 *  org.eclipse.swt.graphics.Image
 *  org.eclipse.swt.layout.FillLayout
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Control
 *  org.eclipse.swt.widgets.Decorations
 *  org.eclipse.swt.widgets.DirectoryDialog
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Listener
 *  org.eclipse.swt.widgets.Menu
 *  org.eclipse.swt.widgets.MenuItem
 *  org.eclipse.swt.widgets.ProgressBar
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.TableItem
 *  org.eclipse.swt.widgets.Text
 *  org.eclipse.swt.widgets.ToolBar
 *  org.eclipse.swt.widgets.ToolItem
 *  org.eclipse.swt.widgets.Widget
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.comm.Propert;
import com.owon.uppersoft.common.comm.USBID;
import com.owon.uppersoft.common.comm.ei.EIDialog;
import com.owon.uppersoft.common.comm.frame.BarcodeReaderComposite;
import com.owon.uppersoft.common.comm.frame.CText;
import com.owon.uppersoft.common.comm.frame.ChangeParamComposite;
import com.owon.uppersoft.common.comm.frame.DebugComposite;
import com.owon.uppersoft.common.comm.frame.EncryptionComposite;
import com.owon.uppersoft.common.comm.frame.InstrumentComposite;
import com.owon.uppersoft.common.comm.frame.MultimeterComposite;
import com.owon.uppersoft.common.comm.frame.OdpDownloadComposite;
import com.owon.uppersoft.common.comm.frame.SerialComm;
import com.owon.uppersoft.common.comm.frame.SerialSender;
import com.owon.uppersoft.common.comm.frame.TdsComposite;
import com.owon.uppersoft.common.comm.frame.XdsComComposite;
import com.owon.uppersoft.common.comm.frame.odptxtedit.OdpComposite;
import com.owon.uppersoft.common.comm.frame.txtedit.TxtEditComposite;
import com.owon.uppersoft.common.comm.frame.vds.VdsComposite;
import com.owon.uppersoft.common.comm.frame.vds.Xds40Composite;
import com.owon.uppersoft.common.comm.job.BatchJob;
import com.owon.uppersoft.common.comm.job.BatchRunner;
import com.owon.uppersoft.common.comm.job.model.FileDataFile;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.common.utils.VersionUtil;
import com.owon.uppersoft.patch.direct.serial.SerialPortUtil;
import com.owon.uppersoft.patch.direct.util.CRC;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

public class MainFrm {
    private Button btnRefresh;
    private Combo cobUSB;
    private Menu popupErase;
    private MenuItem itemFLASH;
    private MenuItem itemBOOT;
    private MenuItem itemBIOS;
    private MenuItem itemTABLE;
    private MenuItem itemBAK;
    private Button btnErase;
    private Button btnRunOS;
    private Button checkReboot;
    public static final Image EmptyImage = new Image((Device)Display.getDefault(), 15, 20);
    private MenuItem exitMenuItem;
    private MenuItem eiMenuItem;
    private Shell shell;
    private ToolItem eiToolItem;
    private Button btnReboot;
    private Button btnStart;
    private Button btnCOMM;
    private Button btnSelNone;
    private Button btnSelAll;
    private Button clearButton;
    private Label progressLabel;
    private CText txtLog;
    private ProgressBar progressBar;
    private SashForm sash;
    private CTabFolder tabFolder;
    private BatchRunner br;
    private CSelectionListener cselect = new CSelectionListener();
    private Combo combo;
    private Label splab;
    private Label labUsb;
    private Button btnEnterUSBMode;
    private Button btnCrc;
    private Button btnClc;
    private Button btnDownload;
    private Button btnT;
    private Button btnAutoEarse;
    private Button btnXDSdownload;
    private BarcodeReaderComposite bcr;
    private ChangeParamComposite apc;
    private OdpComposite odpCom;
    private MultimeterComposite xdmCom;
    private XdsComComposite xdsCom;
    private OdpDownloadComposite odlCom;
    private TxtEditComposite txtCom;
    private VdsComposite vdsCom;
    private Xds40Composite xds40Com;
    private Button btnTekDrv;
    private static int SHOW_ITEM_INDEX = 10;
    private SerialSender ss;
    private ToolItem OSCImportDir;
    private ToolItem LAImportDir;
    private boolean autoErase;

    public BatchRunner getbr() {
        return this.br;
    }

    public Text getTxtLog() {
        return this.txtLog;
    }

    public CTabFolder getTabFolder() {
        return this.tabFolder;
    }

    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    public Label getProgressLabel() {
        return this.progressLabel;
    }

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

    public String getPort() {
        return this.combo.getText();
    }

    protected void createContents() {
        this.shell = new Shell();
        this.shell.addDisposeListener(new DisposeListener(){

            public void widgetDisposed(DisposeEvent e) {
                EmptyImage.dispose();
            }
        });
        this.shell.setLayout((Layout)new FillLayout());
        this.shell.setText(String.valueOf(MsgCenter.getString("MainFrm.title")) + " " + this.getFullVer());
        this.shell.setSize(960, 850);
        Menu menu = new Menu((Decorations)this.shell, 2);
        MenuItem fileMenuItem = new MenuItem(menu, 64);
        fileMenuItem.setText(MsgCenter.getString("MainFrm.file"));
        Menu menu_1 = new Menu(fileMenuItem);
        fileMenuItem.setMenu(menu_1);
        this.eiMenuItem = new MenuItem(menu_1, 0);
        this.eiMenuItem.setText(MsgCenter.getString("MainFrm.io"));
        new MenuItem(menu_1, 2);
        this.exitMenuItem = new MenuItem(menu_1, 0);
        this.exitMenuItem.setText(MsgCenter.getString("MainFrm.exit"));
        MenuItem helpMenuItem = new MenuItem(menu, 64);
        helpMenuItem.setText(MsgCenter.getString("MainFrm.help"));
        Menu menu_2 = new Menu(helpMenuItem);
        helpMenuItem.setMenu(menu_2);
        MenuItem aboutMenuItem = new MenuItem(menu_2, 0);
        aboutMenuItem.setText(MsgCenter.getString("MainFrm.about"));
        Composite clientArea = new Composite((Composite)this.shell, 0);
        clientArea.setLayout((Layout)new GridLayout());
        ToolBar toolBar = new ToolBar(clientArea, 0);
        toolBar.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        if (SHOW_ITEM_INDEX > -1) {
            toolBar.setVisible(false);
        }
        this.eiToolItem = new ToolItem(toolBar, 8);
        this.eiToolItem.setText(MsgCenter.getString("MainFrm.io"));
        this.LAImportDir = new ToolItem(toolBar, 0);
        this.LAImportDir.setText(MsgCenter.getString("MainFrm.ladir"));
        this.OSCImportDir = new ToolItem(toolBar, 0);
        this.OSCImportDir.setText(MsgCenter.getString("MainFrm.oscdir"));
        this.sash = new SashForm(clientArea, 512);
        this.sash.setLayoutData((Object)new GridData(4, 4, true, true));
        Composite statusLine = new Composite(clientArea, 2048);
        statusLine.setLayout((Layout)new GridLayout());
        statusLine.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        Label label = new Label(statusLine, 0);
        label.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        label.setText(MsgCenter.getString("MainFrm.injection"));
        this.tabFolder = new CTabFolder((Composite)this.sash, 2048);
        this.tabFolder.setSimple(false);
        this.tabFolder.setTabHeight(20);
        this.tabFolder.addSelectionListener(new SelectionListener(){

            public void widgetSelected(SelectionEvent arg0) {
                CTabItem item = MainFrm.this.tabFolder.getSelection();
                if (item.getControl() != null) {
                    return;
                }
                switch (MainFrm.this.tabFolder.getSelectionIndex()) {
                    case 8: {
                        MainFrm.this.odpCom = new OdpComposite((Composite)MainFrm.this.tabFolder);
                        Composite odp = MainFrm.this.odpCom.getComposite();
                        odp.setData((Object)MainFrm.this.odpCom);
                        item.setControl((Control)odp);
                        break;
                    }
                    case 11: {
                        MainFrm.this.txtCom = new TxtEditComposite((Composite)MainFrm.this.tabFolder);
                        Composite txt = MainFrm.this.txtCom.getComposite();
                        txt.setData((Object)MainFrm.this.txtCom);
                        item.setControl((Control)txt);
                    }
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });
        Composite operateCom = new Composite((Composite)this.sash, 0);
        this.sash.setTabList(new Control[]{this.tabFolder, operateCom});
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 10;
        operateCom.setLayout((Layout)gridLayout);
        this.progressLabel = new Label(operateCom, 0);
        GridData gd_progressLabel = new GridData(4, 0x1000000, false, false);
        gd_progressLabel.widthHint = 82;
        this.progressLabel.setLayoutData((Object)gd_progressLabel);
        this.progressLabel.setText(MsgCenter.getString("MainFrm.progress"));
        this.progressBar = new ProgressBar(operateCom, 0);
        GridData gd_progressBar = new GridData(4, 0x1000000, false, false, 1, 1);
        gd_progressBar.widthHint = 100;
        this.progressBar.setLayoutData((Object)gd_progressBar);
        this.btnEnterUSBMode = new Button(operateCom, 0);
        this.btnEnterUSBMode.setText(MsgCenter.getString("MainFrm.toUSBdown"));
        this.splab = new Label(operateCom, 131072);
        this.splab.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 1, 1));
        this.splab.setText(MsgCenter.getString("MainFrm.splab"));
        this.combo = new Combo(operateCom, 8);
        GridData gd_combo = new GridData(0x1000000, 0x1000000, false, false);
        gd_combo.widthHint = 50;
        this.combo.setLayoutData((Object)gd_combo);
        this.labUsb = new Label(operateCom, 0);
        this.labUsb.setLayoutData((Object)new GridData(0x1000000, 0x1000000, false, false));
        this.labUsb.setText(MsgCenter.getString("MainFrm.usb"));
        this.cobUSB = new Combo(operateCom, 8);
        GridData gd_cobUSB = new GridData(4, 0x1000000, true, false);
        gd_cobUSB.widthHint = 147;
        this.cobUSB.setLayoutData((Object)gd_cobUSB);
        this.btnRefresh = new Button(operateCom, 0);
        this.btnRefresh.setText(MsgCenter.getString("MainFrm.refresh"));
        this.btnCOMM = new Button(operateCom, 2);
        GridData gd_btnCOMM = new GridData(4, 0x1000000, false, false, 1, 1);
        gd_btnCOMM.widthHint = 120;
        this.btnCOMM.setLayoutData((Object)gd_btnCOMM);
        this.btnCOMM.setText(MsgCenter.getString("MainFrm.U"));
        this.btnT = new Button(operateCom, 0);
        this.btnT.setText(MsgCenter.getString("MainFrm.ampCheck"));
        this.btnSelAll = new Button(operateCom, 0);
        GridData gd_btnSelAll = new GridData(4, 0x1000000, false, false);
        gd_btnSelAll.widthHint = 66;
        this.btnSelAll.setLayoutData((Object)gd_btnSelAll);
        this.btnSelAll.setText(MsgCenter.getString("MainFrm.selectAll"));
        this.btnSelNone = new Button(operateCom, 0);
        this.btnSelNone.setText(MsgCenter.getString("MainFrm.selectNone"));
        this.clearButton = new Button(operateCom, 0);
        this.clearButton.setText(MsgCenter.getString("MainFrm.clear"));
        this.btnStart = new Button(operateCom, 0);
        this.btnStart.setLayoutData((Object)new GridData(0x1000000, 0x1000000, false, false));
        this.btnStart.setText(MsgCenter.getString("MainFrm.start"));
        this.btnReboot = new Button(operateCom, 0);
        this.btnReboot.setLayoutData((Object)new GridData(0x1000000, 0x1000000, false, false));
        this.btnReboot.setText(MsgCenter.getString("MainFrm.reboot"));
        this.checkReboot = new Button(operateCom, 131104);
        this.checkReboot.setLayoutData((Object)new GridData(4, 0x1000000, false, false));
        this.checkReboot.setText(MsgCenter.getString("MainFrm.rebootOnFinished"));
        this.btnRunOS = new Button(operateCom, 0);
        this.btnRunOS.setText(MsgCenter.getString("MainFrm.runOS"));
        this.btnErase = new Button(operateCom, 0);
        this.btnErase.setLayoutData((Object)new GridData(4, 0x1000000, false, false));
        this.btnErase.setText(MsgCenter.getString("MainFrm.eraser"));
        this.popupErase = new Menu((Control)this.btnErase);
        this.itemBIOS = new MenuItem(this.popupErase, 0);
        this.itemBIOS.setText(MsgCenter.getString("MainFrm.biosProgram"));
        this.itemBOOT = new MenuItem(this.popupErase, 0);
        this.itemBOOT.setText(MsgCenter.getString("MainFrm.bootFile"));
        this.itemFLASH = new MenuItem(this.popupErase, 0);
        this.itemFLASH.setText(MsgCenter.getString("MainFrm.flashFile"));
        this.itemTABLE = new MenuItem(this.popupErase, 0);
        this.itemTABLE.setText(MsgCenter.getString("MainFrm.tableFile"));
        this.itemBAK = new MenuItem(this.popupErase, 0);
        this.itemBAK.setText(MsgCenter.getString("MainFrm.bakFile"));
        this.btnCrc = new Button(operateCom, 0);
        this.btnCrc.setLayoutData((Object)new GridData(4, 0x1000000, false, false));
        this.btnCrc.setText(MsgCenter.getString("MainFrm.CRCcheck"));
        this.btnClc = new Button(operateCom, 0);
        this.btnClc.setText(MsgCenter.getString("MainFrm.clc"));
        this.btnDownload = new Button(operateCom, 0);
        this.btnDownload.setLayoutData((Object)new GridData(4, 4, true, false));
        this.btnDownload.setText(MsgCenter.getString("MainFrm.onekyedownload"));
        this.btnTekDrv = new Button(operateCom, 32);
        this.btnTekDrv.setText(MsgCenter.getString("MainFrm.tekdrv"));
        this.btnAutoEarse = new Button(operateCom, 32);
        this.btnAutoEarse.setText(MsgCenter.getString("MainFrm.autoearse"));
        String flag = Propert.getInstance().get("autoerase");
        this.autoErase = !flag.equalsIgnoreCase("0");
        this.btnAutoEarse.setSelection(this.autoErase);
        this.btnXDSdownload = new Button(operateCom, 32);
        this.btnXDSdownload.setText(MsgCenter.getString("MainFrm.xdsdownload"));
        new Label(operateCom, 0);
        new Label(operateCom, 0);
        new Label(operateCom, 0);
        new Label(operateCom, 0);
        new Label(operateCom, 0);
        new Label(operateCom, 0);
        this.txtLog = new CText(operateCom, 2626);
        this.txtLog.setLayoutData(new GridData(4, 4, false, true, 11, 1));
        this.sash.setWeights(new int[]{170, 170});
    }

    protected void customizeContents() {
        Widget[] cs;
        this.btnCOMM.addSelectionListener(new SelectionListener(){

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent ee) {
                if (MainFrm.this.btnCOMM.getSelection()) {
                    boolean b = MainFrm.this.ss.openPort(MainFrm.this.combo.getText());
                    if (!b) {
                        MessageDialog.openError((Shell)MainFrm.this.shell, (String)"", (String)MsgCenter.getString("MainFrm.comFail"));
                        MainFrm.this.btnCOMM.setSelection(false);
                    }
                } else {
                    MainFrm.this.ss.release();
                }
            }
        });
        this.cobUSB.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                MainFrm.this.br.setUsbSelected(MainFrm.this.cobUSB.getSelectionIndex());
            }
        });
        this.shell.addDisposeListener(new DisposeListener(){

            public void widgetDisposed(DisposeEvent e) {
                Propert.getInstance().save();
                MainFrm.this.ss.release();
            }
        });
        this.btnSelAll.setVisible(false);
        this.btnSelNone.setVisible(false);
        this.btnStart.setVisible(false);
        this.btnReboot.setVisible(false);
        this.clearButton.setVisible(false);
        this.btnRunOS.setVisible(false);
        this.btnErase.setVisible(false);
        this.btnEnterUSBMode.setVisible(false);
        this.btnCrc.setVisible(false);
        this.btnRefresh.setVisible(false);
        this.btnClc.setVisible(false);
        this.btnT.setVisible(false);
        this.btnDownload.setVisible(false);
        this.btnTekDrv.setVisible(false);
        this.btnAutoEarse.setVisible(false);
        this.btnXDSdownload.setVisible(false);
        this.checkReboot.setVisible(false);
        this.cobUSB.setVisible(false);
        this.labUsb.setVisible(false);
        this.btnCOMM.setVisible(false);
        this.progressBar.setVisible(false);
        this.progressLabel.setVisible(false);
        Widget[] widgetArray = cs = new Widget[]{this.btnSelAll, this.btnSelNone, this.btnStart, this.btnReboot, this.clearButton, this.exitMenuItem, this.eiMenuItem, this.eiToolItem, this.btnRunOS, this.btnErase, this.itemBIOS, this.itemBOOT, this.itemFLASH, this.itemTABLE, this.itemBAK, this.btnEnterUSBMode, this.btnCrc, this.btnRefresh, this.btnClc, this.btnT, this.btnDownload, this.btnTekDrv, this.OSCImportDir, this.LAImportDir, this.btnAutoEarse};
        int n = cs.length;
        int n2 = 0;
        while (n2 < n) {
            Widget w = widgetArray[n2];
            w.addListener(13, (Listener)this.cselect);
            ++n2;
        }
        this.init();
        this.tabFolder.setSelection(0);
    }

    protected void init() {
        this.br = new BatchRunner(this);
        List<String> list = SerialPortUtil.loadAvailablePort();
        String[] coms = new String[list.size()];
        this.combo.setItems(list.toArray(coms));
        this.combo.select(0);
        CTabItem odlItem = new CTabItem(this.tabFolder, 0);
        odlItem.setText(MsgCenter.getString("MainFrm.odl"));
        this.odlCom = new OdpDownloadComposite((Composite)this.tabFolder);
        Composite odl = this.odlCom.getComposite();
        this.odlCom.setFrm(this);
        odl.setData((Object)this.odlCom);
        odlItem.setControl((Control)odl);
        this.sash.setFocus();
        this.ss = new SerialSender(this.txtLog);
    }

    public void addBatchJob(final BatchJob bj) {
        CTabItem tabItem = new CTabItem(this.tabFolder, 0);
        tabItem.setText(bj.getTypeName());
        final InstrumentComposite ic = new InstrumentComposite((Composite)this.tabFolder, bj);
        Composite com = ic.getComposite();
        com.setData((Object)ic);
        tabItem.setControl((Control)com);
        DropTarget dropTarget = new DropTarget((Control)com, 15);
        dropTarget.setTransfer(new Transfer[]{LocalSelectionTransfer.getTransfer(), FileTransfer.getInstance()});
        dropTarget.addDropListener((DropTargetListener)new DropTargetAdapter(){

            public void drop(DropTargetEvent event) {
                String[] files = (String[])event.data;
                MainFrm.this.importDir(files[0], bj);
                ic.getCheckboxTableViewer().getTable().setData((Object)files[0]);
            }
        });
    }

    public void refreshTabs() {
        CTabItem ct = this.tabFolder.getSelection();
        Object o = ct.getControl().getData();
        if (o instanceof InstrumentComposite) {
            InstrumentComposite ic = (InstrumentComposite)o;
            ic.getCheckboxTableViewer().refresh();
            ic.setChecked();
        }
    }

    private void clc(Object oo) {
        this.txtLog.setText("");
        this.btnClc.setEnabled(false);
        SerialComm sc = new SerialComm(this.txtLog);
        sc.openPort(this.combo.getText());
        boolean isAddHead = false;
        if (oo instanceof TdsComposite) {
            isAddHead = true;
            String path = ((TdsComposite)oo).getText();
            this.br.clc(sc, isAddHead, path);
        } else {
            this.br.clc(sc, isAddHead, "");
        }
    }

    private void doDownload(CheckboxTableViewer ctv, boolean isCompress) {
        BatchJob bj = (BatchJob)ctv.getInput();
        bj.clearStatus();
        if (ctv.getTable().getItemCount() > 9) {
            this.conbindFpga(ctv);
        }
        ctv.refresh();
        this.br.setDataFiles(ctv, isCompress);
        this.br.thread();
    }

    private void conbindFpga(CheckboxTableViewer ctv) {
        TableItem osfp = ctv.getTable().getItem(0);
        TableItem agfp = ctv.getTable().getItem(9);
        if (!osfp.getChecked() || !agfp.getChecked()) {
            return;
        }
        File f1 = new File(osfp.getText(1));
        File f2 = new File(agfp.getText(1));
        String path = String.valueOf(osfp.getText(1)) + ".uno";
        File out = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(out);
            FileInputStream fis1 = new FileInputStream(f1);
            FileInputStream fis2 = new FileInputStream(f2);
            int len = fis1.available();
            byte[] b = new byte[len + 4];
            b[0] = (byte)(len & 0xFF);
            b[1] = (byte)(len >> 8 & 0xFF);
            b[2] = (byte)(len >> 16 & 0xFF);
            b[3] = (byte)(len >> 24 & 0xFF);
            fis1.read(b, 4, len);
            fos.write(b);
            len = fis2.available();
            b = new byte[len];
            fis2.read(b);
            fos.write(b);
            fos.flush();
            fos.close();
            fis2.close();
            fis1.close();
            ((FileDataFile)ctv.getElementAt(0)).setPath(path);
            agfp.setChecked(false);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int doCRC32(FileDataFile df) {
        int fileSize = (int)df.getLength();
        byte[] bytes = new byte[fileSize];
        try {
            FileInputStream fis = new FileInputStream(df.getPath());
            fis.read(bytes);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        int crc = 0;
        byte tmp = 0;
        int len = bytes.length;
        int i = 0;
        while (i < len) {
            tmp = bytes[i];
            crc ^= tmp << 8;
            int j = 0;
            while (j < 8) {
                crc = (crc & 0x8000) != 0 ? crc << 1 ^ 0x1021 : (crc <<= 1);
                ++j;
            }
            ++i;
        }
        return crc;
    }

    public Button getCheckReboot() {
        return this.checkReboot;
    }

    public void resetCOMM() {
        Display.getDefault().asyncExec(new Runnable(){

            @Override
            public void run() {
                MainFrm.this.btnCOMM.setSelection(false);
                if (MainFrm.this.ss != null) {
                    MainFrm.this.ss.release();
                }
            }
        });
    }

    public void setClcEnable(final boolean b) {
        Display.getDefault().asyncExec(new Runnable(){

            @Override
            public void run() {
                MainFrm.this.btnClc.setEnabled(b);
            }
        });
    }

    public void setTEnable(final boolean b) {
        Display.getDefault().asyncExec(new Runnable(){

            @Override
            public void run() {
                MainFrm.this.btnT.setEnabled(b);
            }
        });
    }

    public void saveFile(String s) {
        Display.getDefault().asyncExec(new Runnable(){

            @Override
            public void run() {
            }
        });
    }

    public Button getBtnRefresh() {
        return this.btnRefresh;
    }

    public Combo getCobUSB() {
        return this.cobUSB;
    }

    public String getFullVer() {
        String path = MainFrm.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String info = null;
        try {
            info = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {}
        return new VersionUtil(info).getFullVer();
    }

    private String getDir() {
        DirectoryDialog dd = new DirectoryDialog(this.shell);
        String path = dd.open();
        if (path == null) {
            return "";
        }
        return path;
    }

    private void importDir(String path, BatchJob bj) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            bj.importFromDir(file);
        }
        if (file.isFile()) {
            bj.importFromFile(file);
        }
        this.refreshTabs();
    }

    public boolean isAutoErase() {
        return this.autoErase;
    }

    public class CSelectionListener
    implements Listener {
        private int SWT_Selection = 13;

        public void handleEvent(Event event) {
            if (event.type != this.SWT_Selection) {
                return;
            }
            Widget o = event.widget;
            if (o == MainFrm.this.btnTekDrv) {
                USBID.isAFG = MainFrm.this.btnTekDrv.getSelection();
                return;
            }
            if (o == MainFrm.this.btnReboot) {
                MainFrm.this.br.reboot();
                return;
            }
            if (o == MainFrm.this.clearButton) {
                MainFrm.this.txtLog.setText("");
                return;
            }
            if (o == MainFrm.this.exitMenuItem) {
                MainFrm.this.shell.close();
                return;
            }
            if (o == MainFrm.this.btnEnterUSBMode) {
                MainFrm.this.br.enterUSBMode();
                return;
            }
            if (o == MainFrm.this.btnRefresh) {
                MainFrm.this.br.refreshUsb();
                return;
            }
            Object oo = MainFrm.this.tabFolder.getSelection().getControl().getData();
            if (oo instanceof EncryptionComposite) {
                EncryptionComposite ec = (EncryptionComposite)oo;
                if (o == MainFrm.this.btnStart) {
                    MainFrm.this.br.setDataFile(ec.getDataFile(), ec.getMark());
                    MainFrm.this.br.thread();
                }
                return;
            }
            if (oo instanceof DebugComposite) {
                DebugComposite dc = (DebugComposite)oo;
                if (o == MainFrm.this.btnStart) {
                    String path = dc.getFilePath();
                    if (path == null) {
                        path = "";
                    }
                    MainFrm.this.br.debug(path, ".txt");
                    return;
                }
                if (o == MainFrm.this.btnT) {
                    MainFrm.this.txtLog.setText("");
                    MainFrm.this.btnT.setEnabled(false);
                    SerialComm sc = new SerialComm(MainFrm.this.txtLog);
                    boolean b = sc.openPort(MainFrm.this.combo.getText());
                    if (!b) {
                        MessageDialog.openError((Shell)MainFrm.this.shell, (String)"", (String)MsgCenter.getString("MainFrm.comFail"));
                        MainFrm.this.btnT.setEnabled(true);
                        return;
                    }
                    String path = dc.getFilePath();
                    if (path == null) {
                        path = "";
                    }
                    MainFrm.this.br.t(sc, path);
                }
                return;
            }
            if (oo instanceof TdsComposite) {
                if (o == MainFrm.this.btnClc) {
                    MainFrm.this.clc(oo);
                    return;
                }
                if (o != MainFrm.this.btnStart) {
                    return;
                }
                TdsComposite tc = (TdsComposite)oo;
                int index = tc.getIndex();
                String txt = tc.getText();
                if (txt.equalsIgnoreCase("")) {
                    MainFrm.this.txtLog.append("text can not be null\n");
                    return;
                }
                switch (index) {
                    case 0: {
                        MainFrm.this.br.getDefFile(txt);
                        break;
                    }
                    case 1: {
                        MainFrm.this.br.setDefFile(txt);
                        break;
                    }
                    case 2: {
                        MainFrm.this.br.update(txt);
                    }
                }
                return;
            }
            if (o == MainFrm.this.btnClc) {
                MainFrm.this.clc(oo);
                return;
            }
            if (!(oo instanceof InstrumentComposite)) {
                return;
            }
            InstrumentComposite icom = (InstrumentComposite)oo;
            final CheckboxTableViewer ctv = icom.getCheckboxTableViewer();
            if (o == MainFrm.this.btnSelAll) {
                ctv.setAllChecked(true);
                return;
            }
            if (o == MainFrm.this.btnSelNone) {
                ctv.setAllChecked(false);
                return;
            }
            if (o == MainFrm.this.eiMenuItem || o == MainFrm.this.eiToolItem) {
                EIDialog eid = new EIDialog(MainFrm.this, MainFrm.this.shell);
                eid.open();
                return;
            }
            if (o == MainFrm.this.OSCImportDir) {
                MainFrm.this.importDir(MainFrm.this.getDir(), BatchJob.oscbj);
                return;
            }
            if (o == MainFrm.this.LAImportDir) {
                MainFrm.this.importDir(MainFrm.this.getDir(), BatchJob.msobj);
                return;
            }
            if (o == MainFrm.this.btnDownload) {
                MainFrm.this.btnDownload.setEnabled(false);
                MainFrm.this.br.oneKeyDownload(new Runnable(){

                    @Override
                    public void run() {
                        MainFrm.this.shell.getDisplay().syncExec(new Runnable(){

                            @Override
                            public void run() {
                                MainFrm.this.btnDownload.setEnabled(true);
                                MainFrm.this.doDownload(ctv, MainFrm.this.btnXDSdownload.getSelection());
                            }
                        });
                    }
                });
                return;
            }
            if (o == MainFrm.this.btnStart) {
                MainFrm.this.doDownload(ctv, MainFrm.this.btnXDSdownload.getSelection());
                return;
            }
            if (o == MainFrm.this.btnRunOS) {
                MainFrm.this.br.runOS();
                return;
            }
            if (o == MainFrm.this.btnErase) {
                MainFrm.this.popupErase.setVisible(true);
                return;
            }
            if (o == MainFrm.this.itemBIOS) {
                MainFrm.this.br.eraseBIOS();
                return;
            }
            if (o == MainFrm.this.itemBOOT) {
                MainFrm.this.br.eraseBOOT();
                return;
            }
            if (o == MainFrm.this.itemFLASH) {
                MainFrm.this.br.eraseFLASH();
                return;
            }
            if (o == MainFrm.this.itemTABLE) {
                MainFrm.this.br.eraseTABLE();
                return;
            }
            if (o == MainFrm.this.itemBAK) {
                MainFrm.this.br.eraseBAK();
                return;
            }
            if (o == MainFrm.this.btnCrc) {
                Object[] os = ctv.getCheckedElements();
                int checksum = 0;
                MainFrm.this.txtLog.append("----------------------\n");
                Object[] objectArray = os;
                int n = os.length;
                int n2 = 0;
                while (n2 < n) {
                    Object obj = objectArray[n2];
                    FileDataFile df = (FileDataFile)obj;
                    File f = new File(df.getPath());
                    if (f.exists()) {
                        int sum = 0;
                        sum = df.getType().equalsIgnoreCase("tx") ? CRC.doCRC16(df.getPath(), 4) : CRC.doCRC16(df.getPath());
                        MainFrm.this.txtLog.append(String.valueOf(df.getType()) + ":" + sum + "\n");
                        checksum += sum;
                    }
                    ++n2;
                }
                MainFrm.this.txtLog.append("CRC:" + Integer.toHexString(checksum).toUpperCase() + "\n");
                MainFrm.this.txtLog.append("----------------------\n");
                return;
            }
            if (o == MainFrm.this.btnAutoEarse) {
                MainFrm.this.autoErase = MainFrm.this.btnAutoEarse.getSelection();
                Propert.getInstance().set("autoerase", MainFrm.this.autoErase ? "1" : "0");
            }
        }
    }
}

