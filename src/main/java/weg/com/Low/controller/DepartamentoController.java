package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.DepartamentoDTO;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Departamento;
import weg.com.Low.model.service.DepartamentoService;
import weg.com.Low.util.GeradorPDF;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/departamento")
public class DepartamentoController {
    private DepartamentoService departamentoService;

    private GeradorPDF geradorPDF;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        String dados = "Informações a serem incluídas no PDF";
        geradorPDF.gerarPDFDemanda(new Demanda());
        return ResponseEntity.status(HttpStatus.OK).body("testando pdf");
//        return ResponseEntity.status(HttpStatus.OK).body(departamentoService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {


        Optional<Departamento> departamentoOptional = departamentoService.findById(codigo);
        if (departamentoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Departamento não encontrado!");
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
