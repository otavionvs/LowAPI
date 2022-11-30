package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.BeneficioDTO;
import weg.com.Low.model.entity.Beneficio;
import weg.com.Low.model.service.BeneficioService;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/beneficio")
public class BeneficioController {
    private BeneficioService beneficioService;

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Beneficio> beneficioOptional = beneficioService.findById(codigo);
        if (beneficioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Beneficio não encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(beneficioOptional.get());
    }


    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid BeneficioDTO beneficioDTO) {
        Beneficio beneficio = new Beneficio();
        BeanUtils.copyProperties(beneficioDTO, beneficio);
        return ResponseEntity.status(HttpStatus.OK).body(beneficioService.save(beneficio));
    }
}
