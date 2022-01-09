/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.Point
 *  org.eclipse.swt.graphics.Rectangle
 *  org.eclipse.swt.layout.RowLayout
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Text
 */
package com.owon.uppersoft.common.communication;

import com.owon.uppersoft.common.i18n.CommonMessageLib;
import java.util.ResourceBundle;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PopupWindow {
    public static final int wndWidth = 180;
    public static final int wndHeight = 80;
    private Shell parent;
    private Shell shell;
    private Display display;
    private Text text;
    private int currentHeight;
    private boolean status;
    protected int moveStep = 2;
    protected int upPosition;
    protected int downPositon;
    protected int currentPosition;
    protected int leftPosition;
    private boolean use = true;
    final Runnable raise = new Runnable(){

        @Override
        public void run() {
            PopupWindow.this.isDisposed = PopupWindow.this.shell.isDisposed();
            if (PopupWindow.this.isDisposed) {
                return;
            }
            PopupWindow.this.shell.setLocation(PopupWindow.this.leftPosition, PopupWindow.this.currentPosition);
            Shell shell = PopupWindow.this.shell;
            PopupWindow popupWindow = PopupWindow.this;
            int n = popupWindow.currentHeight + PopupWindow.this.moveStep;
            popupWindow.currentHeight = n;
            shell.setSize(180, n);
        }
    };
    final Runnable fall = new Runnable(){

        @Override
        public void run() {
            PopupWindow.this.isDisposed = PopupWindow.this.shell.isDisposed();
            if (PopupWindow.this.isDisposed) {
                return;
            }
            PopupWindow.this.shell.setLocation(PopupWindow.this.leftPosition, PopupWindow.this.currentPosition);
            Shell shell = PopupWindow.this.shell;
            PopupWindow popupWindow = PopupWindow.this;
            int n = popupWindow.currentHeight - PopupWindow.this.moveStep;
            popupWindow.currentHeight = n;
            shell.setSize(180, n);
        }
    };
    final Runnable compute = new Runnable(){

        @Override
        public void run() {
            try {
                PopupWindow.this.isDisposed = PopupWindow.this.shell.isDisposed();
                if (PopupWindow.this.isDisposed) {
                    return;
                }
                PopupWindow.this.isParentShellMinimized = PopupWindow.this.parent.getMinimized();
                if (PopupWindow.this.isParentShellMinimized) {
                    return;
                }
                ResourceBundle bundle = CommonMessageLib.getDefaultResourceBundle();
                String msg = bundle.getString(PopupWindow.this.status ? "USB.connected" : "USB.disconnected");
                PopupWindow.this.text.setText(msg);
                PopupWindow.this.text.setForeground(PopupWindow.this.display.getSystemColor(PopupWindow.this.status ? 6 : 3));
                PopupWindow.this.shell.setVisible(true);
                PopupWindow.this.p = PopupWindow.this.parent.getLocation();
                PopupWindow.this.area = PopupWindow.this.parent.getClientArea();
                PopupWindow.this.currentHeight = ((PopupWindow)PopupWindow.this).shell.getSize().y;
                PopupWindow.this.downPositon = PopupWindow.this.p.y + PopupWindow.this.area.y + PopupWindow.this.area.height;
                PopupWindow.this.upPosition = PopupWindow.this.downPositon - 80;
                PopupWindow.this.currentPosition = PopupWindow.this.downPositon;
                PopupWindow.this.leftPosition = PopupWindow.this.p.x + PopupWindow.this.area.x + PopupWindow.this.area.width - 180;
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    };
    Point p;
    Rectangle area;
    boolean isDisposed;
    boolean isParentShellMinimized = false;

    public PopupWindow(Shell parent) {
        this.parent = parent;
        this.display = parent.getDisplay();
        this.shell = new Shell(parent, 16384);
        this.text = new Text((Composite)this.shell, 66);
        this.text.setBackground(this.shell.getBackground());
        this.shell.setLayout((Layout)new RowLayout());
        this.shell.setSize(180, 0);
        this.shell.open();
    }

    public void updateStatus(boolean status) {
        if (!this.use) {
            return;
        }
        this.status = status;
        this.run();
    }

    private void moveUp() {
        while (!this.isDisposed) {
            try {
                Thread.sleep(10L);
                if ((this.currentPosition -= this.moveStep) <= this.upPosition) break;
                this.display.asyncExec(this.raise);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveDown() {
        while (!this.isDisposed) {
            try {
                Thread.sleep(10L);
                if ((this.currentPosition += this.moveStep) >= this.downPositon) break;
                this.display.asyncExec(this.fall);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.display.asyncExec(new Runnable(){

            @Override
            public void run() {
                PopupWindow.this.isDisposed = PopupWindow.this.shell.isDisposed();
                if (PopupWindow.this.isDisposed) {
                    return;
                }
                PopupWindow.this.shell.setVisible(false);
            }
        });
    }

    protected void run() {
        this.display.syncExec(this.compute);
        if (this.isParentShellMinimized) {
            return;
        }
        this.moveUp();
        try {
            Thread.sleep(1000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.moveDown();
    }
}

