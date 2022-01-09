/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.dnd.DropTargetAdapter
 *  org.eclipse.swt.dnd.DropTargetEvent
 *  org.eclipse.swt.dnd.FileTransfer
 */
package com.owon.uppersoft.common.utils;

import com.owon.uppersoft.common.aspect.Linkable;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;

public class CDropTargetAdapter
extends DropTargetAdapter {
    private Linkable linkable;
    private FileTransfer fileTransfer = FileTransfer.getInstance();

    public CDropTargetAdapter(Linkable linkable) {
        this.linkable = linkable;
    }

    public void dragEnter(DropTargetEvent event) {
        if (event.detail == 16) {
            event.detail = 1;
        }
    }

    public void dragOperationChanged(DropTargetEvent event) {
        if (event.detail == 16) {
            event.detail = 1;
        }
    }

    public void dragOver(DropTargetEvent event) {
        event.detail = 0;
        int index = 0;
        while (index < event.dataTypes.length) {
            if (this.fileTransfer.isSupportedType(event.dataTypes[index])) break;
            ++index;
        }
        if (index >= event.dataTypes.length) {
            return;
        }
        event.currentDataType = event.dataTypes[index];
        event.detail = 1;
    }

    public void drop(DropTargetEvent event) {
        if (!this.fileTransfer.isSupportedType(event.currentDataType)) {
            return;
        }
        String[] files = (String[])event.data;
        if (files == null || files.length <= 0) {
            return;
        }
        this.linkable.link(files[0]);
    }
}

