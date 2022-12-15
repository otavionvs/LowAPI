package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.DemandaDTO;
import weg.com.Low.dto.FiltroDemandaDTO;
import weg.com.Low.model.entity.Beneficio;
import weg.com.Low.model.entity.CentroCusto;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.service.BeneficioService;
import weg.com.Low.model.service.CentroCustoService;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.UsuarioService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/demanda")
public class DemandaController {
    private DemandaService demandaService;
    private BeneficioService beneficioService;
    private UsuarioService usuarioService;
    private CentroCustoService centroCustoService;

    @GetMapping
    public ResponseEntity<List<Demanda>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Demanda> demandaOptional = demandaService.findById(codigo);
        if (demandaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda não encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(demandaOptional.get());
    }

    //    @GetMapping("/filtro")
//    public ResponseEntity<List<Produto>> teste(@PageableDefault(sort = "nome",
//            direction = Sort.Direction.ASC,
//            page = 0,
//            size = 10) Pageable page) {
////        PageRequest pageRequest = PageRequest.of(10, 1, Sort.Direction.ASC, "nome");
////        pageRequest = pageRequest.
//
//        List<Produto> produtos = produtoService.findAll(page).getContent();
//        return ResponseEntity.status(HttpStatus.OK).body(produtos);
//    }


    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DemandaDTO demandaDTO) {
        if(!usuarioService.existsById(demandaDTO.getSolicitanteDemanda().getCodigoUsuario())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Solicitante não encontrado!");
        }
        List<CentroCusto> centroCustos = demandaDTO.getCentroCustos();
        for (int i = 0; i < demandaDTO.getCentroCustos().size(); i ++){
            if(!centroCustoService.existsById(centroCustos.get(i).getCodigoCentroCusto())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Centro de Custo não encontrado!");
            }
        }

        Demanda demanda = new Demanda();
        BeanUtils.copyProperties(demandaDTO, demanda);

        Beneficio beneficioPotencial = new Beneficio();
        Beneficio beneficioReal = new Beneficio();

        BeanUtils.copyProperties(demandaDTO.getBeneficioPotencialDemanda(), beneficioPotencial);
        BeanUtils.copyProperties(demandaDTO.getBeneficioRealDemanda(), beneficioReal);

        beneficioPotencial = beneficioService.save(beneficioPotencial);
        beneficioReal = beneficioService.save(beneficioReal);

        demanda.setBeneficioPotencialDemanda(beneficioPotencial);
        demanda.setBeneficioRealDemanda(beneficioReal);

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demanda));
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "codigo") Integer codigo,
            @RequestBody @Valid DemandaDTO demandaDTO) {
        if (!demandaService.existsById(codigo)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta demanda não existe!");
        }

        Demanda demanda = demandaService.findById(codigo).get();
        BeanUtils.copyProperties(demandaDTO, demanda);
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demanda));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional demandaOptional = demandaService.findById(codigo);
        if(demandaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda não encontrada!");
        }
        Demanda demanda = (Demanda) demandaOptional.get();
        beneficioService.deleteById(demanda.getBeneficioPotencialDemanda().getCodigoBeneficio());
        beneficioService.deleteById(demanda.getBeneficioRealDemanda().getCodigoBeneficio());

        demandaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.OK).body("Demanda Deletada!");
    }


}
