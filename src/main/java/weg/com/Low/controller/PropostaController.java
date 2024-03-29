package weg.com.Low.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import weg.com.Low.dto.InfoDgDTO;
import weg.com.Low.dto.PropostaDTO;
import weg.com.Low.dto.RecursoDTO;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.DecisaoProposta;
import weg.com.Low.model.enums.Status;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.model.service.*;
import weg.com.Low.security.TokenUtils;
import weg.com.Low.util.PropostaUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/proposta")
public class PropostaController {
    private PropostaService propostaService;
    private ArquivoService arquivoService;
    private CentroCustoService centroCustoService;
    private DemandaService demandaService;
    private UsuarioService usuarioService;
//    private BeneficioService beneficioService;

    //Gets são feitos em DemandaController

    //ver como fica com status de aprovação
    //verificar se centro de custo existe?
    @PostMapping
    public ResponseEntity<Object> save(
            @RequestParam("arquivos") MultipartFile[] arquivos,
            @RequestParam("proposta") String propostaJson,
            HttpServletRequest request) {
        PropostaUtil propostaUtil = new PropostaUtil();
        Proposta proposta = propostaUtil.convertJsonToModel(propostaJson);

        if (!arquivos[0].getOriginalFilename().equals("")) {
            proposta.setArquivos(arquivos);
        }

        if (!demandaService.existsById(proposta.getCodigoDemanda())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda não Encontrada!");
        }
        Demanda demanda = demandaService.findLastDemandaById(proposta.getCodigoDemanda()).get();

        //Seta as informações de demandaClassificada
        proposta.setAll((DemandaClassificada) demandaService.findLastDemandaById(proposta.getCodigoDemanda()).get());

        for (Recurso recurso : proposta.getRecursosProposta()) {
            if (!centroCustoService.verificaPorcentagemCentroCusto(recurso.getCentroCustoRecurso())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Porcentagem centro de custo incompleta em " + recurso.getNomeRecurso());
            }
            recurso.setCentroCustoRecurso(centroCustoService.saveAll(recurso.getCentroCustoRecurso()));
        }

        if (proposta.getBeneficioPotencialDemanda().getValorBeneficio() == null &&
                proposta.getBeneficioPotencialDemanda().getMemoriaDeCalculoBeneficio() == null) {
            proposta.setBeneficioPotencialDemanda(null);
        } else if (proposta.getBeneficioPotencialDemanda().getValorBeneficio() == null ||
                proposta.getBeneficioPotencialDemanda().getMemoriaDeCalculoBeneficio() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("É necessário preencher todos os campos do benefício Potencial");
        }

        if (proposta.getBeneficioRealDemanda().getValorBeneficio() == null &&
                proposta.getBeneficioRealDemanda().getMemoriaDeCalculoBeneficio() == null) {
            proposta.setBeneficioRealDemanda(null);
        } else if (proposta.getBeneficioRealDemanda().getValorBeneficio() == null ||
                proposta.getBeneficioRealDemanda().getMemoriaDeCalculoBeneficio() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("É necessário preencher todos os campos do benefício Real");
        }

        centroCustoService.saveAll(proposta.getCentroCustosDemanda());

        proposta.setVersion(demanda.getVersion() + 1);

        //Adiciona quem fez a modificação nessa demanda
        proposta.setAutor(usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get().getNomeUsuario());

        return ResponseEntity.status(HttpStatus.OK).body(propostaService.save(proposta, TipoNotificacao.AVANCOU_STATUS_DEMANDA));
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateProposta(
            @RequestParam("arquivos") MultipartFile[] arquivos,
            @RequestParam("proposta") String propostaJson,
            HttpServletRequest request) {
        PropostaUtil propostaUtil = new PropostaUtil();
        Proposta propostaNova = propostaUtil.convertJsonToModel(propostaJson);
        if (!arquivos[0].getOriginalFilename().equals("")) {
            propostaNova.setArquivos(arquivos);
        }

        if (!demandaService.existsById(propostaNova.getCodigoDemanda())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta demanda não existe!");
        }

        centroCustoService.saveAll(propostaNova.getCentroCustosDemanda());

        for (Recurso recurso : propostaNova.getRecursosProposta()) {
            if (!centroCustoService.verificaPorcentagemCentroCusto(recurso.getCentroCustoRecurso())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Porcentagem centro de custo incompleta em " + recurso.getNomeRecurso());
            }
            recurso.setCentroCustoRecurso(centroCustoService.saveAll(recurso.getCentroCustoRecurso()));
        }

        if (propostaNova.getBeneficioPotencialDemanda().getValorBeneficio() == null &&
                propostaNova.getBeneficioPotencialDemanda().getMemoriaDeCalculoBeneficio().equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("É necessário preencher todos os campos do benefício Potencial");
        }

        if (propostaNova.getBeneficioRealDemanda().getValorBeneficio() == null &&
                propostaNova.getBeneficioRealDemanda().getMemoriaDeCalculoBeneficio().equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("É necessário preencher todos os campos do benefício Real");
        }

        Proposta proposta = (Proposta) demandaService.findLastDemandaById(propostaNova.getCodigoDemanda()).get();

        propostaNova.setAll(proposta);
        propostaNova.setStatusDemanda(proposta.getStatusDemanda());
        propostaNova.setVersion(propostaNova.getVersion() + 1);

        //Adiciona quem fez a modificação nessa demanda
        propostaNova.setAutor(usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get().getNomeUsuario());

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(propostaNova, TipoNotificacao.EDITOU_DEMANDA));
    }


