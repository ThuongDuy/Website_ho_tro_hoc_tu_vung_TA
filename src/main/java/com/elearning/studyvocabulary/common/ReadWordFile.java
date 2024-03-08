package com.elearning.studyvocabulary.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class ReadWordFile {
	public static List<String> read(String path) {
		List<String> result = new ArrayList<String>();
		
        try {
            FileInputStream fis = new FileInputStream(path);
            XWPFDocument document = new XWPFDocument(fis);

            for (XWPFParagraph paragraph : document.getParagraphs()) {
            	result.add(paragraph.getText());
            }

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
