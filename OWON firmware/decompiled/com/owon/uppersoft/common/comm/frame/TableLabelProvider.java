/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.viewers.ITableLabelProvider
 *  org.eclipse.jface.viewers.LabelProvider
 *  org.eclipse.swt.graphics.Image
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.comm.frame.MainFrm;
import com.owon.uppersoft.common.comm.job.model.FileDataFile;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

class TableLabelProvider
extends LabelProvider
implements ITableLabelProvider {
    public String getColumnText(Object element, int columnIndex) {
        FileDataFile df = (FileDataFile)element;
        switch (columnIndex) {
            case 0: {
                return df.getType();
            }
            case 1: {
                return df.getPath();
            }
            case 2: {
                return null;
            }
            case 3: {
                switch (df.getStatus()) {
                    case 1: {
                        return MsgCenter.getString("IC.success");
                    }
                    case 2: {
                        return MsgCenter.getString("IC.fail");
                    }
                    case 3: {
                        return "*";
                    }
                }
                return "?";
            }
        }
        return null;
    }

    public Image getColumnImage(Object element, int columnIndex) {
        if (columnIndex == 0) {
            return null;
        }
        if (columnIndex == 3) {
            return MainFrm.EmptyImage;
        }
        return null;
    }
}