    @PutMapping("/dg/{codigoDemanda}")
    public ResponseEntity<Object> infoDGProposta(
            @PathVariable(value = "codigoDemanda") Integer codigoDemanda,
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("infoDg") String infoDgString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InfoDgDTO infoDgDTO = objectMapper.readValue(infoDgString, InfoDgDTO.class);
            Proposta demanda = (Proposta) demandaService.findLastDemandaById(codigoDemanda).get();
            BeanUtils.copyProperties(infoDgDTO, demanda);
            demanda.setArquivoDG(arquivoService.save(new Arquivo(null,
                    arquivo.getOriginalFilename(),
                    arquivo.getContentType(),
                    arquivo.getBytes())));

            if (demanda.getDecisaoDG().equals(DecisaoProposta.APROVAR.toString())) {
                demanda.setStatusDemanda(Status.TO_DO);

                if (demanda.getParecerComissaoProposta().length() == 0) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Envie alguma Recomendação");
                }



            } else if (demanda.getDecisaoDG().equals(DecisaoProposta.APROVAR_COM_RECOMENDACAO.toString())) {

                demanda.setStatusDemanda(Status.TO_DO);

                if (demanda.getParecerComissaoProposta().length() == 0) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Envie alguma Recomendação");
                }

            } else if (demanda.getDecisaoDG().equals(DecisaoProposta.REAPRESENTAR_COM_RECOMENDACAO.toString())) {

                demanda.setStatusDemanda(Status.BACKLOG_PROPOSTA);

                if (demanda.getParecerComissaoProposta().length() == 0) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Envie alguma Recomendação");
                }

            } else if (demanda.getDecisaoDG().equals(DecisaoProposta.REPROVAR.toString())) {
                demanda.setStatusDemanda(Status.CANCELLED);
            }

            return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demanda, TipoNotificacao.EDITOU_DEMANDA));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }


//    @PostMapping
//    public ResponseEntity<Object> save(@RequestBody @Valid PropostaDTO propostaDTO) {
//        List<RecursoDTO> recursosDTO = propostaDTO.getRecursosProposta();
//        List<Recurso> recursos = new ArrayList<>();
//
//        Proposta proposta = new Proposta();
//        BeanUtils.copyProperties(propostaDTO, proposta);
//
//        for (int i = 0; i < recursosDTO.size(); i++) {
//            Recurso recurso = new Recurso();
//            RecursoDTO recursoDTO = recursosDTO.get(i);
//            BeanUtils.copyProperties(recursoDTO, recurso);
////            centroCustoService.saveAll(recurso.getCentroCustos());
//            recurso = recursoService.save(recurso);
//            recursos.add(recurso);
//        }
//
//        if(proposta.getBeneficioPotencialDemanda().getCodigoBeneficio() == null){
//            proposta.setBeneficioPotencialDemanda(beneficioService.save(proposta.getBeneficioPotencialDemanda()));
//        }
//        if(proposta.getBeneficioRealDemanda().getCodigoBeneficio() == null){
//            proposta.setBeneficioRealDemanda(beneficioService.save(proposta.getBeneficioRealDemanda()));
//        }
//
//        proposta.setRecursosProposta(recursos);
//        proposta.setStatusDemanda(Status.ASSESSMENT);
//        proposta.setVersion(proposta.getVersion() + 1);
//
//        return ResponseEntity.status(HttpStatus.OK).body(propostaService.save(proposta));
//    }


//    @DeleteMapping("/{codigo}")
//    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
//        if (!propostaService.existsById(codigo)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proposta não encontrada!");
//        }
//        propostaService.deleteById(codigo);
//        return ResponseEntity.status(HttpStatus.OK).body("Proposta Deletada!");
//    }
}
