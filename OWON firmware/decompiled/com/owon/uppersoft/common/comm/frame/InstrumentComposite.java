/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.viewers.CellEditor
 *  org.eclipse.jface.viewers.CheckboxTableViewer
 *  org.eclipse.jface.viewers.ColumnLayoutData
 *  org.eclipse.jface.viewers.ColumnWeightData
 *  org.eclipse.jface.viewers.IBaseLabelProvider
 *  org.eclipse.jface.viewers.ICellModifier
 *  org.eclipse.jface.viewers.IContentProvider
 *  org.eclipse.jface.viewers.TableLayout
 *  org.eclipse.jface.viewers.TableViewer
 *  org.eclipse.jface.viewers.TextCellEditor
 *  org.eclipse.swt.custom.TableEditor
 *  org.eclipse.swt.layout.GridData
 *  org.eclipse.swt.layout.GridLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Control
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.FileDialog
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Listener
 *  org.eclipse.swt.widgets.Table
 *  org.eclipse.swt.widgets.TableColumn
 *  org.eclipse.swt.widgets.TableItem
 *  org.eclipse.swt.widgets.Widget
 */
package com.owon.uppersoft.common.comm.frame;

import com.owon.uppersoft.common.comm.frame.ContentProvider;
import com.owon.uppersoft.common.comm.frame.MyCellModifier;
import com.owon.uppersoft.common.comm.frame.TableLabelProvider;
import com.owon.uppersoft.common.comm.job.BatchJob;
import com.owon.uppersoft.common.comm.job.model.FileDataFile;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

public class InstrumentComposite {
    private Composite com;
    private CheckboxTableViewer ctv;
    private BatchJob bj;
    private Table table;
    private static final String BrowseItem = "BI";
    private CSelectionListener cselect = new CSelectionListener();

    public BatchJob getBatchJob() {
        return this.bj;
    }

    public CheckboxTableViewer getCheckboxTableViewer() {
        return this.ctv;
    }

    public Composite getComposite() {
        return this.com;
    }

    public InstrumentComposite(Composite parent, BatchJob bj) {
        this.bj = bj;
        this.com = new Composite(parent, 0);
        GridLayout gridLayout = new GridLayout();
        this.com.setLayout((Layout)gridLayout);
        this.ctv = CheckboxTableViewer.newCheckList((Composite)this.com, (int)67584);
        this.customizeContents();
        this.init();
    }

    protected void customizeContents() {
        this.ctv.setContentProvider((IContentProvider)new ContentProvider());
        this.ctv.setLabelProvider((IBaseLabelProvider)new TableLabelProvider());
        this.table = this.ctv.getTable();
        this.table.setLinesVisible(true);
        this.table.setHeaderVisible(true);
        this.table.setLayoutData((Object)new GridData(4, 4, true, true));
        TableLayout tl = new TableLayout();
        this.table.setLayout((Layout)tl);
        tl.addColumnData((ColumnLayoutData)new ColumnWeightData(3));
        TableColumn tc = new TableColumn(this.table, 16384);
        tc.setText(MsgCenter.getString("IC.fileType"));
        tl.addColumnData((ColumnLayoutData)new ColumnWeightData(8));
        tc = new TableColumn(this.table, 16384);
        tc.setText(MsgCenter.getString("IC.path"));
        tl.addColumnData((ColumnLayoutData)new ColumnWeightData(2));
        tc = new TableColumn(this.table, 16384);
        tc.setText(MsgCenter.getString("IC.browse"));
        tl.addColumnData((ColumnLayoutData)new ColumnWeightData(2));
        tc = new TableColumn(this.table, 16384);
        tc.setText(MsgCenter.getString("IC.status"));
        String[] columnProperties = new String[]{"0", "1", "2", "3"};
        this.ctv.setColumnProperties(columnProperties);
        CellEditor[] cellEditor = new CellEditor[columnProperties.length];
        cellEditor[0] = null;
        cellEditor[1] = new TextCellEditor((Composite)this.table);
        cellEditor[2] = null;
        cellEditor[3] = null;
        this.ctv.setCellEditors(cellEditor);
        this.ctv.setCellModifier((ICellModifier)new MyCellModifier((TableViewer)this.ctv));
    }

    protected void init() {
        this.ctv.setInput((Object)this.bj);
        int SWT_Selection = 13;
        int count = this.table.getItemCount();
        int i = 0;
        while (i < count) {
            TableEditor te = new TableEditor(this.table);
            te.grabVertical = true;
            te.grabHorizontal = true;
            Button btnBrowse = new Button((Composite)this.table, 8);
            btnBrowse.setText(MsgCenter.getString("IC.browse"));
            btnBrowse.addListener(SWT_Selection, (Listener)this.cselect);
            TableItem ti = this.table.getItem(i);
            btnBrowse.setData(BrowseItem, (Object)ti);
            te.setEditor((Control)btnBrowse, ti, 2);
            ++i;
        }
    }

    public void setChecked() {
        List<FileDataFile> list = this.bj.getList();
        ArrayList<FileDataFile> list1 = new ArrayList<FileDataFile>();
        for (FileDataFile di : list) {
            if (!di.isSelected()) continue;
            list1.add(di);
        }
        this.ctv.setCheckedElements(list1.toArray());
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
            if (o instanceof Button) {
                String path;
                Button btn = (Button)o;
                FileDialog fd = new FileDialog(InstrumentComposite.this.com.getShell());
                String s = (String)InstrumentComposite.this.table.getData();
                if (s != null) {
                    fd.setFilterPath(s);
                }
                if ((path = fd.open()) == null) {
                    return;
                }
                TableItem ti = (TableItem)btn.getData(InstrumentComposite.BrowseItem);
                FileDataFile df = (FileDataFile)ti.getData();
                df.setPath(path);
                df.setStatus(0);
                InstrumentComposite.this.ctv.refresh();
            }
        }
    }
}

