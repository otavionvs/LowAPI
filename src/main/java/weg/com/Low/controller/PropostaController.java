package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.PropostaDTO;
import weg.com.Low.dto.RecursoDTO;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.Status;
import weg.com.Low.model.service.*;

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
    private CentroCustoRecursoService centroCustoRecursoService;
    private DemandaService demandaService;
    private DemandaAnalistaService demandaAnalistaService;

    @GetMapping
    public ResponseEntity<List<Proposta>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(propostaService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Proposta> propostaOptional = propostaService.findById(codigo);
        if (propostaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proposta não encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(propostaOptional.get());
    }

    //ver como fica com status de aprovação
    //verificar se centro de custo existe?
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PropostaDTO propostaDTO) {
        List<RecursoDTO> recursosDTO = propostaDTO.getRecursosProposta();
        List<Recurso> recursos = new ArrayList<>();

        Proposta proposta = new Proposta();
        BeanUtils.copyProperties(propostaDTO, proposta);

        for (int i = 0; i < recursosDTO.size(); i++) {
            Recurso recurso = new Recurso();
            RecursoDTO recursoDTO = recursosDTO.get(i);
            BeanUtils.copyProperties(recursoDTO, recurso);
            recurso = recursoService.save(recurso);
            for (int i2 = 0; i2 < recursoDTO.getCentroDeCustoRecurso().size(); i2++) {
                CentroCustoRecurso centroCustoRecurso = new CentroCustoRecurso(null,
                        recursoDTO.getCentroDeCustoRecurso().get(i2), recurso, recursoDTO.getPorcentagemCustoRecurso().get(i2));
                centroCustoRecursoService.save(centroCustoRecurso);

            }
            recursos.add(recurso);
        }
        proposta.setRecursosProposta(recursos);
        try{
            DemandaAnalista demandaAnalista =  demandaAnalistaService.findById(proposta.getDemandaAnalistaProposta().getCodigoDemandaAnalista()).get();
            Demanda demanda = demandaService.findById(demandaAnalista.getDemandaDemandaAnalista().getCodigoDemanda()).get();
            demanda.setStatusDemanda(Status.ASSESSMENT);

            demandaService.save(demanda);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda não encontrada!");
        }


        return ResponseEntity.status(HttpStatus.OK).body(propostaService.save(proposta));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        if (!propostaService.existsById(codigo)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proposta não encontrada!");
        }
        propostaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.OK).body("Proposta Deletada!");
    }
}
