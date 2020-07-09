package com.ky.ykt.excle;

import java.io.Serializable;

public class ExcelStyle implements Serializable{
	private int columnWidth = 25;
	
	private String sheetName = "sheet1";
	
	private String xlsName = "export";

	public String getXlsName() {
		return xlsName;
	}

	public void setXlsName(String xlsName) {
		this.xlsName = xlsName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}
}
