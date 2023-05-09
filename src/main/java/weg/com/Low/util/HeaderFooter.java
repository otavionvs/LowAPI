package weg.com.Low.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;

public class HeaderFooter extends PdfPageEventHelper {
//    public void onStartPage(PdfWriter writer,Document document) {
//        try{
    //            PdfContentByte cb = writer.getDirectContent();
    //            Image imgSoc = Image.getInstance("weg-logo.png");
    //            imgSoc.scaleToFit(60,60);
    //            imgSoc.setAbsolutePosition(document.getPageSize().getWidth() - 90, document.getPageSize().getHeight() - 70);
    //            cb.addImage(imgSoc);
//
//
//            Rectangle rect = writer.getBoxSize("header");
//            rect.setBottom(rect.getTop() - 50);
//            ColumnText.showTextAligned(writer.getDirectContent(),
//                    Element.ALIGN_CENTER, new Phrase("Cabeçalho"),
//                    (rect.getLeft() + rect.getRight()) / 2, rect.getTop(), 0);
//
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }

    private final Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private final Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
//        Phrase header = new Phrase("1", headerFont);

        float headerX = document.leftMargin();
        float headerY = document.topMargin()-50;
        float footerX = document.leftMargin();
        float footerY = document.bottom() - 10;

//        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, header, headerX, document.top() + 30, 0);

        try {

            Image logo = Image.getInstance("weg-logo.png");
            logo.scaleToFit(60, 60);
            logo.setAbsolutePosition(document.getPageSize().getWidth() - 90, document.getPageSize().getHeight() - 70);
            cb.addImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

