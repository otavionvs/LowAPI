package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.DepartamentoDTO;
import weg.com.Low.model.entity.Departamento;
import weg.com.Low.model.service.DepartamentoService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/departamento")
public class DepartamentoController {
    private DepartamentoService departamentoService;

    @GetMapping
    public ResponseEntity<List<Departamento>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(departamentoService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Departamento> departamentoOptional = departamentoService.findById(codigo);
        if (departamentoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(departamentoOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DepartamentoDTO departamentoDTO) {
        Departamento departamento = new Departamento();
        BeanUtils.copyProperties(departamentoDTO, departamento);
        return ResponseEntity.status(HttpStatus.OK).body(departamentoService.save(departamento));
    }
}
