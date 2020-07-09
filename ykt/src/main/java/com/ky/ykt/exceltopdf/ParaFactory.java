package com.ky.ykt.exceltopdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

public class ParaFactory {
    private static Font defFont() {
		BaseFont bf = null;
		try {
			bf = BaseFont.createFont("C:\\Windows\\Fonts\\simkai.ttf", BaseFont.IDENTITY_H, false);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Font font = new Font(bf, 10, Font.NORMAL);
        return font;
    }

    public static Paragraph size10(String content) {
        Paragraph p = new Paragraph(content, defFont());
        p.getFont().setSize(10f);
        return p;
    }

    public static Paragraph size8(String content) {
        Paragraph p = new Paragraph(content, defFont());
        p.getFont().setSize(8f);
        return p;
    }
}
