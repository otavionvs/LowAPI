package weg.com.Low.util;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.springframework.stereotype.Component;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.TipoAtaProposta;

import javax.print.Doc;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class GeradorPDF {

    private final Font negritoFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private final Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

    public ByteArrayOutputStream gerarPDFAta(Reuniao reuniao, TipoAtaProposta tipoAtaProposta){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            document.setMargins(document.leftMargin(), document.rightMargin(), document.topMargin() + 50, document.bottomMargin());
            PdfWriter writer = PdfWriter.getInstance(document, baos);


            HeaderFooter header = new HeaderFooter();
            writer.setPageEvent(header);
            document.open();

            Paragraph conteudoTitulo = new Paragraph("ATA "+ tipoAtaProposta.toString() +" REUNIÃO DA" + reuniao.getComissaoReuniao().getComissao().toUpperCase(), negritoFont);
            conteudoTitulo.setAlignment(Element.ALIGN_CENTER);
            document.add(conteudoTitulo);

            for (Proposta demanda :
                    reuniao.getPropostasReuniao()) {
                if(demanda.getTipoAtaProposta() == tipoAtaProposta){
                    Paragraph p = new Paragraph();
                    p.setSpacingAfter(30);
                    document.add(p);
                    setInformationsDocumentDemanda(document, demanda, writer);
                }
            }


            document.close();

            return baos;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void addTitulo(Document document, String textoTitulo) throws DocumentException {
        Paragraph conteudoTitulo = new Paragraph(textoTitulo, negritoFont);
        conteudoTitulo.setAlignment(Element.ALIGN_CENTER);
        document.add(conteudoTitulo);
    }
    private void addSolicitanteEData(Document document, String solicitante, String dataCriacao) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        PdfPCell leftCell = new PdfPCell();
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk("Solicitante:", negritoFont));
        paragraph.add(new Chunk(solicitante, normalFont));
        leftCell.addElement(paragraph);
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(leftCell);
        PdfPCell rightCell = new PdfPCell();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add(new Chunk("Data:", negritoFont));
        paragraph2.add(new Chunk(dataCriacao, normalFont));
        paragraph2.setAlignment(Element.ALIGN_RIGHT);
        rightCell.addElement(paragraph2);
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(rightCell);
        document.add(table);
    }
    private void addEspaco(Document document) throws DocumentException {
        Paragraph p = new Paragraph();
        p.setSpacingAfter(30);
        document.add(p);
    }
    private void addDepartamento(Document document, String nomeDep) throws DocumentException {
        document.add(new Chunk("Departamento:", negritoFont));
        document.add(new Chunk(nomeDep, normalFont));
    }
    private void addObjetivo(Document document, String conteudoObjetivo, PdfWriter writer) throws DocumentException, IOException {
        document.add(new Paragraph("Objetivo:", negritoFont));
        try{
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(conteudoObjetivo.getBytes()));
        }catch (IOException e){
            document.add(new Paragraph(conteudoObjetivo, normalFont));
        }
    }

    private void addSituacaoAtual(Document document, String conteudoSituacaoAtual, PdfWriter writer) throws DocumentException, IOException {
        Paragraph situacaoAtual = new Paragraph("Situação Atual:", negritoFont);
        document.add(situacaoAtual);
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(conteudoSituacaoAtual.getBytes()));
    }
    private void addBeneficios(Document document, Demanda demanda) throws DocumentException {
        Paragraph beneficioReal = new Paragraph();
        Paragraph mbeneficioReal = new Paragraph("Memória de Cálculo do Benefício Real:", negritoFont);
        Paragraph beneficioPotencial = new Paragraph();
        Paragraph mbeneficioPotencial = new Paragraph("Memória de Cálculo do Benefício Potencial:", negritoFont);
        Paragraph beneficioQualitativo = new Paragraph("Beneficio Qualitativo:", negritoFont);
        try{
            String tipoValor = resgatarTipoValorBeneficio(demanda.getBeneficioRealDemanda());
            beneficioReal.add(new Chunk("Benefício Real: ", negritoFont));
            beneficioReal.add(new Chunk(tipoValor + " " + demanda.getBeneficioRealDemanda().getValorBeneficio().toString(), normalFont));

            tipoValor = resgatarTipoValorBeneficio(demanda.getBeneficioPotencialDemanda());
            beneficioPotencial.add(new Chunk("Benefício Potencial: ", negritoFont));
            beneficioPotencial.add(new Chunk(tipoValor + " " + demanda.getBeneficioPotencialDemanda().getValorBeneficio().toString(), normalFont));

            document.add(beneficioReal);
            document.add(mbeneficioReal);
            document.add(new Paragraph(demanda.getBeneficioRealDemanda().getMemoriaDeCalculoBeneficio(), normalFont));

            document.add(beneficioPotencial);
            document.add(mbeneficioPotencial);
            document.add(new Paragraph(demanda.getBeneficioPotencialDemanda().getMemoriaDeCalculoBeneficio(), normalFont));

            document.add(beneficioQualitativo);
            document.add(new Paragraph(demanda.getBeneficioQualitativoDemanda(), normalFont));

        }catch (NullPointerException e){
            e.printStackTrace();
        }


    }
    private void addTamanhoDemanda(Document document, String tamanho) throws DocumentException {
        Paragraph tamanhoDemanda = new Paragraph();
        tamanhoDemanda.add(new Chunk("Tamanho Demanda: ", negritoFont));
        tamanhoDemanda.add(new Chunk(tamanho, normalFont));
        document.add(tamanhoDemanda);
    }
    private void addRecursos(Document document, Demanda demanda) throws DocumentException {
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
    }
    private void addPrazos(Document document, Demanda demanda) throws DocumentException {
        Paragraph prazoElaboracao = new Paragraph();
        prazoElaboracao.add(new Chunk("Prazo Elaboração Proposta: ", negritoFont));
        prazoElaboracao.add(new Chunk(((Proposta) demanda).getPrazoProposta().toString(), normalFont));


        Paragraph inicioExDemandaProposta = new Paragraph();
        inicioExDemandaProposta.add(new Chunk("Inicio Execução Proposta: ", negritoFont));
        inicioExDemandaProposta.add(new Chunk(((Proposta) demanda).getInicioExDemandaProposta().toString(), normalFont));


        Paragraph fimExDemandaProposta = new Paragraph();
        fimExDemandaProposta.add(new Chunk("Fim Execução Proposta: ", negritoFont));
        fimExDemandaProposta.add(new Chunk(((Proposta) demanda).getFimExDemandaProposta().toString(), normalFont));


        document.add(prazoElaboracao);
        document.add(inicioExDemandaProposta);
        document.add(fimExDemandaProposta);
    }
    private void addPayback(Document document, String payback) throws DocumentException {

        Paragraph paybackProposta = new Paragraph();
        paybackProposta.add(new Chunk("Payback: ", negritoFont));
        paybackProposta.add(new Chunk(payback, normalFont));
        document.add(paybackProposta);
    }
    private void addEscopo(Document document, String escopo) throws DocumentException {

        Paragraph escopoDemandaProposta = new Paragraph("Escopo Proposta: ", negritoFont);
        Paragraph conteudoEscopoDemandaProposta = new Paragraph(escopo, normalFont);
        document.add(escopoDemandaProposta);
        document.add(conteudoEscopoDemandaProposta);
    }

    private void addParecerEDecisao(Document document, String parecer, String ultimaDecisao) throws DocumentException {
        Paragraph parecerComissao = new Paragraph();
        parecerComissao.add(new Chunk("Parecer da Comissão: ", negritoFont));
        parecerComissao.add(new Chunk(parecer, normalFont));
        Paragraph decisao = new Paragraph();
        decisao.add(new Chunk("Decisão: ", negritoFont));
        decisao.add(new Chunk(ultimaDecisao, normalFont));

        document.add(parecerComissao);
        document.add(decisao);
    }

    private void addFrequenciaUso(Document document, String frequencia) throws DocumentException {
        Paragraph frequenciaUso = new Paragraph();
        frequenciaUso.add(new Chunk("Frequência de Uso do Sistema: ", negritoFont));
        frequenciaUso.add(new Chunk(frequencia, normalFont));
        document.add(frequenciaUso);
    }

    private Document setInformationsDocumentDemanda(Document document, Demanda demanda, PdfWriter writer){
        try {

            addTitulo(document, demanda.getCodigoDemanda().toString()+". "+ demanda.getTituloDemanda().toUpperCase());
            addSolicitanteEData(document, demanda.getSolicitanteDemanda().getNomeUsuario(), demanda.getDataCriacaoDemanda().toString());
            addEspaco(document);
            addDepartamento(document, demanda.getSolicitanteDemanda().getDepartamentoUsuario().getNomeDepartamento());
            addObjetivo(document, demanda.getObjetivoDemanda(), writer);
            addSituacaoAtual(document, demanda.getSituacaoAtualDemanda(), writer);
            addBeneficios(document, demanda);
            addFrequenciaUso(document, demanda.getFrequenciaDeUsoDemanda());
            // Caso a demanda tiver dados da DemandaClassificada adicionar o tamanho da demanda ao documento
            if (demanda.getStatusDemanda().ordinal() > 0 && (demanda instanceof DemandaClassificada || demanda instanceof Proposta)) {
                addTamanhoDemanda(document, ((DemandaClassificada) demanda).getTamanhoDemandaClassificada().toString());
            }
            // Caso a demanda tiver dados da Proposta, está sendo adicionado os dados relativos
            if (demanda.getStatusDemanda().ordinal() > 1 && demanda instanceof Proposta) {
                addPrazos(document, demanda);
                addPayback(document, ((Proposta) demanda).getPaybackProposta().toString());
                addEscopo(document, ((Proposta) demanda).getEscopoDemandaProposta());
                //Responsável
                addResponsaveis(document, ((Proposta) demanda).getResponsavelProposta());
                addRecursos(document, demanda);
                try {
                    addParecerEDecisao(document, ((Proposta) demanda).getParecerComissaoProposta(), ((Proposta) demanda).getUltimaDecisaoComissao());

                }catch (Exception e ){

                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return document;
    }

    private void addResponsaveis(Document document, String responsavel) throws DocumentException {
        Paragraph responsavelProposta = new Paragraph();
        responsavelProposta.add(new Chunk("Responsável Proposta: ", negritoFont));
        responsavelProposta.add(new Chunk(responsavel, normalFont));
        document.add(responsavelProposta);
    }

    public ByteArrayOutputStream gerarPDFDemanda(Demanda demanda) {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            document.setMargins(document.leftMargin(), document.rightMargin(), document.topMargin() + 50, document.bottomMargin());
            PdfWriter writer = PdfWriter.getInstance(document, baos);

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
