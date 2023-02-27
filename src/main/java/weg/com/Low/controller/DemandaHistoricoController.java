//package weg.com.Low.controller;
//
//
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import weg.com.Low.model.entity.Demanda;
//import weg.com.Low.model.entity.DemandaHistorico;
//import weg.com.Low.model.service.DemandaHistoricoService;
//
//import java.util.List;
//
//@CrossOrigin
//@AllArgsConstructor
//@Controller
//@RequestMapping("/demandaHistorico")
//public class DemandaHistoricoController {
//    private DemandaHistoricoService demandaHistoricoService;
//
//    @GetMapping
//    public ResponseEntity<List<DemandaHistorico>> findAll() {
//        return ResponseEntity.status(HttpStatus.OK).body(demandaHistoricoService.findAll());
//    }
//
//    @GetMapping("/porDemanda")
//    public ResponseEntity<List<DemandaHistorico>> findByDemanda(@RequestParam("demanda") Demanda demanda) {
//        return ResponseEntity.status(HttpStatus.OK).body(demandaHistoricoService.findByDemandaDemandaHistorico(demanda));
//    }
//
//
//}
