/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jxl.Cell
 *  jxl.Workbook
 *  jxl.format.Alignment
 *  jxl.format.Border
 *  jxl.format.BorderLineStyle
 *  jxl.format.CellFormat
 *  jxl.format.Colour
 *  jxl.format.UnderlineStyle
 *  jxl.format.VerticalAlignment
 *  jxl.read.biff.BiffException
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

import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class OperateExcel {
    public static void modifyExcel(File file, String orderNum, String item, String sn, String plc, int row) {
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;
        WritableCellFormat wcf1 = OperateExcel.getDataCellFormat(0);
        WritableCellFormat wcf2 = OperateExcel.getDataCellFormat(1);
        try {
            Workbook rwb = Workbook.getWorkbook((File)file);
            workbook = Workbook.createWorkbook((File)file, (Workbook)rwb);
            sheet = workbook.getSheet(0);
            Label numLabel = new Label(0, row, orderNum, (CellFormat)wcf2);
            sheet.addCell((WritableCell)numLabel);
            Label itemLabel = new Label(1, row, item, (CellFormat)wcf2);
            sheet.addCell((WritableCell)itemLabel);
            Label labelNum = new Label(2, row, sn, (CellFormat)wcf1);
            sheet.addCell((WritableCell)labelNum);
            Label label = new Label(5, row, plc, (CellFormat)wcf2);
            sheet.addCell((WritableCell)label);
            workbook.write();
            workbook.close();
        }
        catch (BiffException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (RowsExceededException e) {
            e.printStackTrace();
        }
        catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public static WritableCellFormat getDataCellFormat(int color) {
        WritableCellFormat wcf = null;
        WritableFont wf = null;
        try {
            switch (color) {
                case 0: {
                    wf = new WritableFont(WritableFont.createFont((String)"\u7039\u5b29\u7d8b"), 14, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
                    break;
                }
                case 1: {
                    wf = new WritableFont(WritableFont.createFont((String)"\u7039\u5b29\u7d8b"), 14, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                    break;
                }
            }
            wcf = new WritableCellFormat(wf);
            wcf.setAlignment(Alignment.CENTRE);
            wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
            wcf.setBorder(Border.TOP, BorderLineStyle.THIN);
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            wcf.setBackground(Colour.WHITE);
        }
        catch (WriteException e) {
            e.printStackTrace();
        }
        return wcf;
    }

    public static void generateExcel(File file1, File file2) {
        WritableWorkbook ww = null;
        try {
            Workbook wb = Workbook.getWorkbook((File)file1);
            ww = Workbook.createWorkbook((File)file2, (Workbook)wb);
            ww.write();
            ww.close();
        }
        catch (BiffException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (RowsExceededException e) {
            e.printStackTrace();
        }
        catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public static boolean addExcel(File file, String value, int column) {
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;
        boolean succ_fail = false;
        try {
            Workbook rwb = Workbook.getWorkbook((File)file);
            workbook = Workbook.createWorkbook((File)file, (Workbook)rwb);
            sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            System.out.println(rows);
            if (!OperateExcel.compareContent(value, sheet, column, rows)) {
                Label labelNum = new Label(column, rows, value);
                sheet.addCell((WritableCell)labelNum);
                succ_fail = true;
            } else {
                succ_fail = false;
            }
            workbook.write();
            workbook.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (WriteException e) {
            e.printStackTrace();
        }
        catch (BiffException e) {
            e.printStackTrace();
        }
        return succ_fail;
    }

    public static boolean compareContent(String value, WritableSheet sheet, int column, int rows) {
        int i = 0;
        while (i < rows) {
            Cell cell = sheet.getCell(column, i);
            String content = cell.getContents();
            String rowValue = null;
            if (content != null && content.length() != 0) {
                rowValue = content;
            }
            if (value.endsWith(rowValue)) {
                return true;
            }
            ++i;
        }
        return false;
    }

    public static String getExcel(File file, int page, int col, int row) {
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;
        String content = null;
        try {
            Workbook rwb = Workbook.getWorkbook((File)file);
            workbook = Workbook.createWorkbook((File)file, (Workbook)rwb);
            sheet = workbook.getSheet(page);
            Cell cell = sheet.getCell(col, row);
            content = cell.getContents();
            workbook.write();
            workbook.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (WriteException e) {
            e.printStackTrace();
        }
        catch (BiffException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static int getRows(File file, int page) {
        int rows = 0;
        try {
            Workbook rwb = Workbook.getWorkbook((File)file);
            WritableWorkbook workbook = Workbook.createWorkbook((File)file, (Workbook)rwb);
            WritableSheet sheet = workbook.getSheet(page);
            rows = sheet.getRows();
            workbook.write();
            workbook.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
}

