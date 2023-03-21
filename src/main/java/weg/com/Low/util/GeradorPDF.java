package weg.com.Low.util;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
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

            Font fontNegrito = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Paragraph solicitante = new Paragraph("Solicitante:", fontNegrito);
            Paragraph data = new Paragraph();
            Paragraph setorTi = new Paragraph("Departamento:", fontNegrito);
            Paragraph titulo = new Paragraph("Título:", fontNegrito);Paragraph objetivo = new Paragraph("Objetivo:", fontNegrito);
            Paragraph situacaoAtual = new Paragraph("Situação Atual:", fontNegrito);
            Paragraph beneficioReal = new Paragraph("Benefício Real:", fontNegrito);
            Paragraph mbeneficioReal = new Paragraph("Memória de Cálculo do Benefício Real:", fontNegrito);
            Paragraph beneficioPotencial = new Paragraph("Beneficio Potencial:", fontNegrito);
            Paragraph mbeneficioPotencial = new Paragraph("Memória de Cálculo do Benefício Potencial:", fontNegrito);
            Paragraph beneficioQualitativo = new Paragraph("Beneficio Qualitativo:", fontNegrito);
            Paragraph escopoProjeto = new Paragraph("Escopo do Projeto:", fontNegrito);
            Paragraph anexos = new Paragraph("Anexos:", fontNegrito);

// Adicionando o conteúdo do cabeçalho do documento
            Paragraph conteudoSolicitante = new Paragraph(demanda.getSolicitanteDemanda().getNomeUsuario());
            Paragraph conteudoData = new Paragraph(demanda.getDataCriacaoDemanda().toString());
            Paragraph conteudoSetorTi = new Paragraph(demanda.getSolicitanteDemanda().getDepartamentoUsuario().getNomeDepartamento());
            Paragraph conteudoTitulo = new Paragraph(demanda.getTituloDemanda());
            Paragraph conteudoObjetivo = new Paragraph(demanda.getObjetivoDemanda());
            Paragraph conteudoSituacaoAtual = new Paragraph(demanda.getSituacaoAtualDemanda());
            Paragraph conteudoBeneficioReal = new Paragraph(demanda.getBeneficioRealDemanda().getValorBeneficio().toString());
            Paragraph conteudoMBeneficioReal = new Paragraph(demanda.getBeneficioRealDemanda().getMemoriaDeCalculoBeneficio());
            Paragraph conteudoBeneficioPotencial = new Paragraph(demanda.getBeneficioPotencialDemanda().getValorBeneficio().toString());
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

            PdfPCell leftCell = new PdfPCell(new Phrase("Informação alinhada à esquerda"));
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(leftCell);

            PdfPCell rightCell = new PdfPCell(new Phrase("Informação alinhada à direita"));
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(rightCell);

            document.add(table);

            document.add(setorTi);
            document.add(conteudoSetorTi);

            document.add(titulo);
            document.add(conteudoTitulo);

            document.add(objetivo);
            document.add(conteudoObjetivo);

            document.add(situacaoAtual);
            document.add(conteudoSituacaoAtual);

            document.add(beneficioReal);
            document.add(conteudoBeneficioReal);

            document.add(mbeneficioReal);
            document.add(conteudoMBeneficioReal);

            document.add(beneficioPotencial);
            document.add(conteudoBeneficioPotencial);

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


}
