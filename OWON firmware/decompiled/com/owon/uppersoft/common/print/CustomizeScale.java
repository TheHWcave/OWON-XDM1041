/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.layout.FillLayout
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Listener
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Spinner
 *  org.eclipse.swt.widgets.Widget
 */
package com.owon.uppersoft.common.print;

import com.owon.uppersoft.common.aspect.Localizable;
import com.owon.uppersoft.common.i18n.CommonMessageLib;
import com.owon.uppersoft.common.utils.ShellUtil;
import java.util.ResourceBundle;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Widget;

public class CustomizeScale
implements Localizable {
    private Button cancelButton;
    private Button okButton;
    private Spinner spinner;
    private Label label;
    private Shell shell;
    private int scale;
    private CSelectionListener cselect = new CSelectionListener();

    public Shell getShell() {
        return this.shell;
    }

    public CustomizeScale(Shell parent) {
        this.shell = new Shell(parent, 67680);
    }

    public double open(double scale) {
        this.scale = (int)(scale * 100.0);
        this.createContents();
        this.customizeContents();
        this.shell.pack();
        ShellUtil.centerLoc(this.shell);
        this.shell.open();
        Display display = this.shell.getDisplay();
        while (!this.shell.isDisposed()) {
            if (display.readAndDispatch()) continue;
            display.sleep();
        }
        return (double)this.scale / 100.0;
    }

    protected void createContents() {
        this.shell.setLayout((Layout)new FillLayout());
        Composite composite = new Composite((Composite)this.shell, 0);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        composite.setLayout((Layout)gridLayout);
        this.label = new Label(composite, 0);
        GridData gd_label = new GridData(4, 0x1000000, true, false);
        this.label.setLayoutData((Object)gd_label);
        this.spinner = new Spinner(composite, 2048);
        GridData gd_spinner = new GridData(50, -1);
        this.spinner.setLayoutData((Object)gd_spinner);
        Label percentlabel = new Label(composite, 0);
        percentlabel.setText("%");
        this.okButton = new Button(composite, 0);
        GridData gd_okButton = new GridData(131072, 0x1000000, false, false);
        gd_okButton.widthHint = 60;
        this.okButton.setLayoutData((Object)gd_okButton);
        this.cancelButton = new Button(composite, 0);
        GridData gd_cancelButton = new GridData(60, -1);
        this.cancelButton.setLayoutData((Object)gd_cancelButton);
    }

    protected void customizeContents() {
        this.spinner.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetDefaultSelected(SelectionEvent e) {
                CustomizeScale.this.scale = CustomizeScale.this.spinner.getSelection();
                CustomizeScale.this.shell.close();
            }
        });
        this.spinner.setMinimum(0);
        this.spinner.setMaximum(600);
        this.spinner.setSelection(this.scale);
        int SWT_Selection = 13;
        CSelectionListener listener = this.cselect;
        this.okButton.addListener(SWT_Selection, (Listener)listener);
        this.cancelButton.addListener(SWT_Selection, (Listener)listener);
        this.localize();
    }

    @Override
    public void localize() {
        ResourceBundle bundle = CommonMessageLib.getDefaultResourceBundle();
        this.shell.setText(bundle.getString("CS.title"));
        this.label.setText(bundle.getString("CS.Prompt"));
        this.okButton.setText(bundle.getString("Option.OK"));
        this.cancelButton.setText(bundle.getString("Option.Cancel"));
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
            if (o == CustomizeScale.this.okButton) {
                CustomizeScale.this.scale = CustomizeScale.this.spinner.getSelection();
                CustomizeScale.this.shell.close();
            }
            if (o == CustomizeScale.this.cancelButton) {
                CustomizeScale.this.shell.close();
            }
        }
    }
}

