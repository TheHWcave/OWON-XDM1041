/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jxl.Cell
 *  jxl.Workbook
 *  jxl.write.WritableSheet
 *  jxl.write.WritableWorkbook
 *  jxl.write.WriteException
 */
package com.owon.uppersoft.common.comm.job;

import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class OperateXdsExcel {
    private WritableWorkbook workbook;

    public OperateXdsExcel(File file) {
        try {
            Workbook rwb = Workbook.getWorkbook((File)file);
            this.workbook = Workbook.createWorkbook((File)file, (Workbook)rwb);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getExcel(int page, int col, int row) {
        WritableSheet sheet = this.workbook.getSheet(page);
        Cell cell = sheet.getCell(col, row);
        String content = cell.getContents();
        return content;
    }

    public int getRow(int page) {
        WritableSheet sheet = this.workbook.getSheet(page);
        int row = sheet.getRows();
        return row;
    }

    public void close() {
        try {
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
}

