package com.ky.ykt.excle;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * 导出Excel公共方法
 */
public class ExportExcel {

	public static void export(String[] rowName, List<String[]> dataList,ExcelStyle style ,HttpServletResponse response) throws Exception {

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + style.getXlsName() + ".xls");

		OutputStream out = response.getOutputStream();
		export(rowName, dataList, style,out);
	}

	/*
	 * 导出数据
	 */
	public static void export(String[] rowName, List<String[]> dataList, ExcelStyle style , OutputStream out) throws Exception {
		if (style == null) {
			style = new ExcelStyle();
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(style.getSheetName());
		HSSFRow row = sheet.createRow((int) 0);
		for (int i = 0; i < rowName.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(rowName[i]);
			//cell.setCellStyle(getColumnTopStyle(wb));
			sheet.setColumnWidth(i, 256 * style.getColumnWidth());
		}

		for (int i = 0; i < dataList.size(); i++) {
			row = sheet.createRow(i + 1);
			String[] data = dataList.get(i);
			for (int j = 0; j < data.length; j++) {
				row.createCell(j).setCellValue(data[j]);
			}
		}
		wb.write(out);
		out.flush();
		out.close();
	}

	/*
	 * 列头单元格样式
	 *//*
	public static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		font.setFontHeightInPoints((short) 11);
		// 字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		return style;

	}

	*//*
	 * 列数据信息单元格样式
	 *//*
	public static HSSFCellStyle getStyle(HSSFWorkbook workbook) {
		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		// font.setFontHeightInPoints((short)10);
		// 字体加粗
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		return style;

	}*/
}