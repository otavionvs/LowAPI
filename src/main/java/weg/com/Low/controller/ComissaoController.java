package weg.com.Low.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.ComissaoDTO;
import weg.com.Low.model.entity.Comissao;
import weg.com.Low.model.service.ComissaoService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ComissaoController {

    private ComissaoService comissaoService;


    @PostMapping
    public ResponseEntity<Object> save(
            @RequestBody @Valid ComissaoDTO comissaoDTO) {
        if (comissaoService.existsByNome(comissaoDTO.getNome())) {
            return ResponseEntity.badRequest().body("Comissão já existente");
        }

        Comissao comissao = new Comissao();
        BeanUtils.copyProperties(comissaoDTO, comissao);

        return ResponseEntity.status(HttpStatus.OK).body(
                comissaoService.save(comissao));
    }

    @GetMapping
    public ResponseEntity<List<Comissao>> findAll() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(comissaoService.findAll());
    }

    @DeleteMapping("/{codigoComissao}")
    public ResponseEntity<Object> deleteById(
            @PathVariable(value = "codigoComissao") Integer codigoCentroCusto
    ) {
        if (!comissaoService.existsById(codigoCentroCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comissão não encontrado");
        } else {
            comissaoService.deleteById(codigoCentroCusto);
            return ResponseEntity.status(HttpStatus.OK).body("Comissão deletada com sucesso");
        }
    }
}
