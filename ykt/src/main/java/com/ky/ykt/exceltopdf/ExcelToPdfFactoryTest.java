package com.ky.ykt.exceltopdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class ExcelToPdfFactoryTest {

    public static void main(String args[]) {
        OutputStream os = null;
        try {
            os = new FileOutputStream("F:/PdfTable2.pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ExcelToPdfFactory.execute("F:\\数据库转换工具1.0\\db_pull\\tomcat\\webapps\\ROOT\\upload\\huamingce20200528111124.xls", os);
    }

}
