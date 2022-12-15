package weg.com.Low.controller;

import lombok.AllArgsConstructor;
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
import java.util.Optional;
@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/comissao")
public class ComissaoController {
    private ComissaoService comissaoService;

    @PostMapping
    public ResponseEntity<Object> save(
            @RequestBody @Valid ComissaoDTO comissaoDTO) {
        if (comissaoService.existsByNomeComissao(comissaoDTO.getNomeComissao())) {
            return ResponseEntity.badRequest().body("Comissão já existente");
        }

        Comissao comissao = new Comissao();
        BeanUtils.copyProperties(comissaoDTO, comissao);
        comissao = comissaoService.save(comissao);

        return ResponseEntity.status(HttpStatus.OK).body(comissao);
    }

    @GetMapping
    public ResponseEntity<List<Comissao>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(comissaoService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Comissao> comissaoOptional = comissaoService.findById(codigo);
        if (comissaoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comissão não encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(comissaoOptional.get());
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
