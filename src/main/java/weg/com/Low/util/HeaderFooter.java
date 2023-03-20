package weg.com.Low.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;

public class HeaderFooter extends PdfPageEventHelper {
    @SneakyThrows
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase header = new Phrase("WEG");
        Image    image = Image.getInstance("weg-logo.png");
        image.scaleAbsolute(50, 50);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, header, document.left(), document.top() + 10, 0);
        cb.addImage(image, 0, 0, 50, 50, document.left(), document.top() + 10);
    }
}
