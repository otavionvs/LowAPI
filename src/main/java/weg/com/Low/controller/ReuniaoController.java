package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.ParecerComissaoDTO;
import weg.com.Low.dto.ReuniaoDTO;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Proposta;
import weg.com.Low.model.entity.Reuniao;
import weg.com.Low.model.enums.Comissao;
import weg.com.Low.model.enums.DecisaoProposta;
import weg.com.Low.model.enums.Status;
import weg.com.Low.model.enums.StatusReuniao;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.PropostaService;
import weg.com.Low.model.service.ReuniaoService;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/reuniao")
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
            listaPropostas.add(proposta);
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
        if(parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.APROVAR)){
            demanda.setStatusDemanda(Status.TO_DO);

        }else if(parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.APROVAR_COM_RECOMENDACAO)){
            demanda.setStatusDemanda(Status.TO_DO);

            if(parecerComissaoDTO.getParecerComissaoProposta().length() == 0){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Envie alguma Recomendação");
            }

        }else if(parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.REAPRESENTAR_COM_RECOMENDACAO)){
            demanda.setStatusDemanda(Status.RETURNED);

            if(parecerComissaoDTO.getParecerComissaoProposta().length() == 0){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Envie alguma Recomendação");
            }

        }else if(parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.REPROVAR)){
            demanda.setStatusDemanda(Status.CANCELLED);
        }

        BeanUtils.copyProperties(parecerComissaoDTO, demanda);
        demanda.setVersion(demanda.getVersion() + 1);
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demanda));
    }

    @PutMapping("/ata/{codigoReuniao}")
    public ResponseEntity<Object> downloadAta(
            @PathVariable(value = "codigoReuniao") Integer codigo,
            @RequestBody String ) {


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
