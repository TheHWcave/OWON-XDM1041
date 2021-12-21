/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.viewers.ICellModifier
 *  org.eclipse.jface.viewers.TableViewer
 *  org.eclipse.swt.widgets.TableItem
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.comm.job.model.FileDataFile;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

class MyCellModifier
implements ICellModifier {
    private TableViewer tv;

    public MyCellModifier(TableViewer tv) {
        this.tv = tv;
    }

    public boolean canModify(Object element, String property) {
        return true;
    }

    public Object getValue(Object element, String property) {
        FileDataFile df = (FileDataFile)element;
        if (property.equals("1")) {
            return df.getPath();
        }
        return null;
    }

    public void modify(Object element, String property, Object value) {
        TableItem tableItem = (TableItem)element;
        FileDataFile df = (FileDataFile)tableItem.getData();
        if (property.equals("1")) {
            String newValue = (String)value;
            df.setPath(newValue);
        }
        this.tv.update((Object)df, null);
    }
}

