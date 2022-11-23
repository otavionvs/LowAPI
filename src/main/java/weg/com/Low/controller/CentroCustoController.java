package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.CentroCustoDTO;
import weg.com.Low.model.entity.CentroCusto;
import weg.com.Low.model.service.CentroCustoService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("low-api/centro-custo")
public class CentroCustoController {
    private CentroCustoService centroCustoService;


    @PostMapping
    public ResponseEntity<Object> save(
            @RequestBody @Valid CentroCustoDTO centroCustoDTO) {
        if (centroCustoService.existsByNome(centroCustoDTO.getNome())) {
            return ResponseEntity.badRequest().body("Centro de Custo já existe");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CentroCusto>> findAll() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(centroCustoService.findAll());
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteById(
            @PathVariable(value = "isbn") Integer codigoCentro
    ) {
        if (!centroCustoService.existsById(codigoCentro)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado");
        } else {
            centroCustoService.deleteById(codigoCentro);
            return ResponseEntity.status(HttpStatus.OK).body("Livro deletado com sucesso");
        }
    }
}
