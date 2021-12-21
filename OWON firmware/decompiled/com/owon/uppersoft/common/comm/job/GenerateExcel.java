/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jxl.Workbook
 *  jxl.format.Alignment
 *  jxl.format.CellFormat
 *  jxl.format.UnderlineStyle
 *  jxl.write.Label
 *  jxl.write.WritableCell
 *  jxl.write.WritableCellFormat
 *  jxl.write.WritableFont
 *  jxl.write.WritableSheet
 *  jxl.write.WritableWorkbook
 *  jxl.write.WriteException
 *  jxl.write.biff.RowsExceededException
 */
package com.owon.uppersoft.common.comm.job;

import com.owon.uppersoft.common.comm.job.GenerateOrder;
import com.owon.uppersoft.common.inject.i18n.MsgCenter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class GenerateExcel {
    private WritableWorkbook workbook = null;
    private String pathname;

    public GenerateExcel(String pathname) {
        this.pathname = pathname;
    }

    public void outputExcel(List<GenerateOrder> list) {
        File file = new File(this.pathname);
        WritableSheet sheet = null;
        try {
            this.workbook = Workbook.createWorkbook((File)file);
            sheet = this.workbook.createSheet(MsgCenter.getString("order_formula"), 0);
            WritableFont font = new WritableFont(WritableFont.createFont((String)"\u7039\u5b29\u7d8b"), 11, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
            WritableCellFormat cellFormat = new WritableCellFormat(font);
            cellFormat.setAlignment(Alignment.CENTRE);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            sheet.setColumnView(2, 30);
            sheet.addCell((WritableCell)new Label(0, 0, MsgCenter.getString("XDS.xlsnum"), (CellFormat)cellFormat));
            sheet.addCell((WritableCell)new Label(1, 0, MsgCenter.getString("XDS.xlssn"), (CellFormat)cellFormat));
            sheet.addCell((WritableCell)new Label(2, 0, MsgCenter.getString("XDS.xlsfunction"), (CellFormat)cellFormat));
            int i = 0;
            while (i < list.size()) {
                this.addShell(list, sheet, i + 1, i);
                ++i;
            }
            this.workbook.write();
            this.workbook.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public void addShell(List<GenerateOrder> list, WritableSheet sheet, int line, int loc) {
        WritableFont font = new WritableFont(WritableFont.createFont((String)"\u7039\u5b29\u7d8b"), 11, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
        WritableCellFormat cellFormat = new WritableCellFormat(font);
        GenerateOrder go = list.get(loc);
        try {
            cellFormat.setWrap(true);
            sheet.addCell((WritableCell)new Label(0, line, go.getOrderNum(), (CellFormat)cellFormat));
            sheet.addCell((WritableCell)new Label(1, line, go.getSn(), (CellFormat)cellFormat));
            sheet.addCell((WritableCell)new Label(2, line, go.getFunction(), (CellFormat)cellFormat));
        }
        catch (RowsExceededException e) {
            e.printStackTrace();
        }
        catch (WriteException e) {
            e.printStackTrace();
        }
    }
}

