/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.dialogs.MessageDialog
 *  org.eclipse.jface.viewers.ArrayContentProvider
 *  org.eclipse.jface.viewers.IBaseLabelProvider
 *  org.eclipse.jface.viewers.IContentProvider
 *  org.eclipse.jface.viewers.ISelection
 *  org.eclipse.jface.viewers.ITableLabelProvider
 *  org.eclipse.jface.viewers.LabelProvider
 *  org.eclipse.jface.viewers.StructuredSelection
 *  org.eclipse.jface.viewers.TableViewer
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.events.ShellAdapter
 *  org.eclipse.swt.events.ShellEvent
 *  org.eclipse.swt.events.ShellListener
 *  org.eclipse.swt.graphics.Image
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.layout.RowData
 *  org.eclipse.swt.layout.RowLayout
 *  org.eclipse.swt.program.Program
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Table
 *  org.eclipse.swt.widgets.TableColumn
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.update;

import com.owon.uppersoft.common.i18n.CommonMessageLib;
import com.owon.uppersoft.common.update.DownloadFile;
import com.owon.uppersoft.common.update.IUpdatable;
import com.owon.uppersoft.common.update.UpdateDetection;
import com.owon.uppersoft.common.update.UpdateTask;
import com.owon.uppersoft.common.utils.ShellUtil;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class UpdateFrame {
    private Text txtUrl;
    private Thread taskThd;
    private UpdateTask task;
    private Shell shell;
    private List<DownloadFile> files;
    private Composite bottomCom;
    private UpdateDetection ud;
    private Button cancelButton;
    private Button updateButton;
    private TableColumn urlTableColumn;
    private TableColumn progressTableColumn;
    private CloseNUpdate closeNupdate = new CloseNUpdate();
    private TableViewer tableViewer;

    public UpdateFrame(UpdateDetection ud, String url, List<DownloadFile> files) {
        this.ud = ud;
        this.files = files;
        this.createContents();
        this.txtUrl.setText("Downloading...");
        this.localize();
        this.task = new UpdateTask(this, url, files);
        this.taskThd = new Thread(this.task);
    }

    public void open() {
        this.shell.layout();
        this.shell.open();
        Display display = this.shell.getDisplay();
        while (!this.shell.isDisposed()) {
            if (display.readAndDispatch()) continue;
            display.sleep();
        }
    }

    protected void createContents() {
        this.shell = new Shell(this.ud.getUpdatable().getMainShell(), 34032);
        this.shell.setSize(430, 250);
        GridLayout gridLayout = new GridLayout();
        this.shell.setLayout((Layout)gridLayout);
        this.txtUrl = new Text((Composite)this.shell, 586);
        GridData gd_txtUrl = new GridData(4, 0x1000000, true, false);
        gd_txtUrl.heightHint = 30;
        this.txtUrl.setLayoutData((Object)gd_txtUrl);
        this.tableViewer = new TableViewer((Composite)this.shell, 67584);
        this.tableViewer.setLabelProvider((IBaseLabelProvider)new TableLabelProvider());
        this.tableViewer.setContentProvider((IContentProvider)new ArrayContentProvider());
        this.tableViewer.setInput(this.files);
        Table table = this.tableViewer.getTable();
        table.setHeaderVisible(true);
        GridData gd_table = new GridData(4, 4, true, true);
        table.setLayoutData((Object)gd_table);
        this.urlTableColumn = new TableColumn(table, 131072);
        this.urlTableColumn.setWidth(350);
        this.progressTableColumn = new TableColumn(table, 131072);
        this.progressTableColumn.setWidth(50);
        this.bottomCom = new Composite((Composite)this.shell, 0);
        RowLayout rowLayout = new RowLayout();
        rowLayout.justify = true;
        this.bottomCom.setLayout((Layout)rowLayout);
        GridData gd_bottomCom = new GridData(4, 0x1000000, true, false);
        this.bottomCom.setLayoutData((Object)gd_bottomCom);
        this.updateButton = new Button(this.bottomCom, 0);
        RowData rd_updateButton = new RowData();
        rd_updateButton.width = 100;
        this.updateButton.setLayoutData((Object)rd_updateButton);
        this.updateButton.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                UpdateFrame.this.updateButton.setEnabled(false);
                UpdateFrame.this.taskThd.start();
            }
        });
        this.cancelButton = new Button(this.bottomCom, 0);
        RowData rd_cancelButton = new RowData();
        rd_cancelButton.width = 100;
        this.cancelButton.setLayoutData((Object)rd_cancelButton);
        this.cancelButton.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                UpdateFrame.this.shell.close();
            }
        });
        this.shell.addShellListener((ShellListener)new ShellAdapter(){

            public void shellClosed(ShellEvent e) {
                if (UpdateFrame.this.taskThd.isAlive()) {
                    UpdateFrame.this.task.setCancel();
                    try {
                        UpdateFrame.this.taskThd.join();
                    }
                    catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        ShellUtil.centerLoc(this.shell);
        this.shell.setFocus();
    }

    public void localize() {
        ResourceBundle bundle = CommonMessageLib.getDefaultResourceBundle();
        this.shell.setText(bundle.getString("Info.Title"));
        this.urlTableColumn.setText(bundle.getString("Info.DownloadFile"));
        this.progressTableColumn.setText(bundle.getString("Info.DownloadProgress"));
        this.cancelButton.setText(bundle.getString("Option.Cancel"));
        this.updateButton.setText(bundle.getString("Info.DoUpdate"));
    }

    public File getDownloadTempDir() {
        return this.ud.getDownloadTempDir();
    }

    public void downloadFailed() {
        this.shell.getDisplay().asyncExec(new Runnable(){

            @Override
            public void run() {
                MessageDialog.openError((Shell)UpdateFrame.this.shell, (String)"", (String)CommonMessageLib.getDefaultResourceBundle().getString("Error.InternetConnection"));
                UpdateFrame.this.shell.close();
            }
        });
    }

    public void downloadFinished() {
        this.shell.getDisplay().asyncExec((Runnable)this.closeNupdate);
    }

    public void setCurrentDownloadFile(final DownloadFile downloadFile, URL url) {
        this.shell.getDisplay().asyncExec(new Runnable(){

            @Override
            public void run() {
                UpdateFrame.this.tableViewer.getTable().setFocus();
                UpdateFrame.this.tableViewer.setSelection((ISelection)new StructuredSelection((Object)downloadFile), true);
            }
        });
    }

    public void updateProgress() {
        this.shell.getDisplay().asyncExec(new Runnable(){

            @Override
            public void run() {
                UpdateFrame.this.tableViewer.refresh();
            }
        });
    }

    class CloseNUpdate
    implements Runnable {
        CloseNUpdate() {
        }

        @Override
        public void run() {
            if (!UpdateFrame.this.shell.isDisposed()) {
                UpdateFrame.this.cancelButton.setEnabled(false);
                UpdateFrame.this.shell.close();
            }
            IUpdatable iu = UpdateFrame.this.ud.getUpdatable();
            iu.notifyDestroy();
            iu.close();
            UpdateFrame.this.ud.filesUpdate(UpdateFrame.this.files);
            String eN = UpdateFrame.this.ud.getExecName();
            if (eN.length() == 0) {
                iu.startAgain();
            } else {
                Program.launch((String)eN);
            }
        }
    }

    class TableLabelProvider
    extends LabelProvider
    implements ITableLabelProvider {
        TableLabelProvider() {
        }

        public String getColumnText(Object element, int columnIndex) {
            DownloadFile df = (DownloadFile)element;
            switch (columnIndex) {
                case 0: {
                    return df.getRelativePath().replaceAll("owon.", "");
                }
                case 1: {
                    return df.getPercent();
                }
            }
            return "";
        }

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }
    }
}

