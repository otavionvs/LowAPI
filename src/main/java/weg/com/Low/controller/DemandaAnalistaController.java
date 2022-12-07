package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.DemandaAnalistaDTO;
import weg.com.Low.model.entity.DemandaAnalista;
import weg.com.Low.model.service.DemandaAnalistaService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/demandaAnalista")
public class DemandaAnalistaController {
    private DemandaAnalistaService demandaAnalistaService;

    @GetMapping
    public ResponseEntity<List<DemandaAnalista>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(demandaAnalistaService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<DemandaAnalista> demandaAnalistaOptional = demandaAnalistaService.findById(codigo);
        if (demandaAnalistaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DemandaAnalista não encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(demandaAnalistaOptional.get());
    }

    //verificar o analista
    //aprovação do gerente de negocio
    //verificar se demanda existe
    //verificar se demanda não esta sendo usada (status)
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DemandaAnalistaDTO demandaAnalistaDTO) {
        DemandaAnalista demandaAnalista = new DemandaAnalista();
        BeanUtils.copyProperties(demandaAnalistaDTO, demandaAnalista);
        return ResponseEntity.status(HttpStatus.OK).body(demandaAnalistaService.save(demandaAnalista));
    }

    //verificar o status da demanda
    @PutMapping("/{codigo}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "codigo") Integer codigo,
            @RequestBody @Valid DemandaAnalistaDTO demandaAnalistaDTO) {
        if (!demandaAnalistaService.existsById(codigo)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta demandaAnalista não existe!");
        }

        DemandaAnalista demandaAnalista = demandaAnalistaService.findById(codigo).get();
        BeanUtils.copyProperties(demandaAnalistaDTO, demandaAnalista);
        return ResponseEntity.status(HttpStatus.OK).body(demandaAnalistaService.save(demandaAnalista));
    }

    //verificar o status da demanda
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        if (!demandaAnalistaService.existsById(codigo)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("demandaAnalista não encontrada!");
        }
        demandaAnalistaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.OK).body("demandaAnalista Deletada!");
    }

}
