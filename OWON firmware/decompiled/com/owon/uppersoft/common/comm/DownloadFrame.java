/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.dialogs.MessageDialog
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.ProgressBar
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.comm;

import com.owon.uppersoft.common.comm.DLEvent;
import com.owon.uppersoft.common.comm.DLListener;
import com.owon.uppersoft.common.comm.USBCommunication;
import com.owon.uppersoft.common.comm.job.OSCTYPE;
import com.owon.uppersoft.common.comm.job.model.FileDataFile;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.io.File;
import java.io.IOException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DownloadFrame
implements DLListener {
    private Label label;
    private Text downSpeed;
    private Text commSpeed;
    private Text TxtPath;
    private Button btnBrowser;
    private Button btnExit;
    private Button btnOK;
    private Button btnCancel;
    private ProgressBar progressBar;
    private Shell shell;
    private USBCommunication usbCom = new USBCommunication(this);
    private File file;

    public Shell getShell() {
        return this.shell;
    }

    public static void main(String[] args) {
        try {
            Display display = Display.getDefault();
            DownloadFrame shell = new DownloadFrame();
            shell.getShell().open();
            while (!shell.getShell().isDisposed()) {
                if (display.readAndDispatch()) continue;
                display.sleep();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DownloadFrame() {
        this.shell = new Shell();
        this.label = new Label((Composite)this.shell, 0);
        this.label.setText("Label");
        this.label.setBounds(20, 126, 470, 20);
        this.createContents();
        this.customizeContents();
    }

    protected void createContents() {
        this.shell.setText("SWT Application");
        this.shell.setSize(518, 219);
        this.btnExit = new Button((Composite)this.shell, 0);
        this.btnExit.setText(MsgCenter.getString("DF.exit"));
        this.btnExit.setBounds(430, 150, 60, 20);
        this.btnOK = new Button((Composite)this.shell, 0);
        this.btnOK.setText(MsgCenter.getString("DF.OK"));
        this.btnOK.setBounds(298, 150, 60, 20);
        this.btnCancel = new Button((Composite)this.shell, 0);
        this.btnCancel.setText(MsgCenter.getString("DF.cancel"));
        this.btnCancel.setBounds(364, 150, 60, 20);
        Label label = new Label((Composite)this.shell, 0);
        label.setText(MsgCenter.getString("DF.selectFile"));
        label.setBounds(20, 40, 66, 20);
        Label label_1 = new Label((Composite)this.shell, 0);
        label_1.setText(MsgCenter.getString("DF.progress"));
        label_1.setBounds(20, 70, 66, 20);
        this.TxtPath = new Text((Composite)this.shell, 2048);
        this.TxtPath.setBounds(102, 40, 315, 15);
        this.btnBrowser = new Button((Composite)this.shell, 0);
        this.btnBrowser.setText(MsgCenter.getString("DF.browse"));
        this.btnBrowser.setBounds(442, 40, 48, 22);
        this.progressBar = new ProgressBar((Composite)this.shell, 0);
        this.progressBar.setBounds(102, 70, 388, 17);
        Label label_2 = new Label((Composite)this.shell, 0);
        label_2.setText(MsgCenter.getString("DF.commSpeed"));
        label_2.setBounds(20, 100, 66, 20);
        Label label_3 = new Label((Composite)this.shell, 0);
        label_3.setText(MsgCenter.getString("DF.downSpeed"));
        label_3.setBounds(216, 100, 66, 20);
        this.commSpeed = new Text((Composite)this.shell, 2056);
        this.commSpeed.setBounds(102, 100, 80, 20);
        this.downSpeed = new Text((Composite)this.shell, 2056);
        this.downSpeed.setBounds(298, 97, 80, 20);
    }

    private void customizeContents() {
        this.btnOK.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                new Thread(){

                    @Override
                    public void run() {
                        OSCTYPE ot = OSCTYPE.bios;
                        DownloadFrame.this.usbCom.setDataItem(new FileDataFile(ot.name(), ot.value(), DownloadFrame.this.TxtPath.getText(), 16384));
                        boolean b = DownloadFrame.this.usbCom.getData();
                        if (!b) {
                            DownloadFrame.this.shell.getDisplay().asyncExec(new Runnable(){

                                @Override
                                public void run() {
                                    MessageDialog.openError((Shell)DownloadFrame.this.shell, (String)"", (String)"Error on comm.");
                                }
                            });
                        }
                    }
                }.start();
            }
        });
        this.btnCancel.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                DownloadFrame.this.usbCom.cancelTransaction();
            }
        });
        this.btnBrowser.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                FileDialog fd = new FileDialog(DownloadFrame.this.shell);
                String path = fd.open();
                if (path == null) {
                    return;
                }
                DownloadFrame.this.file = new File(path);
                try {
                    DownloadFrame.this.TxtPath.setText(DownloadFrame.this.file.getCanonicalPath());
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        this.btnExit.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                DownloadFrame.this.shell.close();
            }
        });
    }

    @Override
    public void handleEvent(final DLEvent e) {
        this.shell.getDisplay().asyncExec(new Runnable(){

            @Override
            public void run() {
                int value = e.intValue;
                switch (e.type) {
                    case 1: {
                        DownloadFrame.this.progressBar.setSelection(value);
                        break;
                    }
                    case 2: {
                        DownloadFrame.this.progressBar.setMaximum(value);
                        break;
                    }
                    case 3: {
                        DownloadFrame.this.commSpeed.setText(String.valueOf(String.valueOf(value)) + " kB/s");
                        break;
                    }
                    case 4: {
                        DownloadFrame.this.downSpeed.setText(String.valueOf(String.valueOf(value)) + " kB/s");
                        break;
                    }
                    case 5: {
                        DownloadFrame.this.label.setText(e.data.toString());
                    }
                }
            }
        });
    }

    public Label getLabel() {
        return this.label;
    }
}

