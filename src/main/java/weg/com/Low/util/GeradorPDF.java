package weg.com.Low.util;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.springframework.stereotype.Component;
import weg.com.Low.model.entity.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class GeradorPDF {

    private final Font negritoFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private final Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

    public ByteArrayOutputStream gerarPDFAta(Reuniao reuniao){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            document.setMargins(document.leftMargin(), document.rightMargin(), document.topMargin() + 50, document.bottomMargin());
            PdfWriter writer = PdfWriter.getInstance(document, baos);


            HeaderFooter header = new HeaderFooter();
            writer.setPageEvent(header);
            document.open();

            Paragraph conteudoTitulo = new Paragraph("ATA REUNIÃO " + reuniao.getComissaoReuniao().getComissao().toUpperCase(), negritoFont);
            conteudoTitulo.setAlignment(Element.ALIGN_CENTER);
            document.add(conteudoTitulo);

            for (Demanda demanda :
                    reuniao.getPropostasReuniao()) {
                setInformationsDocumentDemanda(document, demanda, writer);
                Paragraph p = new Paragraph();
                p.setSpacingAfter(30);
                document.add(p);
            }


            document.close();

            return baos;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    private Document setInformationsDocumentDemanda(Document document, Demanda demanda, PdfWriter writer){
        try {

            Paragraph departamento = new Paragraph();
            Paragraph objetivo = new Paragraph("Objetivo:", negritoFont);
            Paragraph situacaoAtual = new Paragraph("Situação Atual:", negritoFont);
            Paragraph beneficioReal = new Paragraph();
            Paragraph mbeneficioReal = new Paragraph("Memória de Cálculo do Benefício Real:", negritoFont);
            Paragraph beneficioPotencial = new Paragraph();
            Paragraph mbeneficioPotencial = new Paragraph("Memória de Cálculo do Benefício Potencial:", negritoFont);
            Paragraph beneficioQualitativo = new Paragraph("Beneficio Qualitativo:", negritoFont);

            Paragraph conteudoTitulo = new Paragraph(demanda.getCodigoDemanda().toString()+". "+ demanda.getTituloDemanda().toUpperCase(), negritoFont);
            conteudoTitulo.setAlignment(Element.ALIGN_CENTER);

            String conteudoObjetivo = demanda.getObjetivoDemanda();
            String conteudoSituacaoAtual = demanda.getSituacaoAtualDemanda();
            Paragraph conteudoMBeneficioReal = new Paragraph(demanda.getBeneficioRealDemanda().getMemoriaDeCalculoBeneficio(), normalFont);
            Paragraph conteudoMBeneficioPotencial = new Paragraph(demanda.getBeneficioPotencialDemanda().getValorBeneficio().toString(), normalFont);
            Paragraph conteudoBeneficioQualitativo = new Paragraph(demanda.getBeneficioQualitativoDemanda(), normalFont);


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
            document.add(conteudoTitulo);

            document.add(table);
            departamento.add(new Chunk("Departamento:", negritoFont));
            departamento.add(new Chunk(demanda.getSolicitanteDemanda().getDepartamentoUsuario().getNomeDepartamento(), normalFont));
            document.add(departamento);
            document.add(objetivo);
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(conteudoObjetivo.getBytes()));
            document.add(situacaoAtual);
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(conteudoSituacaoAtual.getBytes()));

            String tipoValor = resgatarTipoValorBeneficio(demanda.getBeneficioRealDemanda());
            beneficioReal.add(new Chunk("Benefício Real: ", negritoFont));
            beneficioReal.add(new Chunk(tipoValor + " " + demanda.getBeneficioRealDemanda().getValorBeneficio().toString(), normalFont));
            document.add(beneficioReal);



            document.add(mbeneficioReal);
            document.add(conteudoMBeneficioReal);

            tipoValor = resgatarTipoValorBeneficio(demanda.getBeneficioPotencialDemanda());
            beneficioPotencial.add(new Chunk("Benefício Potencial: ", negritoFont));
            beneficioPotencial.add(new Chunk(tipoValor + " " + demanda.getBeneficioPotencialDemanda().getValorBeneficio().toString(), normalFont));
            document.add(beneficioPotencial);

            document.add(mbeneficioPotencial);
            document.add(conteudoMBeneficioPotencial);

            document.add(beneficioQualitativo);
            document.add(conteudoBeneficioQualitativo);

            // Caso a demanda tiver dados da DemandaClassificada adicionar o tamanho da demanda ao documento
            if (demanda.getStatusDemanda().ordinal() > 0 && (demanda instanceof DemandaClassificada || demanda instanceof Proposta)) {
                Paragraph tamanhoDemanda = new Paragraph();
                tamanhoDemanda.add(new Chunk("Tamanho Demanda: ", negritoFont));
                tamanhoDemanda.add(new Chunk(((DemandaClassificada) demanda).getTamanhoDemandaClassificada().toString(), normalFont));
                document.add(tamanhoDemanda);
            }

            // Caso a demanda tiver dados da Proposta, está sendo adicionado os dados relativos
            if (demanda.getStatusDemanda().ordinal() > 1 && demanda instanceof Proposta) {
                Paragraph prazoElaboracao = new Paragraph();
                prazoElaboracao.add(new Chunk("Prazo Elaboração Proposta: ", negritoFont));
                prazoElaboracao.add(new Chunk(((Proposta) demanda).getPrazoProposta().toString(), normalFont));


                Paragraph inicioExDemandaProposta = new Paragraph();
                inicioExDemandaProposta.add(new Chunk("Inicio Execução Proposta: ", negritoFont));
                inicioExDemandaProposta.add(new Chunk(((Proposta) demanda).getInicioExDemandaProposta().toString(), normalFont));


                Paragraph fimExDemandaProposta = new Paragraph();
                fimExDemandaProposta.add(new Chunk("Fim Execução Proposta: ", negritoFont));
                fimExDemandaProposta.add(new Chunk(((Proposta) demanda).getFimExDemandaProposta().toString(), normalFont));

                Paragraph paybackProposta = new Paragraph();
                paybackProposta.add(new Chunk("Payback: ", negritoFont));
                paybackProposta.add(new Chunk(((Proposta) demanda).getPaybackProposta().toString(), normalFont));

                Paragraph escopoDemandaProposta = new Paragraph("Escopo Proposta: ", negritoFont);
                Paragraph conteudoEscopoDemandaProposta = new Paragraph(((Proposta) demanda).getEscopoDemandaProposta(), normalFont);

                Paragraph responsavelProposta = new Paragraph();
                responsavelProposta.add(new Chunk("Responsável Proposta: ", negritoFont));
                responsavelProposta.add(new Chunk(((Proposta) demanda).getResponsavelProposta().getNomeUsuario(), normalFont));

                Paragraph areaResponsavelProposta = new Paragraph();
                areaResponsavelProposta.add(new Chunk("Área Responsável Proposta: ", negritoFont));
                areaResponsavelProposta.add(new Chunk(((Proposta) demanda).getResponsavelProposta().getDepartamentoUsuario().getNomeDepartamento(), normalFont));

                Paragraph recursos = new Paragraph("Recursos: ", negritoFont);
                recursos.setSpacingAfter(10);
                document.add(recursos);

                PdfPTable tableRecurso = new PdfPTable(7);
                tableRecurso.setWidthPercentage(100);
                tableRecurso.addCell(new PdfPCell(new Phrase("Recurso Necessário", negritoFont)));
                tableRecurso.addCell(new PdfPCell(new Phrase("Tipo da despesa", negritoFont)));
                tableRecurso.addCell(new PdfPCell(new Phrase("Perfil da despesa", negritoFont)));
                tableRecurso.addCell(new PdfPCell(new Phrase("C.C. pagantes", negritoFont)));
                tableRecurso.addCell(new PdfPCell(new Phrase("Qtd de horas", negritoFont)));
                tableRecurso.addCell(new PdfPCell(new Phrase("Valor da hora", negritoFont)));
                tableRecurso.addCell(new PdfPCell(new Phrase("Período de execução (Mensal)", negritoFont)));


                for (Recurso recurso : ((Proposta) demanda).getRecursosProposta()) {
                    tableRecurso.addCell(new PdfPCell(new Phrase(recurso.getNomeRecurso(), normalFont)));
                    tableRecurso.addCell(new PdfPCell(new Phrase(recurso.getTipoDespesaRecurso().toString(), normalFont)));
                    tableRecurso.addCell(new PdfPCell(new Phrase(recurso.getPerfilDespesaRecurso().toString(), normalFont)));
                    PdfPCell cellCC = new PdfPCell();
                    //Adicionar Centro de custo: por algum motivo estava comentado
                    cellCC.addElement(new Phrase("Centro de ", normalFont));
                    tableRecurso.addCell(cellCC);
                    tableRecurso.addCell(new PdfPCell(new Phrase(recurso.getQuantidadeHorasRecurso().toString(), normalFont)));
                    tableRecurso.addCell(new PdfPCell(new Phrase(recurso.getValorHoraRecurso().toString(), normalFont)));
                    tableRecurso.addCell(new PdfPCell(new Phrase(recurso.getPeriodoExMesesRecurso().toString(), normalFont)));
                }
                document.add(tableRecurso);


                document.add(prazoElaboracao);
                document.add(inicioExDemandaProposta);
                document.add(fimExDemandaProposta);
                document.add(paybackProposta);
                document.add(escopoDemandaProposta);
                document.add(conteudoEscopoDemandaProposta);
                document.add(responsavelProposta);
                document.add(areaResponsavelProposta);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return document;
    }

    public ByteArrayOutputStream gerarPDFDemanda(Demanda demanda) {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            document.setMargins(document.leftMargin(), document.rightMargin(), document.topMargin() + 50, document.bottomMargin());
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            String html = "<h1>12131kjfshdaklsdhfklsadhfklashdflkjshdlfkahsdklfhasdklfjhasldkfjhskladfhklasjdhfklsdahfsda2</h1>";

            HeaderFooter header = new HeaderFooter();
            writer.setPageEvent(header);
            document.open();

            setInformationsDocumentDemanda(document, demanda, writer);

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
