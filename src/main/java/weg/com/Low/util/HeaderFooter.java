package weg.com.Low.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;

public class HeaderFooter extends PdfPageEventHelper {
    public void onStartPage(PdfWriter writer,Document document) {
        try{
            PdfContentByte cb = writer.getDirectContent();


            System.out.println();

            Image imgSoc = Image.getInstance("weg-logo.png");
            imgSoc.scaleToFit(60,60);
            imgSoc.setAbsolutePosition(document.getPageSize().getWidth() - 90, document.getPageSize().getHeight() - 70);
            cb.addImage(imgSoc);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
