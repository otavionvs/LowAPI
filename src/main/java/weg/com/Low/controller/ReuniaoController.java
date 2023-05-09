package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.*;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Proposta;
import weg.com.Low.model.entity.Reuniao;
import weg.com.Low.model.enums.*;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.PropostaService;
import weg.com.Low.model.service.ReuniaoService;
import weg.com.Low.util.GeradorPDF;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
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
    public ResponseEntity<List<Reuniao>> search(
            @RequestParam("nomeComissao") String nomeComissao,
            @RequestParam("dataReuniao") String dataReuniao,
            @RequestParam("statusReuniao") String statusReuniao,
            @RequestParam("ppmProposta") String ppmProposta,
            @RequestParam("analista") String analista,
            @RequestParam("solicitante") String solicitante,
            @PageableDefault(
                    page = 0,
                    size = 10) Pageable page){
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.search(nomeComissao, dataReuniao, statusReuniao,
                ppmProposta, analista, solicitante, page));
    }

    @GetMapping("/ata/{codigoReuniao}")
    public ResponseEntity<Object> downloadAta(
            @PathVariable(value = "codigoReuniao") Integer codigoReuniao, @RequestBody TipoAtaProposta tipoAta) {
        Reuniao reuniao = reuniaoService.findById(codigoReuniao).get();
        if (reuniao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma reuniao encontrada!");
        }
        GeradorPDF geradorPDF = new GeradorPDF();
        ByteArrayOutputStream baos = geradorPDF.gerarPDFAta(reuniao, tipoAta);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "documento.pdf");
        headers.setContentLength(baos.size());

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ReuniaoDTO reuniaoDTO) {
        Reuniao reuniao = new Reuniao();
        //Verificação para caso a Reunião não tenha Propostas
        if(reuniaoDTO.getPropostasReuniao().size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Adicione Propostas na sua Reunião");
        }
        //Verificar se existe a comissão enviada e setar no objeto principal
        boolean comissaoNotFound = true;
        for(Comissao comissao: Comissao.values()){
            if(comissao.toString().equals(reuniaoDTO.getComissaoReuniao())) {
                comissaoNotFound = false;
                reuniao.setComissaoReuniao(comissao);
                break;
            }
        }

        if(comissaoNotFound){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comissão não Encontrada! Comissões: [CPGCI, CGPN, CPVM, CWBS, CTI, DTI, CPGPR]");
        }
        //Instancia Propostas com o código
        ArrayList<Proposta> listaPropostas = new ArrayList<>();
        for (int i = 0; i < reuniaoDTO.getPropostasReuniao().size(); i++) {
            Proposta proposta = (Proposta) demandaService.findLastDemandaById(reuniaoDTO.getPropostasReuniao().get(i).getCodigoDemanda()).get();

            Proposta propostaNova = new Proposta();
            BeanUtils.copyProperties(proposta, propostaNova);


            propostaNova.setStatusDemanda(Status.DISCUSSION);
            propostaNova.setVersion(proposta.getVersion() + 1);
            //É criado uma nova proposta para atulizar a versão corretamente.
            //Necessário para a realização de um PUT
            listaPropostas.add(propostaService.save(propostaNova));
        }

        BeanUtils.copyProperties(reuniaoDTO, reuniao);
        reuniao.setPropostasReuniao(listaPropostas);
        Long tempo = reuniao.getDataReuniao().getTime() - new Date().getTime();
        //aproximadamente duas semanas
        if(tempo > 0 && tempo < 1300000000){
            reuniao.setStatusReuniao(StatusReuniao.PROXIMO);
        }else{
            reuniao.setStatusReuniao(StatusReuniao.AGUARDANDO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao));
    }

    @PutMapping("/parecer/{codigoProposta}")
    public ResponseEntity<Object> parecer(
            @PathVariable(value = "codigoProposta") Integer codigoProposta,
            @RequestBody @Valid ParecerComissaoDTO parecerComissaoDTO) {


        Proposta demanda = (Proposta) demandaService.findLastDemandaById(codigoProposta).get();

        Proposta novaDemanda = new Proposta();
        BeanUtils.copyProperties(demanda, novaDemanda);

        if(parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.APROVAR)){
            novaDemanda.setStatusDemanda(Status.TO_DO);

        }else if(parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.APROVAR_COM_RECOMENDACAO)){
            novaDemanda.setStatusDemanda(Status.TO_DO);

            if(parecerComissaoDTO.getParecerComissaoProposta().length() == 0){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Envie alguma Recomendação");
            }

        }else if(parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.REAPRESENTAR_COM_RECOMENDACAO)){

            novaDemanda.setStatusDemanda(Status.BACKLOG_PROPOSTA);

            if(parecerComissaoDTO.getParecerComissaoProposta().length() == 0){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Envie alguma Recomendação");
            }

        }else if(parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.REPROVAR)){
            novaDemanda.setStatusDemanda(Status.CANCELLED);
        }

        novaDemanda.setUltimaDecisaoComissao(parecerComissaoDTO.getDecisaoProposta().toString());
        novaDemanda.setVersion(demanda.getVersion() + 1);
        BeanUtils.copyProperties(parecerComissaoDTO, novaDemanda);


        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(novaDemanda));
    }


    @PutMapping("/finalizar/{codigoReuniao}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "codigoReuniao") Integer codigoReuniao) {
        if (!reuniaoService.existsById(codigoReuniao)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta reunião não existe!");
        }
        Reuniao reuniao = reuniaoService.findById(codigoReuniao).get();
        reuniao.setStatusReuniao(StatusReuniao.CONCLUIDO);
        List<Proposta> listaPropostas = new ArrayList<>();
        for (Proposta proposta : reuniao.getPropostasReuniao()) {
            //Aqui deve retornar ao status anterior.
            if (proposta.getStatusDemanda() == Status.DISCUSSION) {
                Demanda propostaAnterior = demandaService.findFirstByCodigoDemandaAndVersion(proposta.getCodigoDemanda(), proposta.getVersion() - 1).get();
                Proposta propostaNova = new Proposta();
                BeanUtils.copyProperties(propostaAnterior, propostaNova);
                propostaNova.setVersion(propostaAnterior.getVersion() + 2);
                proposta = propostaService.save(propostaNova);
            }
            listaPropostas.add(proposta);
        }
        reuniao.setPropostasReuniao(listaPropostas);

        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao));
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

        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao));
    }

    @PutMapping("/update/{codigo}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "codigo") Integer codigo,
            @RequestBody @Valid ReuniaoDTO reuniaoDTO) {
        if (!reuniaoService.existsById(codigo)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta reunião não existe!");
        }

        Reuniao reuniao = reuniaoService.findById(codigo).get();
        BeanUtils.copyProperties(reuniaoDTO, reuniao);
        if (!reuniaoDTO.getStatusReuniao().equals(StatusReuniao.CANCELADO) &&
        !reuniaoDTO.getStatusReuniao().equals(StatusReuniao.CONCLUIDO)) {
            Long tempo = reuniao.getDataReuniao().getTime() - new Date().getTime();
            if (tempo > 0 && tempo < 1300000000) {
                reuniao.setStatusReuniao(StatusReuniao.PROXIMO);
            } else if (tempo < 0) {
                reuniao.setStatusReuniao(StatusReuniao.PENDENTE);
            } else {
                reuniao.setStatusReuniao(StatusReuniao.AGUARDANDO);
            }
        }else{
            reuniao.setStatusReuniao(reuniaoDTO.getStatusReuniao());
        }
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao));
    }
}
