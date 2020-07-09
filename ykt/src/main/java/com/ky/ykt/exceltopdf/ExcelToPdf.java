package com.ky.ykt.exceltopdf;

/**
 * @ClassName Ass
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/28
 **/

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ExcelToPdf {
    public static void main(String[] args) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4, 0, 0, 50, 0);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:/PdfTable2.pdf"));

        //字体设置
        /*
         * 由于itext不支持中文，所以需要进行字体的设置，我这里让itext调用windows系统的中文字体，
         * 找到文件后，打开属性，将文件名及所在路径作为字体名即可。
         */
        //创建BaseFont对象，指明字体，编码方式,是否嵌入
        BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\simkai.ttf", BaseFont.IDENTITY_H, false);
        //创建Font对象，将基础字体对象，字体大小，字体风格
        Font font = new Font(bf, 13, Font.NORMAL);
        int rowNum = 0;
        int colNum = 0;
        try {
            Workbook workbook = Workbook.getWorkbook(new File("F:\\数据库转换工具1.0\\db_pull\\tomcat\\webapps\\ROOT\\upload\\huamingce20200528111124.xls"));

            Sheet sheet = workbook.getSheet("2020农机购置补贴发放花名表");
            int column = sheet.getColumns();

            //下面是找出表格中的空行和空列
            List<Integer> nullCol = new ArrayList<>();
            List<Integer> nullRow = new ArrayList<>();
            for (int j = 0; j < sheet.getColumns(); j++) {
                int nullColNum = 0;
                for (int i = 0; i < sheet.getRows(); i++) {
                    Cell cell = sheet.getCell(j, i);
                    String str = cell.getContents();
                    if (str == null || "".equals(str)) {
                        nullColNum++;
                    }
                }
                if (nullColNum == sheet.getRows()) {
                    nullCol.add(j);
                    column--;
                }
            }

            for (int i = 0; i < sheet.getRows(); i++) {
                int nullRowNum = 0;
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell cell = sheet.getCell(j, i);
                    String str = cell.getContents();
                    if (str == null || "".equals(str)) {
                        nullRowNum++;
                    }
                }
                if (nullRowNum == sheet.getColumns()) {
                    nullRow.add(i);
                }
            }
            PdfPTable table = new PdfPTable(column);
            Range[] ranges = sheet.getMergedCells();

            PdfPCell cell1 = new PdfPCell();
            for (int i = 0; i < sheet.getRows(); i++) {
                if (nullRow.contains(i)) {    //如果这一行是空行，这跳过这一行
                    continue;
                }
                for (int j = 0; j < sheet.getColumns(); j++) {
                    if (nullCol.contains(j)) {    //如果这一列是空列，则跳过这一列
                        continue;
                    }
                    boolean flag = true;
                    Cell cell = sheet.getCell(j, i);
                    String str = cell.getContents();
                    for (Range range : ranges) {    //合并的单元格判断和处理
                        if (j >= range.getTopLeft().getColumn() && j <= range.getBottomRight().getColumn()
                                && i >= range.getTopLeft().getRow() && i <= range.getBottomRight().getRow()) {
                            if (str == null || "".equals(str)) {
                                flag = false;
                                break;
                            }
                            rowNum = range.getBottomRight().getRow() - range.getTopLeft().getRow() + 1;
                            colNum = range.getBottomRight().getColumn() - range.getTopLeft().getColumn() + 1;
                            if (rowNum > colNum) {
                                cell1 = mergeRow(str, font, rowNum);
                                cell1.setColspan(colNum);
                                table.addCell(cell1);
                            } else {
                                cell1 = mergeCol(str, font, colNum);
                                cell1.setRowspan(rowNum);
                                table.addCell(cell1);
                            }
                            //System.out.println(num1 + "  " + num2);
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        table.addCell(getPDFCell(str, font));
                    }
                }
            }

            workbook.close();
            document.open();
            document.add(table);
            document.close();
            writer.close();
        } catch (BiffException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //合并行的静态函数
    public static PdfPCell mergeRow(String str, Font font, int i) {

        //创建单元格对象，将内容及字体传入
        PdfPCell cell = new PdfPCell(new Paragraph(str, font));
        //设置单元格内容居中
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //将该单元格所在列包括该单元格在内的i行单元格合并为一个单元格
        cell.setRowspan(i);

        return cell;
    }

    //合并列的静态函数
    public static PdfPCell mergeCol(String str, Font font, int i) {

        PdfPCell cell = new PdfPCell(new Paragraph(str, font));
        cell.setMinimumHeight(25);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //将该单元格所在行包括该单元格在内的i列单元格合并为一个单元格
        cell.setColspan(i);

        return cell;
    }

    //获取指定内容与字体的单元格
    public static PdfPCell getPDFCell(String string, Font font) {
        //创建单元格对象，将内容与字体放入段落中作为单元格内容
        PdfPCell cell = new PdfPCell(new Paragraph(string, font));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        //设置最小单元格高度
        cell.setMinimumHeight(25);
        return cell;
    }

}
