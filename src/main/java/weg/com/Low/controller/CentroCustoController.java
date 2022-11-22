package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weg.com.Low.dto.CentroCustoDTO;
import weg.com.Low.model.service.CentroCustoService;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("low-api/centro-custo")
public class CentroCustoController {
    private CentroCustoService centroCustoService;


    @PostMapping
    public ResponseEntity<Object>save(
            @RequestBody @Valid CentroCustoDTO centroCustoDTO){
        if(centroCustoService.existsByNome(centroCustoDTO.getNome())){
            return ResponseEntity.badRequest().body("Centro de Custo já existe");
        }
        return ResponseEntity.ok().build();
    }
}
