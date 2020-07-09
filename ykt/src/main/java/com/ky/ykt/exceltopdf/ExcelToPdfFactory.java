package com.ky.ykt.exceltopdf;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


import com.itextpdf.text.PageSize;

public class ExcelToPdfFactory implements ConvertFactory{
	
	public static void execute(String srcPath, OutputStream os){
		new ExcelToPdfFactory().convert(srcPath, os);
	}
	
	@Override
	public void convert(String srcPath, OutputStream os) {
		Workbook wb = createdWorkBook(srcPath);
		WorkBookStruct wbStruct = new WorkBookStruct(wb);
		PdfBuilder builder = new PdfBuilder(os);
		builder.buildDocument()
		.buildPageSetting(30, 30, 30, 30)
		.buildPageSetting(PageSize.A4);
		for(Sheet sheet : wbStruct.getSheets()){
			SheetStruct sheetStruct = new SheetStruct(sheet);
			try {
				builder.buildTable(sheetStruct);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		builder.build();
	}
	
	
	private Workbook createdWorkBook(String srcPath){
		File src = new File(srcPath);
		Workbook wb;
		try {
			wb = WorkbookFactory.create(src);
			return wb;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
