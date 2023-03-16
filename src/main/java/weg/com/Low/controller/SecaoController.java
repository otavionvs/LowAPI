//package weg.com.Low.controller;
//
//import lombok.AllArgsConstructor;
//import org.springframework.beans.BeanUtils;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import weg.com.Low.dto.SecaoDTO;
//import weg.com.Low.model.entity.Secao;
//import weg.com.Low.model.service.SecaoService;
//
//import javax.validation.Valid;
//import java.util.List;
//import java.util.Optional;
//
//@CrossOrigin
//@AllArgsConstructor
//@Controller
//@RequestMapping("/secao")
//public class SecaoController {
//    private SecaoService secaoService;
//
//    @GetMapping
//    public ResponseEntity<List<Secao>> findAll() {
//        return ResponseEntity.status(HttpStatus.OK).body(secaoService.findAll());
//    }
//
//    @GetMapping("/{codigo}")
//    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
//        Optional<Secao> secaoOptional = secaoService.findById(codigo);
//        if (secaoOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sessão não encontrada!");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(secaoOptional.get());
//    }
//
//    @PostMapping
//    public ResponseEntity<Object> save(@RequestBody @Valid SecaoDTO secaoDTO) {
//        Secao secao = new Secao();
//        BeanUtils.copyProperties(secaoDTO, secao);
//        return ResponseEntity.status(HttpStatus.OK).body(secaoService.save(secao));
//    }
//}
