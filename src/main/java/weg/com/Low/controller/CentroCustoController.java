package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.CentroCustoDTO;
import weg.com.Low.model.entity.CentroCusto;
import weg.com.Low.model.service.CentroCustoService;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@Controller
@AllArgsConstructor
@RequestMapping("/centro-custo")
public class CentroCustoController {
    private CentroCustoService centroCustoService;

    @PostMapping
    public ResponseEntity<Object> save(
            @RequestBody @Valid CentroCustoDTO centroCustoDTO) {
        if (centroCustoService.existsByNome(centroCustoDTO.getNomeCentroCusto())) {
            return ResponseEntity.badRequest().body("Centro de Custo já existe");
        }

        CentroCusto cc = new CentroCusto();
        BeanUtils.copyProperties(centroCustoDTO, cc);

        return ResponseEntity.status(HttpStatus.OK).body(
                centroCustoService.save(cc));
    }

    @GetMapping
    public ResponseEntity<List<CentroCusto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoService.findAll());
    }

    @DeleteMapping("/{codigoCentroCusto}")
    public ResponseEntity<Object> deleteById(
            @PathVariable(value = "codigoCentroCusto") Integer codigoCentroCusto
    ) {
        if (!centroCustoService.existsById(codigoCentroCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Centro de Custo não encontrado");
        } else {
            centroCustoService.deleteById(codigoCentroCusto);
            return ResponseEntity.status(HttpStatus.OK).body("Centro de Custo deletado com sucesso");
        }
    }
}
