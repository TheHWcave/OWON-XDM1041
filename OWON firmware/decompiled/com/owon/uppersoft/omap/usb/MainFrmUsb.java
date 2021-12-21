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
 *  org.eclipse.swt.layout.FillLayout
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.ProgressBar
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.omap.usb;

import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import com.owon.uppersoft.omap.ScriptEntry;
import com.owon.uppersoft.omap.usb.DoJob;
import com.owon.uppersoft.omap.usb.DownloadItem;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MainFrmUsb {
    private Text text_1;
    private Button btnStart;
    private Combo cobVer;
    private Combo cobModel;
    private Label lbSeril;
    private Text txtSn;
    private Label lbModel;
    private Label lbVer;
    private Shell shell;
    private Label lbpro;
    private ProgressBar progressBar;
    private List<DownloadItem> diList;

    public Shell getShell() {
        return this.shell;
    }

    public MainFrmUsb() {
        this.createContents();
        this.customizeContents();
    }

    public void open() {
        this.shell.layout();
        this.shell.open();
    }

    public MainFrmUsb getFrm() {
        return this;
    }

    protected void createContents() {
        this.shell = new Shell();
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        this.shell.setLayout((Layout)gridLayout);
        this.shell.addDisposeListener(new DisposeListener(){

            public void widgetDisposed(DisposeEvent e) {
            }
        });
        this.shell.setSize(600, 205);
        Composite composite = new Composite((Composite)this.shell, 0);
        composite.setLayout((Layout)new GridLayout(3, false));
        composite.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.lbModel = new Label(composite, 0);
        this.lbModel.setLayoutData((Object)new GridData(0x1000000, 0x1000000, false, false, 1, 1));
        this.lbVer = new Label(composite, 0);
        this.lbVer.setLayoutData((Object)new GridData(0x1000000, 0x1000000, false, false, 1, 1));
        this.lbSeril = new Label(composite, 0);
        this.lbSeril.setLayoutData((Object)new GridData(0x1000000, 0x1000000, false, false, 1, 1));
        this.cobModel = new Combo(composite, 8);
        this.cobModel.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                File parent = (File)MainFrmUsb.this.cobModel.getData(MainFrmUsb.this.cobModel.getText());
                File[] file = parent.listFiles();
                int i = 0;
                while (i < file.length) {
                    if (file[i].isDirectory()) {
                        String name = file[i].getName();
                        MainFrmUsb.this.cobVer.add(name);
                    }
                    ++i;
                }
            }
        });
        this.cobModel.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.cobVer = new Combo(composite, 8);
        this.cobVer.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.txtSn = new Text(composite, 2048);
        this.txtSn.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        Composite composite_1 = new Composite((Composite)this.shell, 0);
        composite_1.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        composite_1.setLayout((Layout)new GridLayout(3, false));
        this.lbpro = new Label(composite_1, 0);
        this.progressBar = new ProgressBar(composite_1, 0);
        this.progressBar.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 1, 1));
        this.btnStart = new Button(composite_1, 0);
        this.btnStart.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                MainFrmUsb.this.startUsbDownload();
            }
        });
        Composite composite_2 = new Composite((Composite)this.shell, 0);
        composite_2.setLayoutData((Object)new GridData(4, 4, true, true, 1, 1));
        composite_2.setLayout((Layout)new FillLayout(256));
        this.text_1 = new Text(composite_2, 2568);
        this.localize();
        this.initCombo();
    }

    private void startUsbDownload() {
        if (!this.initDownloadItem()) {
            return;
        }
        this.btnStart.setEnabled(false);
        this.progressBar.setMaximum(this.diList.size());
        DoJob doJob = new DoJob(this);
        doJob.start();
    }

    private boolean initDownloadItem() {
        if (this.cobVer.getSelectionIndex() == -1 || this.cobModel.getSelectionIndex() == -1) {
            MessageDialog.openError((Shell)this.shell, (String)"", (String)"model or version no selected");
            return false;
        }
        String serial = this.txtSn.getText().trim();
        if (serial.equalsIgnoreCase("")) {
            MessageDialog.openError((Shell)this.shell, (String)"", (String)"sn can not be null");
            return false;
        }
        String model = this.cobModel.getText();
        String ver = this.cobVer.getText();
        String path = "data\\" + model + "\\" + ver + "\\";
        String fullVer = String.valueOf(model) + "-" + ver;
        ScriptEntry se = new ScriptEntry(new File(String.valueOf(path) + "spq4chboot.txt"));
        se.update(serial, fullVer);
        se.save(String.valueOf(path) + "spq4chboot.sh");
        File x_load = new File("data\\xload.xml");
        if (!x_load.exists()) {
            MessageDialog.openError((Shell)this.shell, (String)"", (String)"x-load file no found");
            return false;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(x_load);
            NodeList items = doc.getElementsByTagName("item");
            int len = items.getLength();
            System.out.println(len);
            this.diList = new ArrayList<DownloadItem>();
            int i = 0;
            while (i < len) {
                Element n = (Element)items.item(i);
                this.diList.add(new DownloadItem(n, path));
                ++i;
            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
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
            this.txtSn.setText(String.valueOf(s.substring(0, len - 4)) + end);
            return true;
        }
        catch (Exception exception) {
            MessageDialog.openError((Shell)this.shell, (String)"", (String)"last 4 char is not digit");
            return false;
        }
    }

    private void initCombo() {
        File data = new File("data");
        if (!data.exists() || !data.isDirectory()) {
            return;
        }
        File[] file = data.listFiles();
        int i = 0;
        while (i < file.length) {
            if (file[i].isDirectory()) {
                String name = file[i].getName();
                this.cobModel.add(name);
                this.cobModel.setData(name, (Object)file[i]);
            }
            ++i;
        }
    }

    protected void customizeContents() {
    }

    protected void localize() {
        this.shell.setText(String.valueOf(MsgCenter.getString("OM.title")) + "1.9.9");
        this.btnStart.setText(MsgCenter.getString("MainFrm.start"));
        this.lbSeril.setText(MsgCenter.getString("OM.seril"));
        this.lbModel.setText(MsgCenter.getString("OM.model"));
        this.lbVer.setText(MsgCenter.getString("OM.ver"));
        this.lbpro.setText(MsgCenter.getString("OM.prograss"));
    }

    public void logLn(final String s) {
        if (!this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new Runnable(){

                @Override
                public void run() {
                    MainFrmUsb.this.text_1.append(String.valueOf(s) + "\n");
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
                    MainFrmUsb.this.btnStart.setEnabled(b);
                }
            });
        }
    }

    public void setprog(final int i) {
        if (!this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new Runnable(){

                @Override
                public void run() {
                    MainFrmUsb.this.progressBar.setSelection(i);
                }
            });
        }
    }

    public List<DownloadItem> getList() {
        return this.diList;
    }
}

