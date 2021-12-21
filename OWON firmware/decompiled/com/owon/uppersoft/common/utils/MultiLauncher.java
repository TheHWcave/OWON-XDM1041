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
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.common.utils;

import com.owon.uppersoft.common.aspect.ILaunchable;
import com.owon.uppersoft.common.utils.ShellUtil;
import java.util.Iterator;
import java.util.LinkedList;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

public class MultiLauncher {
    private Shell shell;
    private Display display;
    private Composite composite;

    public void open(Iterator<ILaunchable> ilaunch) {
        this.display = Display.getDefault();
        this.shell = new Shell(this.display);
        this.shell.setLayout((Layout)new FillLayout());
        this.shell.setText("MSO");
        this.composite = new Composite((Composite)this.shell, 0);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginTop = 10;
        gridLayout.marginRight = 10;
        gridLayout.marginLeft = 10;
        gridLayout.marginBottom = 10;
        this.composite.setLayout((Layout)gridLayout);
        while (ilaunch.hasNext()) {
            final ILaunchable il = ilaunch.next();
            Button oscButton = new Button(this.composite, 0);
            oscButton.addSelectionListener((SelectionListener)new SelectionAdapter(){

                public void widgetSelected(SelectionEvent e) {
                    MultiLauncher.this.shell.close();
                    il.run();
                }
            });
            GridData gd_oscButton = new GridData(4, 0x1000000, false, false);
            oscButton.setLayoutData((Object)gd_oscButton);
            oscButton.setText(il.getText());
            oscButton.setImage(this.display.getSystemImage(2));
        }
        this.shell.pack();
        ShellUtil.centerLoc(this.shell);
        this.shell.open();
        while (!this.shell.isDisposed()) {
            if (this.display.readAndDispatch()) continue;
            this.display.sleep();
        }
    }

    public static void main(String[] args) {
        LinkedList<ILaunchable> ilaunch = new LinkedList<ILaunchable>();
        ilaunch.add(new ILaunchable(){

            @Override
            public String getText() {
                return "Number 1";
            }

            @Override
            public void run() {
            }
        });
        ilaunch.add(new ILaunchable(){

            @Override
            public String getText() {
                return "Number 2";
            }

            @Override
            public void run() {
            }
        });
        new MultiLauncher().open(ilaunch.iterator());
        new MultiLauncher().open(ilaunch.iterator());
    }
}

