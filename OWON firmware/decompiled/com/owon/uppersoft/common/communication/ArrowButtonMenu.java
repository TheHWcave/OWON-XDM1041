/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.dialogs.MessageDialog
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.widgets.Decorations
 *  org.eclipse.swt.widgets.Menu
 *  org.eclipse.swt.widgets.MenuItem
 *  org.eclipse.swt.widgets.Shell
 */
package com.owon.uppersoft.common.communication;

import com.owon.uppersoft.common.aspect.Localizable;
import com.owon.uppersoft.common.i18n.CommonMessageLib;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class ArrowButtonMenu
implements Localizable {
    public static final String USBDrvDir = "USBDRV";
    private Menu arrowBtnMenu;
    private MenuItem installUSBItem;
    private MenuItem checkUSBItem;
    private boolean shouldNotify;
    private ResourceBundle bundle2;

    public ArrowButtonMenu(final Shell shell, final String command, boolean notify) {
        this.arrowBtnMenu = new Menu((Decorations)shell, 8);
        this.installUSBItem = new MenuItem(this.arrowBtnMenu, 8);
        this.installUSBItem.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                String msg = ArrowButtonMenu.this.bundle2.getString("USB.ConfirmReinstall");
                boolean option = true;
                ResourceBundle bundle = CommonMessageLib.getDefaultResourceBundle();
                MessageDialog dialog = new MessageDialog(shell, "", null, msg, 3, new String[]{bundle.getString("Option.OK"), bundle.getString("Option.Cancel")}, 0);
                int flag = dialog.open();
                if (flag == 1 || flag < 0) {
                    option = false;
                }
                if (!option) {
                    return;
                }
                try {
                    Runtime.getRuntime().exec(command, null, new File(ArrowButtonMenu.USBDrvDir));
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        this.shouldNotify = notify;
        this.checkUSBItem = new MenuItem(this.arrowBtnMenu, 32);
        this.checkUSBItem.setSelection(!this.shouldNotify);
        this.checkUSBItem.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent e) {
                ArrowButtonMenu.this.setShouldPopupCheck(!ArrowButtonMenu.this.shouldNotify);
            }
        });
    }

    protected void setShouldPopupCheck(boolean should) {
        this.shouldNotify = should;
    }

    public boolean getShouldPopupCheck() {
        return this.shouldNotify;
    }

    public Menu getMenu() {
        return this.arrowBtnMenu;
    }

    public void setVisible(boolean b) {
        this.arrowBtnMenu.setVisible(b);
    }

    @Override
    public void localize() {
        this.bundle2 = CommonMessageLib.getDefaultResourceBundle();
        this.installUSBItem.setText(this.bundle2.getString("USB.ReinstallUSBDriver"));
        this.checkUSBItem.setText(this.bundle2.getString("USB.HideUSBCheckWindow"));
    }
}

