package weg.com.Low.util;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import weg.com.Low.model.entity.Beneficio;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Departamento;
import weg.com.Low.model.entity.Usuario;

import javax.websocket.Decoder;
import java.io.ByteArrayOutputStream;
@Component
public class GeradorPDF {

    private final Font negritoFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private final Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

    public ByteArrayOutputStream gerarPDF(Demanda demanda) {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            document.setMargins(document.leftMargin(), document.rightMargin(), document.topMargin() + 50, document.bottomMargin());
            PdfWriter writer = PdfWriter.getInstance(document, baos);


            HeaderFooter header = new HeaderFooter();
            writer.setPageEvent(header);
            document.open();

            Paragraph departamento = new Paragraph();
            Paragraph objetivo = new Paragraph("Objetivo:", negritoFont);
            Paragraph situacaoAtual = new Paragraph("Situação Atual:", negritoFont);
            Paragraph beneficioReal = new Paragraph();
            Paragraph mbeneficioReal = new Paragraph("Memória de Cálculo do Benefício Real:", negritoFont);
            Paragraph beneficioPotencial = new Paragraph();
            Paragraph mbeneficioPotencial = new Paragraph("Memória de Cálculo do Benefício Potencial:", negritoFont);
            Paragraph beneficioQualitativo = new Paragraph("Beneficio Qualitativo:", negritoFont);
            Paragraph escopoProjeto = new Paragraph("Escopo do Projeto:", negritoFont);
            Paragraph anexos = new Paragraph("Anexos:", negritoFont);

            //Paragraph paragraph = new Paragraph();
            //paragraph.add(new Chunk("Solicitante: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            //paragraph.add(new Chunk("nomeDoSolicitante"));
            //
            //document.add(paragraph);

// Adicionando o conteúdo do cabeçalho do documento
            Paragraph conteudoTitulo = new Paragraph(demanda.getTituloDemanda(), negritoFont);
            conteudoTitulo.setAlignment(Element.ALIGN_CENTER);
            Paragraph conteudoObjetivo = new Paragraph(demanda.getObjetivoDemanda());
            Paragraph conteudoSituacaoAtual = new Paragraph(demanda.getSituacaoAtualDemanda());
            Paragraph conteudoMBeneficioReal = new Paragraph(demanda.getBeneficioRealDemanda().getMemoriaDeCalculoBeneficio());
            Paragraph conteudoMBeneficioPotencial = new Paragraph(demanda.getBeneficioPotencialDemanda().getValorBeneficio().toString());
            Paragraph conteudoBeneficioQualitativo = new Paragraph(demanda.getBeneficioQualitativoDemanda());

            Paragraph conteudoEscopoProjeto = new Paragraph("Ações de curto prazo visando ganhos globais de desempenho/estabilidade no MAESTRO Identificar causa raiz das instabilidades dos servidores (memória, CPU etc.)\n" +
                    "Otimizar estrutura das tabelas, consultas de baixo desempenho e criação/remoção de índices\n" +
                    "Otimizar configurações dos servidores (virtualização, balanceador de carga, recursos alocados,\n" +
                    "etc.)\n" +
                    "\uF0B7 Otimizar configurações dos servidores de aplicação (versão, parâmetros JVM, pool de conexões)o");
            Paragraph conteudoAnexos = new Paragraph("Arquivo 1.pdf\nArquivo 2.docx");

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            PdfPCell leftCell = new PdfPCell();
            Paragraph paragraph = new Paragraph();
            paragraph.add(new Chunk("Solicitante:", negritoFont));
            paragraph.add(new Chunk(demanda.getSolicitanteDemanda().getNomeUsuario(), normalFont));
            leftCell.addElement(paragraph);
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(leftCell);
            

            PdfPCell rightCell = new PdfPCell();
            Paragraph paragraph2 = new Paragraph();
            paragraph2.add(new Chunk("Data:", negritoFont));
            paragraph2.add(new Chunk(demanda.getDataCriacaoDemanda().toString(), normalFont));
            paragraph2.setAlignment(Element.ALIGN_RIGHT);
            rightCell.addElement(paragraph2);
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(rightCell);

            document.add(table);

            departamento.add(new Chunk("Departamento:", negritoFont));
            departamento.add(new Chunk(demanda.getSolicitanteDemanda().getDepartamentoUsuario().getNomeDepartamento(), normalFont));
            document.add(departamento);


            document.add(conteudoTitulo);

            document.add(objetivo);
            document.add(conteudoObjetivo);

            document.add(situacaoAtual);
            document.add(conteudoSituacaoAtual);

            String tipoValor = resgatarTipoValorBeneficio(demanda.getBeneficioRealDemanda());
            beneficioReal.add(new Chunk("Benefício Real: ", negritoFont));
            beneficioReal.add(new Chunk(tipoValor+" "+demanda.getBeneficioRealDemanda().getValorBeneficio().toString(), normalFont));
            document.add(beneficioReal);

            document.add(mbeneficioReal);
            document.add(conteudoMBeneficioReal);

            tipoValor = resgatarTipoValorBeneficio(demanda.getBeneficioPotencialDemanda());
            beneficioPotencial.add(new Chunk("Benefício Potencial: ", negritoFont));
            beneficioPotencial.add(new Chunk(tipoValor+" "+demanda.getBeneficioPotencialDemanda().getValorBeneficio().toString(), normalFont));
            document.add(beneficioPotencial);

            document.add(mbeneficioPotencial);
            document.add(conteudoMBeneficioPotencial);

            document.add(beneficioQualitativo);
            document.add(conteudoBeneficioQualitativo);

            document.add(escopoProjeto);
            document.add(conteudoEscopoProjeto);

            document.add(anexos);
            document.add(conteudoAnexos);

            document.close();
            return baos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String resgatarTipoValorBeneficio(Beneficio beneficio) {
        if(beneficio.getMoedaBeneficio().getMoeda() == "Dollar"){
            return "$";
        }else if(beneficio.getMoedaBeneficio().getMoeda() == "Real"){
            return "R$";
        }else if(beneficio.getMoedaBeneficio().getMoeda() == "Euro"){
            return "€";
        }else{
            return "£";
        }
    }


}
