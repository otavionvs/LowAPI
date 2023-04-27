//package weg.com.Low.controller;
//
//import lombok.AllArgsConstructor;
//import org.springframework.beans.BeanUtils;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.web.bind.annotation.*;
//import weg.com.Low.dto.MensagensDTO;
//import weg.com.Low.model.entity.Demanda;
//import weg.com.Low.model.entity.Mensagens;
//import weg.com.Low.model.entity.Proposta;
//import weg.com.Low.model.service.DemandaService;
//import weg.com.Low.model.service.MensagensService;
//
//import java.util.List;
//import java.util.Optional;
//
//@CrossOrigin
//@AllArgsConstructor
//@RestController
//@RequestMapping("/low/mensagens")
//public class MensagensController {
//    private MensagensService mensagensService;
//    private DemandaService demandaService;
//
//    @GetMapping("/{codigo}")
//    public ResponseEntity<?> findAllByDemanda(@PathVariable(value = "codigo") Integer codigo) {
//        if(!demandaService.existsById(codigo)){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda não encontrada!");
//        }
//        return ResponseEntity.ok(mensagensService.findAllByDemanda(demandaService.findLastDemandaById(codigo).get()));
//    }
//
//    @MessageMapping("/{codigo}")
//    @SendTo("/chat/{codigo}")
//    public Mensagens save(MensagensDTO mensagensDTO) {
//        Mensagens mensagens = new Mensagens();
//        BeanUtils.copyProperties(mensagensDTO, mensagens);
//        return mensagensService.save(mensagens);
//    }
//}
