/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.viewers.IStructuredContentProvider
 *  org.eclipse.jface.viewers.Viewer
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.comm.job.BatchJob;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

class ContentProvider
implements IStructuredContentProvider {
    ContentProvider() {
    }

    public Object[] getElements(Object inputElement) {
        BatchJob bj = (BatchJob)inputElement;
        return bj.getList().toArray();
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}

