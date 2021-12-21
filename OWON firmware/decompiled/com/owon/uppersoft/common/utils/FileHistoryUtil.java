/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.events.MenuEvent
 *  org.eclipse.swt.events.MenuListener
 *  org.eclipse.swt.widgets.Listener
 *  org.eclipse.swt.widgets.Menu
 *  org.eclipse.swt.widgets.MenuItem
 */
package com.owon.uppersoft.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class FileHistoryUtil {
    private List<File> fileHistory;
    private int capacity;
    private Menu fileHistoryMenu;
    private Listener l;

    public FileHistoryUtil(int capacity, List<File> fileHistory) {
        this.capacity = capacity;
        if (fileHistory == null) {
            fileHistory = new ArrayList<File>(capacity);
        }
        this.fileHistory = fileHistory;
    }

    public void add(File file) {
        this.fileHistory.remove(file);
        this.fileHistory.add(file);
        if (this.fileHistory.size() > this.capacity) {
            this.fileHistory.remove(0);
        }
    }

    public void remove(File file, Menu fileHistoryMenu) {
        this.fileHistory.remove(file);
        MenuItem[] menuItemArray = fileHistoryMenu.getItems();
        int n = menuItemArray.length;
        int n2 = 0;
        while (n2 < n) {
            MenuItem m = menuItemArray[n2];
            if (file.equals(m.getData())) {
                m.dispose();
                break;
            }
            ++n2;
        }
    }

    public void setFileHistoryMenu(Menu menu, Listener listener) {
        this.fileHistoryMenu = menu;
        this.l = listener;
        this.fileHistoryMenu.addMenuListener(new MenuListener(){

            public void menuHidden(MenuEvent e) {
            }

            public void menuShown(MenuEvent e) {
                FileHistoryUtil.this.updateFileHistory(FileHistoryUtil.this.fileHistoryMenu, FileHistoryUtil.this.l);
            }
        });
    }

    protected void updateFileHistory(Menu fileHistoryMenu, Listener l) {
        MenuItem[] menuItemArray = fileHistoryMenu.getItems();
        int n = menuItemArray.length;
        int n2 = 0;
        while (n2 < n) {
            MenuItem m = menuItemArray[n2];
            if ((m.getStyle() & 2) == 0) {
                m.dispose();
            }
            ++n2;
        }
        int size = this.fileHistory.size();
        MenuItem mi = null;
        String name = null;
        String path = null;
        int i = 0;
        while (i < size) {
            File file = this.fileHistory.get(i);
            name = file.getName();
            path = file.getPath();
            mi = new MenuItem(fileHistoryMenu, 8);
            mi.setText("&" + (i + 1) + " " + name + " [" + path + "]");
            mi.setData((Object)file);
            mi.addListener(13, l);
            ++i;
        }
    }

    public File getFileFromMenuItem(MenuItem mi, Menu fileHistoryMenu) {
        if (mi.getParent() == fileHistoryMenu) {
            File file = (File)mi.getData();
            return file;
        }
        return null;
    }

    public List<File> getFileHistory() {
        return this.fileHistory;
    }
}

