/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.DirectoryDialog
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Group
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Listener
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 *  org.eclipse.swt.widgets.Widget
 */
package com.owon.uppersoft.common.comm.ei;

import com.owon.uppersoft.common.comm.ei.PropertiesAccess;
import com.owon.uppersoft.common.comm.frame.MainFrm;
import com.owon.uppersoft.common.comm.job.BatchJob;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class EIDialog {
    private Button btnOSC;
    private Button btnLA;
    private Button btnOther;
    private Button exportButton;
    private Button importButton;
    private Text txtOther;
    private Text txtLA;
    private Text txtOSC;
    private Object result;
    private Shell shell;
    private MainFrm mf;
    private Button btnOSCDir;
    private Button btnLADir;
    private CSelectionListener cselect = new CSelectionListener();

    public EIDialog(MainFrm mf, Shell parent) {
        this.mf = mf;
        this.shell = new Shell(parent, 2160);
        this.createContents();
    }

    public Object open() {
        this.shell.open();
        this.shell.layout();
        Display display = this.shell.getDisplay();
        while (!this.shell.isDisposed()) {
            if (display.readAndDispatch()) continue;
            display.sleep();
        }
        return this.result;
    }

    protected void createContents() {
        GridLayout gridLayout = new GridLayout();
        this.shell.setLayout((Layout)gridLayout);
        this.shell.setSize(600, 300);
        this.shell.setText(MsgCenter.getString("EIDialog.ioset"));
        Composite composite = new Composite((Composite)this.shell, 0);
        composite.setLayoutData((Object)new GridData(4, 4, true, true));
        GridLayout gridLayout_4 = new GridLayout();
        gridLayout_4.verticalSpacing = 10;
        gridLayout_4.horizontalSpacing = 10;
        gridLayout_4.marginHeight = 10;
        gridLayout_4.marginWidth = 10;
        gridLayout_4.numColumns = 2;
        composite.setLayout((Layout)gridLayout_4);
        Label label = new Label(composite, 0);
        label.setLayoutData((Object)new GridData(4, 0x1000000, true, false, 2, 1));
        label.setText(MsgCenter.getString("EIDialog.prompt"));
        this.importButton = new Button(composite, 8);
        this.importButton.setText(MsgCenter.getString("EIDialog.import"));
        this.exportButton = new Button(composite, 8);
        this.exportButton.setText(MsgCenter.getString("EIDialog.outport"));
        Group oscGroup = new Group((Composite)this.shell, 0);
        oscGroup.setText(MsgCenter.getString("EIDialog.osc"));
        oscGroup.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 3;
        oscGroup.setLayout((Layout)gridLayout_1);
        this.txtOSC = new Text((Composite)oscGroup, 2048);
        this.txtOSC.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        this.btnOSC = new Button((Composite)oscGroup, 0);
        this.btnOSC.setText(MsgCenter.getString("EIDialog.browse"));
        this.btnOSCDir = new Button((Composite)oscGroup, 0);
        this.btnOSCDir.setText(MsgCenter.getString("EIDialog.browsedir"));
        Group laGroup = new Group((Composite)this.shell, 0);
        laGroup.setText(MsgCenter.getString("EIDialog.la"));
        laGroup.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 3;
        laGroup.setLayout((Layout)gridLayout_2);
        this.txtLA = new Text((Composite)laGroup, 2048);
        this.txtLA.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        this.btnLA = new Button((Composite)laGroup, 0);
        this.btnLA.setText(MsgCenter.getString("EIDialog.browse"));
        this.btnLADir = new Button((Composite)laGroup, 0);
        this.btnLADir.setText(MsgCenter.getString("EIDialog.browsedir"));
        Group otherGroup = new Group((Composite)this.shell, 0);
        otherGroup.setText(MsgCenter.getString("EIDialog.other"));
        otherGroup.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        GridLayout gridLayout_3 = new GridLayout();
        gridLayout_3.numColumns = 2;
        otherGroup.setLayout((Layout)gridLayout_3);
        this.txtOther = new Text((Composite)otherGroup, 2056);
        this.txtOther.setLayoutData((Object)new GridData(4, 0x1000000, true, false));
        this.btnOther = new Button((Composite)otherGroup, 0);
        this.btnOther.setEnabled(false);
        this.btnOther.setText(MsgCenter.getString("EIDialog.browse"));
        int SWT_Selection = 13;
        this.btnLA.addListener(SWT_Selection, (Listener)this.cselect);
        this.btnOSC.addListener(SWT_Selection, (Listener)this.cselect);
        this.btnOther.addListener(SWT_Selection, (Listener)this.cselect);
        this.exportButton.addListener(SWT_Selection, (Listener)this.cselect);
        this.importButton.addListener(SWT_Selection, (Listener)this.cselect);
        this.btnOSCDir.addListener(SWT_Selection, (Listener)this.cselect);
        this.btnLADir.addListener(SWT_Selection, (Listener)this.cselect);
    }

    private String getFile() {
        FileDialog fd = new FileDialog(this.shell);
        String path = fd.open();
        if (path == null) {
            return "";
        }
        return path;
    }

    private String getDir() {
        DirectoryDialog dd = new DirectoryDialog(this.shell);
        String path = dd.open();
        if (path == null) {
            return "";
        }
        return path;
    }

    protected void exportConf(String path, BatchJob bj) {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            boolean b = false;
            try {
                b = file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if (!b) {
                return;
            }
        }
        Properties p = bj.exportAsProperties();
        PropertiesAccess.storeProperties(p, file);
    }

    protected void importConf(String path, BatchJob bj) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            Properties p = PropertiesAccess.loadProperties(file);
            bj.importFromProperties(p);
        } else {
            bj.importFromDir(file);
        }
    }

    class CSelectionListener
    implements Listener {
        private int SWT_Selection = 13;

        CSelectionListener() {
        }

        public void handleEvent(Event event) {
            if (event.type != this.SWT_Selection) {
                return;
            }
            Widget o = event.widget;
            if (o == EIDialog.this.btnOther) {
                EIDialog.this.txtOther.setText(EIDialog.this.getFile());
            }
            if (o == EIDialog.this.btnOSC) {
                EIDialog.this.txtOSC.setText(EIDialog.this.getFile());
            }
            if (o == EIDialog.this.btnLA) {
                EIDialog.this.txtLA.setText(EIDialog.this.getFile());
            }
            if (o == EIDialog.this.btnOSCDir) {
                EIDialog.this.txtOSC.setText(EIDialog.this.getDir());
            }
            if (o == EIDialog.this.btnLADir) {
                EIDialog.this.txtLA.setText(EIDialog.this.getDir());
            }
            if (o == EIDialog.this.importButton) {
                EIDialog.this.importConf(EIDialog.this.txtOSC.getText(), BatchJob.oscbj);
                EIDialog.this.importConf(EIDialog.this.txtLA.getText(), BatchJob.msobj);
                EIDialog.this.mf.refreshTabs();
            }
            if (o == EIDialog.this.exportButton) {
                EIDialog.this.exportConf(EIDialog.this.txtOSC.getText(), BatchJob.oscbj);
                EIDialog.this.exportConf(EIDialog.this.txtLA.getText(), BatchJob.msobj);
            }
        }
    }
}

