package com.ky.ykt.excle;


import com.ky.ykt.exceltopdf.ExcelToPdfFactory;
import com.ky.ykt.utils.PathUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.poi.ss.usermodel.VerticalAlignment.CENTER;

/**
 * 导出Excel公共方法
 */
public class ExportHM {

    public static void exportHM(List<String[]> dataList, ExcelHMStyle style, HttpServletResponse response) throws Exception {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + style.getXlsName() + ".xls");

        OutputStream out = response.getOutputStream();
        exporthm(dataList, style);
    }

    /*
     * 导出数据
     */
    public static String exporthm(List<String[]> dataList, ExcelHMStyle style) throws Exception {
        if (style == null) {
            style = new ExcelHMStyle();
        }
        //获得一个工作薄
        Date date = new Date();
        HSSFWorkbook wb = new HSSFWorkbook();
        //为第一个sheet命名
        HSSFSheet sheet = wb.createSheet(style.getSheetName());
        //设置excel密码锁
        //sheet.protectSheet("123456");
        //设置自适应列宽
        sheet.autoSizeColumn(1, true);
        sheet.autoSizeColumn(2, true);
        sheet.autoSizeColumn(3, true);
        sheet.autoSizeColumn(4, true);
        sheet.autoSizeColumn(5, true);
        sheet.autoSizeColumn(6, true);
        sheet.autoSizeColumn(7, true);
        sheet.autoSizeColumn(8, true);
        sheet.autoSizeColumn(9, true);
        //创建第一行
        Row row1 = sheet.createRow(0);
        //获取第一个单元格(第一列)
        Cell cellA1 = row1.createCell(0);
        //给第一个单元格赋值
        cellA1.setCellValue("《" + style.getDocumentProjectType() + "》发放花名表");
        //标题设置
        cellA1.setCellStyle(getDocumentTitleStyle(wb));

        //创建第二行
        Row row2 = sheet.createRow(1);
        //给第二行第一个单元格
        Cell cellA2 = row2.createCell(0);
        cellA2.setCellValue("单据号");

        Cell cellB2 = row2.createCell(2);
        cellB2.setCellValue(style.getDocumentNumber());

        Cell cellC2 = row2.createCell(3);
        cellC2.setCellValue("日期");

        Cell cellD2 = row2.createCell(4);
        cellD2.setCellValue(style.getDocumentDate());

        Cell cellE2 = row2.createCell(5);
        cellE2.setCellValue("主管单位");

        Cell cellF2 = row2.createCell(6);
        cellF2.setCellValue(style.getDocumentCompetent());

        //创建第三行
        Row row3 = sheet.createRow(2);
        Cell cellA3 = row3.createCell(0);
        cellA3.setCellValue("备注");

        Cell cellC3 = row3.createCell(2);
        cellC3.setCellValue(style.getDocumentRemark());

        Cell cellE3 = row3.createCell(5);
        cellE3.setCellValue("审核意见");

        Cell cellF3 = row3.createCell(6);
        cellF3.setCellValue(style.getDocumentOpinion());

        //创建第四行
        Row row4 = sheet.createRow(3);
        Cell cellA4 = row4.createCell(0);
        cellA4.setCellValue("单据名称");

        Cell cellC4 = row4.createCell(2);
        cellC4.setCellValue(style.getDocumentBillName());

        Cell cellE4 = row4.createCell(5);
        cellE4.setCellValue("负责人(签字)");

        Cell cellF4 = row4.createCell(6);
        cellF4.setCellValue(style.getDocumentPrincipalPerson());

        Cell cellG4 = row4.createCell(7);
        cellG4.setCellValue("经办人(签字)");

        Cell cellH4 = row4.createCell(8);
        cellH4.setCellValue(style.getDocumentResponsiblePerson());

        //创建第五行
        Row row5 = sheet.createRow(4);
        Cell cellA5 = row5.createCell(0);
        cellA5.setCellValue("资金类型");

        Cell cellC5 = row5.createCell(2);
        cellC5.setCellValue(style.getDocumentProjectType());

        //创建第六行
        /*
        HSSFRow row6 = sheet.createRow((int) 5);
        for (int i = 0; i < rowName.length; i++) {
            HSSFCell cell = row6.createCell(i);
            cell.setCellValue(rowName[i]);
            //cell.setCellStyle(getColumnTopStyle(wb));
            sheet.setColumnWidth(i, 256 * style.getColumnWidth());
        }
        */

        //创建第六行
        Row row6 = sheet.createRow(5);
        Cell cellA6 = row6.createCell(0);
        cellA6.setCellValue("序号");
        Cell cellB6 = row6.createCell(1);
        cellB6.setCellValue("姓名");
        Cell cellC6 = row6.createCell(2);
        cellC6.setCellValue("身份证号");
        Cell cellD6 = row6.createCell(3);
        cellD6.setCellValue("行政区域");
        Cell cellE6 = row6.createCell(5);
        cellE6.setCellValue("社保卡号");
        Cell cellF6 = row6.createCell(7);
        cellF6.setCellValue("金额");
        Cell cellG6 = row6.createCell(8);
        cellG6.setCellValue("备注");
        HSSFCellStyle documentStyle = getDocumentStyle(wb);
        HSSFCellStyle smallTitleStyle = getSmallTitleStyle(wb);
        HSSFCellStyle documentHeaderStyle = getDocumentHeaderStyle(wb);
        for (int i = 0; i < dataList.size(); i++) {
            //每循环一行，创建一行
            Row row = sheet.createRow(i + 6);
            row.setHeightInPoints(25);
            //合并行政区域与社保卡号
            CellRangeAddress cellRangeAddress15 = new CellRangeAddress(i + 6, i + 6, 3, 4);
            CellRangeAddress cellRangeAddress16 = new CellRangeAddress(i + 6, i + 6, 5, 6);
            sheet.addMergedRegion(cellRangeAddress15);
            sheet.addMergedRegion(cellRangeAddress16);
            //给单元格加边框
            RegionUtil.setBorderLeft(1, cellRangeAddress15, sheet);
            RegionUtil.setBorderBottom(1, cellRangeAddress15, sheet);
            RegionUtil.setBorderRight(1, cellRangeAddress15, sheet);
            RegionUtil.setBorderTop(1, cellRangeAddress15, sheet);
            RegionUtil.setBorderLeft(1, cellRangeAddress16, sheet);
            RegionUtil.setBorderBottom(1, cellRangeAddress16, sheet);
            RegionUtil.setBorderRight(1, cellRangeAddress16, sheet);
            RegionUtil.setBorderTop(1, cellRangeAddress16, sheet);
            //进行赋值
            String[] data = dataList.get(i);
            for (int j = 0; j < data.length; j++) {
                //row.createCell(j).setCellValue(data[j]);
                //进行赋值----序号
                Cell cell0 = row.createCell(j);
                cell0.setCellValue(Integer.valueOf(i + 1));
                //进行赋值----姓名
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(data[0]);
                //进行赋值----身份证号
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(data[1]);
                //进行赋值----区域
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(data[2]);
                //进行赋值----社保卡号
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(data[3]);
                //进行赋值----金额
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(data[4]);
                //进行赋值----备注
                Cell cell8 = row.createCell(8);
                cell8.setCellValue(data[5]);
                //内容样式
                cell0.setCellStyle(documentStyle);
                cell1.setCellStyle(documentStyle);
                //sheet.setColumnWidth(1,cell1.getStringCellValue().getBytes().length*256);
                cell2.setCellStyle(documentStyle);
                //sheet.setColumnWidth(2,cell2.getStringCellValue().getBytes().length*2*256);
                cell3.setCellStyle(documentStyle);
                //sheet.setColumnWidth(3,cell3.getStringCellValue().getBytes().length*256);
                cell5.setCellStyle(documentStyle);
                //sheet.setColumnWidth(5,cell5.getStringCellValue().getBytes().length*256);
                cell7.setCellStyle(documentStyle);
                //sheet.setColumnWidth(7,cell7.getStringCellValue().getBytes().length*256);
                cell8.setCellStyle(documentStyle);
                //sheet.setColumnWidth(8, cell8.getStringCellValue().getBytes().length * 256);
            }
        }

        Map<String, CellRangeAddress> map = new HashMap<String, CellRangeAddress>();

        //结尾最后一行
        Row rowLast = sheet.createRow(dataList.size() + 6);
        CellRangeAddress cellRangeAddress17 = new CellRangeAddress(dataList.size() + 6, dataList.size() + 6, 3, 5);
        map.put("cellRangeAddress17", cellRangeAddress17);
        CellRangeAddress cellRangeAddress18 = new CellRangeAddress(dataList.size() + 6, dataList.size() + 6, 0, 2);
        map.put("cellRangeAddress18", cellRangeAddress18);
        CellRangeAddress cellRangeAddress19 = new CellRangeAddress(dataList.size() + 6, dataList.size() + 6, 7, 8);
        map.put("cellRangeAddress19", cellRangeAddress19);
        /*
        sheet.addMergedRegion(cellRangeAddress17);
        sheet.addMergedRegion(cellRangeAddress18);
        sheet.addMergedRegion(cellRangeAddress19);
        */
        Cell cellLast6 = rowLast.createCell(6);
        cellLast6.setCellValue("合计");

        Cell cellLast7 = rowLast.createCell(7);
        cellLast7.setCellValue(style.getSumMoney());

        Cell cellLast0 = rowLast.createCell(0);
        cellLast0.setCellValue("合计（大写）");

        Cell cellLast3 = rowLast.createCell(3);
        cellLast3.setCellValue(style.getBigSumMoney());

        // 合并日期占两行(4个参数，分别为起始行，结束行，起始列，结束列)
        // 行和列都是从0开始计数，且起始结束都会合并
        // 这里是合并excel中日期的两行为一行
        //List list = new ArrayList();

        CellRangeAddress cellRangeAddress14 = new CellRangeAddress(0, 0, 0, 8);
        //map.put("cellRangeAddress14",cellRangeAddress14);
        sheet.addMergedRegion(cellRangeAddress14);
        CellRangeAddress cellRangeAddress1 = new CellRangeAddress(1, 1, 0, 1);
        map.put("cellRangeAddress1", cellRangeAddress1);
        CellRangeAddress cellRangeAddress2 = new CellRangeAddress(2, 2, 0, 1);
        map.put("cellRangeAddress2", cellRangeAddress2);
        CellRangeAddress cellRangeAddress3 = new CellRangeAddress(3, 3, 0, 1);
        map.put("cellRangeAddress3", cellRangeAddress3);
        CellRangeAddress cellRangeAddress4 = new CellRangeAddress(4, 4, 0, 1);
        map.put("cellRangeAddress4", cellRangeAddress4);
        CellRangeAddress cellRangeAddress5 = new CellRangeAddress(2, 2, 2, 4);
        map.put("cellRangeAddress5", cellRangeAddress5);
        CellRangeAddress cellRangeAddress6 = new CellRangeAddress(5, 5, 3, 4);
        map.put("cellRangeAddress6", cellRangeAddress6);
        CellRangeAddress cellRangeAddress7 = new CellRangeAddress(5, 5, 5, 6);
        map.put("cellRangeAddress7", cellRangeAddress7);
        CellRangeAddress cellRangeAddress8 = new CellRangeAddress(4, 4, 2, 4);
        map.put("cellRangeAddress8", cellRangeAddress8);
        CellRangeAddress cellRangeAddress9 = new CellRangeAddress(3, 4, 5, 5);
        map.put("cellRangeAddress9", cellRangeAddress9);
        CellRangeAddress cellRangeAddress10 = new CellRangeAddress(3, 4, 7, 7);
        map.put("cellRangeAddress10", cellRangeAddress10);
        CellRangeAddress cellRangeAddress11 = new CellRangeAddress(3, 3, 2, 4);
        map.put("cellRangeAddress11", cellRangeAddress11);
        CellRangeAddress cellRangeAddress12 = new CellRangeAddress(1, 1, 6, 8);
        map.put("cellRangeAddress12", cellRangeAddress12);
        CellRangeAddress cellRangeAddress13 = new CellRangeAddress(2, 2, 6, 8);
        map.put("cellRangeAddress13", cellRangeAddress13);
        CellRangeAddress cellRangeAddress20 = new CellRangeAddress(3, 4, 6, 6);
        map.put("cellRangeAddress20", cellRangeAddress20);
        CellRangeAddress cellRangeAddress21 = new CellRangeAddress(3, 4, 8, 8);
        map.put("cellRangeAddress21", cellRangeAddress21);
        /*
        sheet.addMergedRegion(cellRangeAddress);
        sheet.addMergedRegion(cellRangeAddress1);
        sheet.addMergedRegion(cellRangeAddress2);
        sheet.addMergedRegion(cellRangeAddress3);
        sheet.addMergedRegion(cellRangeAddress4);
        sheet.addMergedRegion(cellRangeAddress5);
        sheet.addMergedRegion(cellRangeAddress6);
        sheet.addMergedRegion(cellRangeAddress7);
        sheet.addMergedRegion(cellRangeAddress8);
        sheet.addMergedRegion(cellRangeAddress9);
        sheet.addMergedRegion(cellRangeAddress10);
        sheet.addMergedRegion(cellRangeAddress11);
        sheet.addMergedRegion(cellRangeAddress12);
        sheet.addMergedRegion(cellRangeAddress13);
        sheet.addMergedRegion(cellRangeAddress20);
        sheet.addMergedRegion(cellRangeAddress21);
        */
        for (String key : map.keySet()) {
            sheet.addMergedRegion(map.get(key));
            RegionUtil.setBorderLeft(1, map.get(key), sheet);
            RegionUtil.setBorderBottom(1, map.get(key), sheet);
            RegionUtil.setBorderRight(1, map.get(key), sheet);
            RegionUtil.setBorderTop(1, map.get(key), sheet);
        }

        //表体设置
        cellA2.setCellStyle(smallTitleStyle);
        cellB2.setCellStyle(documentStyle);
        cellC2.setCellStyle(smallTitleStyle);
        cellD2.setCellStyle(getDocumentDate(wb));
        cellE2.setCellStyle(smallTitleStyle);
        //sheet.setColumnWidth(4,cellE2.getStringCellValue().getBytes().length*256);
        cellF2.setCellStyle(documentStyle);
        cellA3.setCellStyle(smallTitleStyle);
        cellC3.setCellStyle(documentStyle);
        cellE3.setCellStyle(smallTitleStyle);
        cellF3.setCellStyle(documentStyle);
        cellA4.setCellStyle(smallTitleStyle);
        cellC4.setCellStyle(documentStyle);
        cellE4.setCellStyle(getPersonStyle(wb));
        //sheet.setColumnWidth(5,cellE4.getStringCellValue().getBytes().length*256);
        cellF4.setCellStyle(documentStyle);
        cellG4.setCellStyle(getPersonStyle(wb));
        //sheet.setColumnWidth(7,cellG4.getStringCellValue().getBytes().length*256);
        cellH4.setCellStyle(documentStyle);
        cellA5.setCellStyle(smallTitleStyle);
        cellC5.setCellStyle(documentStyle);
        cellA6.setCellStyle(documentHeaderStyle);
        sheet.setColumnWidth(0, cellA6.getStringCellValue().getBytes().length * 256);
        cellB6.setCellStyle(documentHeaderStyle);
        cellC6.setCellStyle(documentHeaderStyle);
        sheet.setColumnWidth(2, cellC6.getStringCellValue().getBytes().length * 2 * 256);
        cellD6.setCellStyle(documentHeaderStyle);
        cellE6.setCellStyle(documentHeaderStyle);
        cellF6.setCellStyle(documentHeaderStyle);
        cellG6.setCellStyle(documentHeaderStyle);
        cellLast6.setCellStyle(smallTitleStyle);
        cellLast0.setCellStyle(smallTitleStyle);
        cellLast7.setCellStyle(documentStyle);
        cellLast3.setCellStyle(documentStyle);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");

        String filepath = PathUtil.getClasspath() + "upload";
        File directoryFile = new File(filepath);
        directoryFile.mkdirs();
        File file = new File(filepath + "/huamingce" + sdf1.format(date) + ".xls");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        wb.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        OutputStream os = null;
        try {
            os = new FileOutputStream(filepath + "/huamingce" + sdf1.format(date) + ".pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ExcelToPdfFactory.execute(filepath + "/huamingce" + sdf1.format(date) + ".xls", os);

//
//        com.spire.xls.Workbook workbook = new com.spire.xls.Workbook();
//        workbook.loadFromFile(filepath + "/huamingce" + sdf1.format(date) + ".xls");
//        workbook.saveToFile(filepath + "/huamingce" + sdf1.format(date) + ".pdf", FileFormat.PDF);
//        try {
////            PrintTess.printFile("file:///" + filepath + "/huamingce" + sdf1.format(date) + ".pdf", "ds.pdf");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return "/upload" + "/huamingce" + sdf1.format(date) + ".pdf";
//        return filepath + "/huamingce" + sdf1.format(date) + ".pdf";
    }

    /*
     * 文档题目样式
     */
    public static HSSFCellStyle getDocumentTitleStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 18);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("黑体");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        //style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(CENTER);
        return style;
    }

    /*
     * 文档标题样式
     */
    public static HSSFCellStyle getSmallTitleStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 12);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("黑体");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        //style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(CENTER);
        //设置单元格边框
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        return style;
    }

    /*
     * 文档表头样式
     */
    public static HSSFCellStyle getDocumentHeaderStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 7);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("黑体");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        //style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(CENTER);
        //设置单元格边框
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        return style;
    }

    /*
     * 文档填充内容样式
     */
    public static HSSFCellStyle getDocumentStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 10);
        // 字体加粗
        font.setBold(false);
        // 设置字体名字
        font.setFontName("黑体");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(true);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(CENTER);
        //设置单元格边框
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        return style;
    }

    /*
     * 负责人、经办人
     */
    public static HSSFCellStyle getPersonStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 7);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("黑体");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        //style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(CENTER);
        //设置单元格边框
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        return style;
    }

    /*
     * 日期样式样式
     */
    public static HSSFCellStyle getDocumentDate(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 7);
        // 字体加粗
        font.setBold(false);
        // 设置字体名字
        font.setFontName("黑体");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(true);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(CENTER);
        //设置单元格边框
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        return style;
    }

    /**
     * 获取当前系统路径
     */
    public static String getUploadPath() {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!path.exists()) {
            path = new File("");
        }
        File upload = new File(path.getAbsolutePath(), "upload/");
        if (!upload.exists()) {
            upload.mkdirs();
        }
        return upload.getAbsolutePath();
    }

}