/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.Rectangle
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.Group
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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Widget;

public class PageSetup
implements Localizable {
    private Button cancelButton;
    private Button defaultButton;
    private Button okButton;
    private Label rightMMLabel;
    private Label bottomMMLabel;
    private Label leftMMLabel;
    private Label topMMLabel;
    private Spinner rightSpinner;
    private Spinner bottomSpinner;
    private Spinner leftSpinner;
    private Spinner topSpinner;
    private Label rightLabel;
    private Label bottomLabel;
    private Label leftLabel;
    private Label topLabel;
    private Label promptLabel;
    private Group boundsGroup;
    private Shell shell;
    private Rectangle minTrim;
    private Rectangle maxTrim;
    private Rectangle customizeTrim;
    private Rectangle defaultTrim;
    private CSelectionListener cselect = new CSelectionListener();

    public Shell getShell() {
        return this.shell;
    }

    public PageSetup(Shell parent) {
        this.shell = new Shell(parent, 67680);
        int dt = 6;
        this.minTrim = new Rectangle(dt, dt, dt, dt);
        dt = 20;
        this.defaultTrim = new Rectangle(dt, dt, dt, dt);
        dt = 100;
        this.maxTrim = new Rectangle(dt, dt, dt, dt);
    }

    public Rectangle open(Rectangle customizeTrim) {
        this.customizeTrim = customizeTrim;
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
        return this.customizeTrim;
    }

    protected void createContents() {
        this.shell.setLayout((Layout)new GridLayout());
        this.boundsGroup = new Group((Composite)this.shell, 0);
        this.boundsGroup.setLayout((Layout)new GridLayout());
        this.promptLabel = new Label((Composite)this.boundsGroup, 0);
        this.promptLabel.setLayoutData((Object)new GridData(4, 4, true, false));
        Composite locationCom = new Composite((Composite)this.boundsGroup, 0);
        GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.marginRight = 15;
        gridLayout_1.marginLeft = 15;
        gridLayout_1.marginWidth = 15;
        gridLayout_1.marginHeight = 15;
        gridLayout_1.verticalSpacing = 15;
        gridLayout_1.horizontalSpacing = 15;
        gridLayout_1.numColumns = 7;
        locationCom.setLayout((Layout)gridLayout_1);
        this.topLabel = new Label(locationCom, 131072);
        this.topLabel.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 3, 1));
        this.topSpinner = new Spinner(locationCom, 2048);
        this.topMMLabel = new Label(locationCom, 0);
        this.topMMLabel.setLayoutData((Object)new GridData(16384, 0x1000000, false, false, 3, 1));
        this.topMMLabel.setText("mm");
        this.leftLabel = new Label(locationCom, 131072);
        this.leftSpinner = new Spinner(locationCom, 2048);
        this.leftMMLabel = new Label(locationCom, 0);
        this.leftMMLabel.setLayoutData((Object)new GridData(16384, 0x1000000, false, false, 2, 1));
        this.leftMMLabel.setText("mm");
        this.rightLabel = new Label(locationCom, 131072);
        this.rightSpinner = new Spinner(locationCom, 2048);
        this.rightMMLabel = new Label(locationCom, 0);
        this.rightMMLabel.setText("mm");
        this.bottomLabel = new Label(locationCom, 131072);
        this.bottomLabel.setLayoutData((Object)new GridData(131072, 0x1000000, false, false, 3, 1));
        this.bottomSpinner = new Spinner(locationCom, 2048);
        this.bottomMMLabel = new Label(locationCom, 0);
        this.bottomMMLabel.setLayoutData((Object)new GridData(16384, 0x1000000, false, false, 3, 1));
        this.bottomMMLabel.setText("mm");
        Composite btnBar = new Composite((Composite)this.shell, 0);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        btnBar.setLayout((Layout)gridLayout);
        GridData gd_btnBar = new GridData(131072, 4, false, false);
        btnBar.setLayoutData((Object)gd_btnBar);
        this.okButton = new Button(btnBar, 0);
        this.okButton.setLayoutData((Object)new GridData(70, -1));
        this.defaultButton = new Button(btnBar, 0);
        this.defaultButton.setLayoutData((Object)new GridData(70, -1));
        this.cancelButton = new Button(btnBar, 0);
        this.cancelButton.setLayoutData((Object)new GridData(70, -1));
    }

    protected void customizeContents() {
        this.leftSpinner.setMinimum(this.minTrim.x);
        this.rightSpinner.setMinimum(this.minTrim.width);
        this.topSpinner.setMinimum(this.minTrim.y);
        this.bottomSpinner.setMinimum(this.minTrim.height);
        this.leftSpinner.setMaximum(this.maxTrim.x);
        this.rightSpinner.setMaximum(this.maxTrim.width);
        this.topSpinner.setMaximum(this.maxTrim.y);
        this.bottomSpinner.setMaximum(this.maxTrim.height);
        this.leftSpinner.setSelection(this.customizeTrim.x);
        this.rightSpinner.setSelection(this.customizeTrim.width);
        this.topSpinner.setSelection(this.customizeTrim.y);
        this.bottomSpinner.setSelection(this.customizeTrim.height);
        int SWT_Selection = 13;
        CSelectionListener listener = this.cselect;
        this.okButton.addListener(SWT_Selection, (Listener)listener);
        this.cancelButton.addListener(SWT_Selection, (Listener)listener);
        this.defaultButton.addListener(SWT_Selection, (Listener)listener);
        this.localize();
    }

    @Override
    public void localize() {
        ResourceBundle bundle = CommonMessageLib.getDefaultResourceBundle();
        this.shell.setText(bundle.getString("PS.title"));
        this.boundsGroup.setText(bundle.getString("PS.BorderTitle"));
        this.promptLabel.setText(bundle.getString("PS.Prompt"));
        this.leftLabel.setText(bundle.getString("PS.Left"));
        this.rightLabel.setText(bundle.getString("PS.Right"));
        this.topLabel.setText(bundle.getString("PS.Top"));
        this.bottomLabel.setText(bundle.getString("PS.Bottom"));
        this.okButton.setText(bundle.getString("Option.OK"));
        this.defaultButton.setText(bundle.getString("PS.Default"));
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
            if (o == PageSetup.this.okButton) {
                ((PageSetup)PageSetup.this).customizeTrim.x = PageSetup.this.leftSpinner.getSelection();
                ((PageSetup)PageSetup.this).customizeTrim.width = PageSetup.this.rightSpinner.getSelection();
                ((PageSetup)PageSetup.this).customizeTrim.y = PageSetup.this.topSpinner.getSelection();
                ((PageSetup)PageSetup.this).customizeTrim.height = PageSetup.this.bottomSpinner.getSelection();
                PageSetup.this.shell.close();
            }
            if (o == PageSetup.this.cancelButton) {
                PageSetup.this.shell.close();
            }
            if (o == PageSetup.this.defaultButton) {
                PageSetup.this.leftSpinner.setSelection(((PageSetup)PageSetup.this).defaultTrim.x);
                PageSetup.this.rightSpinner.setSelection(((PageSetup)PageSetup.this).defaultTrim.width);
                PageSetup.this.topSpinner.setSelection(((PageSetup)PageSetup.this).defaultTrim.y);
                PageSetup.this.bottomSpinner.setSelection(((PageSetup)PageSetup.this).defaultTrim.height);
            }
        }
    }
}

