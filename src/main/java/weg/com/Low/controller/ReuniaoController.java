package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import weg.com.Low.dto.*;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.*;
import weg.com.Low.model.service.ArquivoService;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.PropostaService;
import weg.com.Low.model.service.ReuniaoService;
import weg.com.Low.util.GeradorPDF;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/reuniao")
public class ReuniaoController {
    private ReuniaoService reuniaoService;
    private DemandaService demandaService;
    private PropostaService propostaService;
    private ArquivoService arquivoService;

    @GetMapping
    public ResponseEntity<List<Reuniao>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Reuniao> reuniaoOptional = reuniaoService.findById(codigo);
        if (reuniaoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reunião não encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoOptional.get());
    }

    @GetMapping("/filtro")
    public ResponseEntity<Page<Reuniao>> search(
            @RequestParam("nomeComissao") String nomeComissao,
            @RequestParam("dataReuniao") String dataReuniao,
            @RequestParam("statusReuniao") String statusReuniao,
            @RequestParam("ppmProposta") String ppmProposta,
            @RequestParam("analista") String analista,
            @RequestParam("solicitante") String solicitante,
            @RequestParam("ordenar") String ordenar,
            @PageableDefault(
                    page = 0,
                    size = 10) Pageable page) {
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.search(nomeComissao, dataReuniao, statusReuniao,
                ppmProposta, analista, solicitante, ordenar, page));
    }

    @GetMapping("/ata/{codigoReuniao}")
    public ResponseEntity<Object> downloadAta(
            @PathVariable(value = "codigoReuniao") Integer codigoReuniao, @RequestParam TipoAtaProposta tipoAta) {
        Reuniao reuniao = reuniaoService.findById(codigoReuniao).get();
        if (reuniao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma reuniao encontrada!");
        }
        GeradorPDF geradorPDF = new GeradorPDF();
        ByteArrayOutputStream baos = geradorPDF.gerarPDFAta(reuniao, tipoAta);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", reuniao.getComissaoReuniao().getComissao() + " - " + reuniao.getDataReuniao() + ".pdf");
        headers.setContentLength(baos.size());

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ReuniaoDTO reuniaoDTO) {
        Reuniao reuniao = new Reuniao();

        ModelMapper modelMapper = new ModelMapper();
        //Verificação para caso a Reunião não tenha Propostas
        if (reuniaoDTO.getPropostasReuniao().size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Adicione Propostas na sua Reunião");
        }
        //Verificar se existe a comissão enviada e setar no objeto principal
        boolean comissaoNotFound = true;
        for (Comissao comissao : Comissao.values()) {
            if (comissao.toString().equals(reuniaoDTO.getComissaoReuniao())) {
                comissaoNotFound = false;
                reuniao.setComissaoReuniao(comissao);
                break;
            }
        }

        if (comissaoNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comissão não Encontrada! Comissões: [CPGCI, CGPN, CPVM, CWBS, CTI, DTI, CPGPR]");
        }
        //Instancia Propostas com o código
        ArrayList<Proposta> listaPropostas = new ArrayList<>();
        for (int i = 0; i < reuniaoDTO.getPropostasReuniao().size(); i++) {
            Proposta proposta = (Proposta) demandaService.findLastDemandaById(reuniaoDTO.getPropostasReuniao().get(i).getCodigoDemanda()).get();
            Proposta propostaNova = modelMapper.map(proposta, Proposta.class);
            propostaNova.setStatusDemanda(Status.DISCUSSION);
            propostaNova.setVersion(proposta.getVersion() + 1);
            //É criado uma nova proposta para atulizar a versão corretamente.
            //Necessário para a realização de um PUT
            listaPropostas.add(propostaService.save(propostaNova, TipoNotificacao.SEM_NOTIFICACAO));
        }
        reuniao = modelMapper.map(reuniaoDTO, Reuniao.class);
        reuniao.setPropostasReuniao(listaPropostas);
        Long tempo = reuniao.getDataReuniao().getTime() - new Date().getTime();
        //aproximadamente duas semanas
        if (tempo > 0 && tempo < 1300000000) {
            reuniao.setStatusReuniao(StatusReuniao.PROXIMO);
        } else {
            reuniao.setStatusReuniao(StatusReuniao.AGUARDANDO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao, TipoNotificacao.MARCOU_REUNIAO));
    }

    @PutMapping("/parecer/{codigoProposta}")
    public ResponseEntity<Object> parecer(
            @PathVariable(value = "codigoProposta") Integer codigoProposta,
            @RequestParam Integer codigoReuniao,
            @RequestBody @Valid ParecerComissaoDTO parecerComissaoDTO) {


        Proposta demanda = (Proposta) demandaService.findLastDemandaById(codigoProposta).get();
        Reuniao reuniao = reuniaoService.findById(codigoReuniao).get();

        ModelMapper modelMapper = new ModelMapper();
        Proposta novaDemanda = modelMapper.map(demanda, Proposta.class);

        if (parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.APROVAR)) {
            novaDemanda.setStatusDemanda(Status.TO_DO);

        } else if (parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.APROVAR_COM_RECOMENDACAO)) {
            novaDemanda.setStatusDemanda(Status.TO_DO);

            if (parecerComissaoDTO.getParecerComissaoProposta().length() == 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Envie alguma Recomendação");
            }

        } else if (parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.REAPRESENTAR_COM_RECOMENDACAO)) {

            novaDemanda.setStatusDemanda(Status.BACKLOG_PROPOSTA);

            if (parecerComissaoDTO.getParecerComissaoProposta().length() == 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Envie alguma Recomendação");
            }

        } else if (parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.REPROVAR)) {
            novaDemanda.setStatusDemanda(Status.CANCELLED);
        }

        novaDemanda.setUltimaDecisaoComissao(parecerComissaoDTO.getDecisaoProposta().toString());
        novaDemanda.setVersion(demanda.getVersion() + 1);
        BeanUtils.copyProperties(parecerComissaoDTO, novaDemanda);


        Demanda demandaComParecer = demandaService.save(novaDemanda, TipoNotificacao.SEM_NOTIFICACAO);

        //Atualizando a demanda na reunião
        List<Proposta> listaPropostaReuniao = new ArrayList<>();
        for (Demanda i : reuniao.getPropostasReuniao()) {
            if (i.getCodigoDemanda() == demandaComParecer.getCodigoDemanda()) {
                i = demandaComParecer;
            }
            listaPropostaReuniao.add((Proposta) i);
        }
        reuniao.setPropostasReuniao(listaPropostaReuniao);
        reuniaoService.save(reuniao, TipoNotificacao.MARCOU_REUNIAO);

        return ResponseEntity.status(HttpStatus.OK).body(demandaComParecer);
    }


    @PutMapping("/finalizar/{codigoReuniao}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "codigoReuniao") Integer codigoReuniao) {
        if (!reuniaoService.existsById(codigoReuniao)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta reunião não existe!");
        }
        ModelMapper modelMapper = new ModelMapper();
        Reuniao reuniao = reuniaoService.findById(codigoReuniao).get();
        reuniao.setStatusReuniao(StatusReuniao.CONCLUIDO);
        List<Proposta> listaPropostas = new ArrayList<>();
        for (Proposta proposta : reuniao.getPropostasReuniao()) {
            //Aqui deve retornar ao status anterior.
            if (proposta.getStatusDemanda() == Status.DISCUSSION) {
//                demandaService.deleteById(proposta.getCodigoDemanda());
//
//                proposta = (Proposta) demandaService.findLastDemandaById(proposta.getCodigoDemanda()).get();
                Demanda propostaAnterior = demandaService.findFirstByCodigoDemandaAndVersion(proposta.getCodigoDemanda(), proposta.getVersion() - 1).get();
                Proposta propostaNova = new Proposta();
                BeanUtils.copyProperties(propostaAnterior, propostaNova);
                propostaNova.setCentroCustosDemanda(propostaAnterior.getCentroCustosDemanda());
                propostaNova.setArquivosClassificada(propostaAnterior.getArquivosDemanda());
                propostaNova.setVersion(propostaAnterior.getVersion() + 2);
                demandaService.save(propostaNova, TipoNotificacao.SEM_NOTIFICACAO);
            }
            listaPropostas.add(proposta);
        }
        reuniao.setPropostasReuniao(listaPropostas);

        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao, TipoNotificacao.FINALIZOU_REUNIAO));
    }


    @PutMapping("/parecer-dg/{codigoReuniao}")
    public ResponseEntity<Object> addInfoDG(
            @PathVariable(value = "codigoReuniao") Integer codigoReuniao,
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("numAtaDG") String numAtaDG) throws IOException {

        if(!reuniaoService.existsById(codigoReuniao)){
            return ResponseEntity.status(404).body("Demanda não encontrada");
        }

        Reuniao reuniao = reuniaoService.findById(codigoReuniao).get();

        reuniao.setArquivoReuniao(arquivoService.save(new Arquivo(null,
                arquivo.getOriginalFilename(),
                arquivo.getContentType(),
                arquivo.getBytes())));
        reuniao.setNumAtaDG(numAtaDG);

        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao, TipoNotificacao.FINALIZOU_REUNIAO));
    }


    //Caso o usuário adicionar um parecer antes de cancelar uma reunião, a proposta deve voltar a sua versão anterior
    @PutMapping("/cancelar/{codigoReuniao}")
    public ResponseEntity<Object> cancell(
            @PathVariable(value = "codigoReuniao") Integer codigoReuniao, @RequestBody String motivoCancelamentoReuniao) {


        if (!reuniaoService.existsById(codigoReuniao)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta reunião não existe!");
        }
        Reuniao reuniao = reuniaoService.findById(codigoReuniao).get();
        reuniao.setStatusReuniao(StatusReuniao.CANCELADO);
        reuniao.setMotivoCancelamentoReuniao(motivoCancelamentoReuniao);

        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao, TipoNotificacao.DESMARCOU_REUNIAO));
    }

    @PutMapping("/update/{codigo}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "codigo") Integer codigo,
            @RequestBody @Valid ReuniaoDTO reuniaoDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Reuniao reuniao = new Reuniao();
        //Verificação para caso a Reunião não tenha Propostas
        if (reuniaoDTO.getPropostasReuniao().size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Adicione Propostas na sua Reunião");
        }
        //Verificar se existe a comissão enviada e setar no objeto principal
        boolean comissaoNotFound = true;
        for (Comissao comissao : Comissao.values()) {
            if (comissao.toString().equals(reuniaoDTO.getComissaoReuniao())) {
                comissaoNotFound = false;
                reuniao.setComissaoReuniao(comissao);
                break;
            }
        }

        if (comissaoNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comissão não Encontrada! Comissões: [CPGCI, CGPN, CPVM, CWBS, CTI, DTI, CPGPR]");
        }
        //Instancia Propostas com o código
        Reuniao reuniaoAntiga = reuniaoService.findById(codigo).get();
        ArrayList<Proposta> listaPropostas = new ArrayList<>();

        for (int i = 0; i < reuniaoDTO.getPropostasReuniao().size(); i++) {
            Proposta proposta = (Proposta) demandaService.findLastDemandaById(reuniaoDTO.getPropostasReuniao().get(i).getCodigoDemanda()).get();
            Proposta propostaNova = modelMapper.map(proposta, Proposta.class);
            if (!(proposta.getStatusDemanda() == Status.DISCUSSION)) {
                propostaNova.setStatusDemanda(Status.DISCUSSION);
                propostaNova.setVersion(proposta.getVersion() + 1);
            }
            //Verificação de caso a reunião foi editada, porém tirou uma demanda.
            //Esta demanda retirada deve voltar ao seu status anterior

            //É criado uma nova proposta para atulizar a versão corretamente.
            //Necessário para a realização de um PUT
            listaPropostas.add(propostaService.save(propostaNova, TipoNotificacao.SEM_NOTIFICACAO));
        }
        for (Proposta proposta: reuniaoAntiga.getPropostasReuniao()){
            boolean demandaEncontrada = false;
            for(Proposta newProposta: listaPropostas){
                System.out.println("Proposta: " +proposta.getCodigoDemanda());
                System.out.println("newProposta: " +newProposta.getCodigoDemanda());
                System.out.println("verifica: "+(proposta.getCodigoDemanda() == newProposta.getCodigoDemanda()));
                if(proposta.getCodigoDemanda() == newProposta.getCodigoDemanda()){
                    demandaEncontrada = true;
                }
            }
            if(demandaEncontrada == false){
                System.out.println("Demanda Não foi encontrada!");
                Demanda propostaAnterior = demandaService.findFirstByCodigoDemandaAndVersion(proposta.getCodigoDemanda(), proposta.getVersion() - 1).get();
                Proposta propostaNova = modelMapper.map(propostaAnterior, Proposta.class);
                propostaNova.setVersion(propostaAnterior.getVersion() + 2);
                System.out.println("Proposta retornada a seu antigo status");
                propostaService.save(propostaNova, TipoNotificacao.SEM_NOTIFICACAO);
            }
        }

        reuniao = modelMapper.map(reuniaoDTO, Reuniao.class);
        reuniao.setPropostasReuniao(listaPropostas);
        reuniao.setCodigoReuniao(codigo);
        Long tempo = reuniao.getDataReuniao().getTime() - new Date().getTime();
        //aproximadamente duas semanas
        if (tempo > 0 && tempo < 1300000000) {
            reuniao.setStatusReuniao(StatusReuniao.PROXIMO);
        } else {
            reuniao.setStatusReuniao(StatusReuniao.AGUARDANDO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao, TipoNotificacao.MARCOU_REUNIAO));
    }
}
