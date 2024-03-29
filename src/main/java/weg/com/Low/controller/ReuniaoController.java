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
import weg.com.Low.model.service.*;
import weg.com.Low.security.TokenUtils;
import weg.com.Low.util.GeradorPDF;

import javax.servlet.http.HttpServletRequest;
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
    private UsuarioService usuarioService;

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
    public ResponseEntity<Object> save(
            @RequestBody @Valid ReuniaoDTO reuniaoDTO,
            HttpServletRequest request) {
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
            //Adiciona quem fez a modificação nessa demanda
            propostaNova.setAutor(usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get().getNomeUsuario());

            //N retirar o sout
            System.out.println(propostaNova.getScore());
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
            @RequestBody @Valid ParecerComissaoDTO parecerComissaoDTO,
            HttpServletRequest request) {


        Proposta demanda = (Proposta) demandaService.findLastDemandaById(codigoProposta).get();
        Reuniao reuniao = reuniaoService.findById(codigoReuniao).get();

        ModelMapper modelMapper = new ModelMapper();
        Proposta novaDemanda = modelMapper.map(demanda, Proposta.class);

      if (parecerComissaoDTO.getDecisaoProposta().equals(DecisaoProposta.REAPRESENTAR_COM_RECOMENDACAO)) {

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

        //Adiciona quem fez a modificação nessa demanda
        novaDemanda.setAutor(usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get().getNomeUsuario());

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
        reuniaoService.save(reuniao, TipoNotificacao.SEM_NOTIFICACAO);

        return ResponseEntity.status(HttpStatus.OK).body(demandaComParecer);
    }


    @PutMapping("/finalizar/{codigoReuniao}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "codigoReuniao") Integer codigoReuniao,
            HttpServletRequest request) {
        if (!reuniaoService.existsById(codigoReuniao)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta reunião não existe!");
        }
        ModelMapper modelMapper = new ModelMapper();
        Reuniao reuniao = reuniaoService.findById(codigoReuniao).get();
        reuniao.setStatusReuniao(StatusReuniao.CONCLUIDO);
        List<Proposta> listaPropostas = new ArrayList<>();
        for (Proposta proposta : reuniao.getPropostasReuniao()) {
            //Aqui deve retornar ao status anterior. (Caso não tiver parecer da comissão)
            if (proposta.getParecerComissaoProposta() == null) {
//                demandaService.deleteById(proposta.getCodigoDemanda());
//
//                proposta = (Proposta) demandaService.findLastDemandaById(proposta.getCodigoDemanda()).get();
                Demanda propostaAnterior = demandaService.findFirstByCodigoDemandaAndVersion(proposta.getCodigoDemanda(), proposta.getVersion() - 1).get();
                Proposta propostaNova = new Proposta();
                BeanUtils.copyProperties(propostaAnterior, propostaNova);
                propostaNova.setCentroCustosDemanda(propostaAnterior.getCentroCustosDemanda());
                propostaNova.setArquivosClassificada(propostaAnterior.getArquivosDemanda());
                propostaNova.setVersion(propostaAnterior.getVersion() + 2);

                //Adiciona quem fez a modificação nessa demanda
                propostaNova.setAutor(usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get().getNomeUsuario());

                demandaService.save(propostaNova, TipoNotificacao.SEM_NOTIFICACAO);
            }
            listaPropostas.add(proposta);
        }
        reuniao.setPropostasReuniao(listaPropostas);

        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao, TipoNotificacao.FINALIZOU_REUNIAO));
    }




    //Caso o usuário adicionar um parecer antes de cancelar uma reunião, a proposta deve voltar a sua versão anterior
    @PutMapping("/cancelar/{codigoReuniao}")
    public ResponseEntity<Object> cancell(
            @PathVariable(value = "codigoReuniao") Integer codigoReuniao,
            @RequestBody String motivoCancelamentoReuniao,
            HttpServletRequest request) {


        if (!reuniaoService.existsById(codigoReuniao)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta reunião não existe!");
        }
        Reuniao reuniao = reuniaoService.findById(codigoReuniao).get();
        reuniao.setStatusReuniao(StatusReuniao.CANCELADO);
        reuniao.setMotivoCancelamentoReuniao(motivoCancelamentoReuniao);

        List<Proposta> listaPropostas = new ArrayList<>();

        for(Proposta proposta : reuniao.getPropostasReuniao()) {
            proposta = (Proposta) demandaService.findFirstByCodigoDemandaAndVersion(proposta.getCodigoDemanda(), proposta.getVersion() - 1).get();
            proposta.setVersion(proposta.getVersion() + 2);

            Proposta propostaNova = new Proposta();
            BeanUtils.copyProperties(proposta, propostaNova);

            //Adiciona quem fez a modificação nessa demanda
            propostaNova.setAutor(usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get().getNomeUsuario());

            listaPropostas.add((Proposta) demandaService.save(propostaNova, TipoNotificacao.SEM_NOTIFICACAO));
        }
        reuniao.setPropostasReuniao(listaPropostas);

        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao, TipoNotificacao.DESMARCOU_REUNIAO));
    }

    @PutMapping("/update/{codigo}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "codigo") Integer codigo,
            @RequestBody @Valid ReuniaoDTO reuniaoDTO,
            HttpServletRequest request) {
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

                //Adiciona quem fez a modificação nessa demanda
                propostaNova.setAutor(usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get().getNomeUsuario());
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
                //Adiciona quem fez a modificação nessa demanda
                propostaNova.setAutor(usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get().getNomeUsuario());
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
