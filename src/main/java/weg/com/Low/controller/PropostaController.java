package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import weg.com.Low.dto.PropostaDTO;
import weg.com.Low.dto.RecursoDTO;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.Status;
import weg.com.Low.model.service.*;
import weg.com.Low.util.PropostaUtil;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/proposta")
public class PropostaController {
    private PropostaService propostaService;
    private RecursoService recursoService;
    private CentroCustoService centroCustoService;
    private DemandaService demandaService;
    private BeneficioService beneficioService;
//    private DemandaAnalistaService demandaAnalistaService;

//    @GetMapping
//    public ResponseEntity<List<Proposta>> findAll() {
//        return ResponseEntity.status(HttpStatus.OK).body(propostaService.findAll());
//    }
//
//    @GetMapping("/{codigo}")
//    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
//        Optional<Proposta> propostaOptional = propostaService.findById(codigo);
//        if (propostaOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proposta não encontrado!");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(propostaOptional.get());
//    }

    //ver como fica com status de aprovação
    //verificar se centro de custo existe?
    @PostMapping
    public ResponseEntity<Object> save(
            @RequestParam("arquivos") MultipartFile[] arquivos,
            @RequestParam("proposta") String propostaJson) {
        PropostaUtil propostaUtil = new PropostaUtil();
        Proposta proposta = propostaUtil.convertJsonToModel(propostaJson);
        proposta.setArquivos(arquivos);
        //Seta as informações
        proposta.setAll((DemandaClassificada) demandaService.findLastDemandaById(proposta.getCodigoDemanda()).get());

//        List<Recurso> recursos = proposta.getRecursosProposta();
//        List<Recurso> recursos = new ArrayList<>();

//        Proposta proposta = new Proposta();
//        BeanUtils.copyProperties(propostaDTO, proposta);

//        for (int i = 0; i < proposta.getRecursosProposta().size(); i++) {
////            Recurso recurso = new Recurso();
////            RecursoDTO recursoDTO = recursosDTO.get(i);
////            BeanUtils.copyProperties(recursoDTO, recurso);
////            centroCustoService.saveAll(recurso.getCentroCustos());
//            recurso = recursoService.save(proposta.get);
//            recursos.add(recurso);
//        }

        for(Recurso recurso: proposta.getRecursosProposta()){
            recurso.setCentroCustoRecurso(centroCustoService.saveAll(recurso.getCentroCustoRecurso()));
        }

        if(proposta.getBeneficioPotencialDemanda().getCodigoBeneficio() == null){
            proposta.setBeneficioPotencialDemanda(beneficioService.save(proposta.getBeneficioPotencialDemanda()));
        }
        if(proposta.getBeneficioRealDemanda().getCodigoBeneficio() == null){
            proposta.setBeneficioRealDemanda(beneficioService.save(proposta.getBeneficioRealDemanda()));
        }

//        proposta.setRecursosProposta(recursos);
        proposta.setStatusDemanda(Status.ASSESSMENT);
        proposta.setVersion(proposta.getVersion() + 1);

        return ResponseEntity.status(HttpStatus.OK).body(propostaService.save(proposta));
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
