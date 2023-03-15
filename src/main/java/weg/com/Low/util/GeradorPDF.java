package weg.com.Low.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import weg.com.Low.model.entity.Demanda;

import java.io.FileOutputStream;
@Component
public class GeradorPDF {
    public void gerarPDF(Demanda demanda) {
        try {

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("exemplo.pdf"));
            document.open();
            document.addTitle("demanda.getTituloDemanda()");
            Paragraph paragraph = new Paragraph("testando o negocinho2");
            paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragraph);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
