package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.PropostaDTO;
import weg.com.Low.model.entity.Proposta;
import weg.com.Low.model.service.PropostaService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/proposta")
public class PropostaController {
    private PropostaService propostaService;

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
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PropostaDTO propostaDTO) {
        Proposta proposta = new Proposta();
        BeanUtils.copyProperties(propostaDTO, proposta);
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
