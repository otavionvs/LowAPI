package weg.com.Low.util;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Departamento;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.enums.NivelAcesso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
@Component
public class GeradorPDF {
    public ByteArrayOutputStream gerarPDF(Demanda demanda) {
        try {

            demanda = new Demanda();
            Usuario usuario = new Usuario();
            usuario.setNomeUsuario("João da Silva");
            usuario.setDepartamentoUsuario(new Departamento(1, "TI"));
            demanda.setSolicitanteDemanda(usuario);
            demanda.setObjetivoDemanda("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on ");
            demanda.setSituacaoAtualDemanda("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on");
            demanda.setCodigoDemanda(1);
            demanda.setTituloDemanda("Teste de PDF");
            // Criação do documento

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

// Adicionando o cabeçalho do documento
            Font fontNegrito = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Paragraph codigo = new Paragraph("01", fontNegrito);
            Paragraph logo = new Paragraph(new Chunk(Image.getInstance("weg-logo.png"), 0, 0));
            Paragraph solicitante = new Paragraph("Solicitante:", fontNegrito);
            Paragraph data = new Paragraph("Data:", fontNegrito);
            Paragraph setorTi = new Paragraph("Setor de TI:", fontNegrito);
            Paragraph titulo = new Paragraph("Título:", fontNegrito);
            Paragraph codigoDemanda = new Paragraph("Código da Demanda:", fontNegrito);

// Adicionando o conteúdo do cabeçalho do documento
            Paragraph conteudoSolicitante = new Paragraph(demanda.getSolicitanteDemanda().getNomeUsuario());
            Paragraph conteudoData = new Paragraph(demanda.getDataCriacaoDemanda().toString());
            Paragraph conteudoSetorTi = new Paragraph(demanda.getSolicitanteDemanda().getDepartamentoUsuario().getNomeDepartamento());
            Paragraph conteudoTitulo = new Paragraph(demanda.getTituloDemanda());
            Paragraph conteudoCodigoDemanda = new Paragraph(demanda.getCodigoDemanda());

// Adicionando o cabeçalho ao documento
            document.add(codigo);
            document.add(logo);

            document.add(solicitante);
            document.add(conteudoSolicitante);
            document.add(data);
            document.add(conteudoData);
            document.add(setorTi);
            document.add(conteudoSetorTi);
            document.add(titulo);
            document.add(conteudoTitulo);
            document.add(codigoDemanda);
            document.add(conteudoCodigoDemanda);

// Adicionando os campos com maiores informações
            Paragraph objetivo = new Paragraph("Objetivo:", fontNegrito);
            Paragraph situacaoAtual = new Paragraph("Situação Atual:", fontNegrito);
            Paragraph escopoProjeto = new Paragraph("Escopo do Projeto:", fontNegrito);
            Paragraph anexos = new Paragraph("Anexos:", fontNegrito);

// Adicionando o conteúdo dos campos com maiores informações
            Paragraph conteudoObjetivo = new Paragraph(demanda.getObjetivoDemanda());
            Paragraph conteudoSituacaoAtual = new Paragraph(demanda.getSituacaoAtualDemanda());
            Paragraph conteudoEscopoProjeto = new Paragraph("Ações de curto prazo visando ganhos globais de desempenho/estabilidade no MAESTRO Identificar causa raiz das instabilidades dos servidores (memória, CPU etc.)\n" +
                    "Otimizar estrutura das tabelas, consultas de baixo desempenho e criação/remoção de índices\n" +
                    "Otimizar configurações dos servidores (virtualização, balanceador de carga, recursos alocados,\n" +
                    "etc.)\n" +
                    "\uF0B7 Otimizar configurações dos servidores de aplicação (versão, parâmetros JVM, pool de conexões)o");
            Paragraph conteudoAnexos = new Paragraph("Arquivo 1.pdf\nArquivo 2.docx");

// Adicionando os campos com maiores informações ao documento
            document.add(objetivo);
            document.add(conteudoObjetivo);
            document.add(situacaoAtual);
            document.add(conteudoSituacaoAtual);
            document.add(escopoProjeto);
            document.add(conteudoEscopoProjeto);
            document.add(anexos);
            document.add(conteudoAnexos);

// Fechando o documento
            document.close();
            return baos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
