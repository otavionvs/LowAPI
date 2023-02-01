package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.ReuniaoDTO;
import weg.com.Low.model.entity.Reuniao;
import weg.com.Low.model.entity.StatusReuniao;
import weg.com.Low.model.service.ReuniaoService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/reuniao")
public class ReuniaoController {
    private ReuniaoService reuniaoService;

    @GetMapping
    public ResponseEntity<List<Reuniao>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Reuniao> reuniaoOptional = reuniaoService.findById(codigo);
        if (reuniaoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reunião não encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoOptional.get());
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<Reuniao>> search(
            @RequestParam("nomeComissao") String nomeComissao,
            @RequestParam("dataReuniao") String dataReuniao,
            @RequestParam("statusReuniao") String statusReuniao,
            @RequestParam("ppmProposta") String ppmProposta,
            @RequestParam("analista") String analista,
            @RequestParam("solicitante") String solicitante,
            @PageableDefault(
                    page = 0,
                    size = 10) Pageable page){
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.search(nomeComissao, dataReuniao, statusReuniao,
                ppmProposta, analista, solicitante, page.getOffset(), page.getPageSize()));
    }

//    String nomeComissao, String dataReuniao, String statusReuniao,
//    String ppmProposta, String analista, String solicitante, Pageable page


    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ReuniaoDTO reuniaoDTO) {
        Reuniao reuniao = new Reuniao();
        BeanUtils.copyProperties(reuniaoDTO, reuniao);
        Long tempo = reuniao.getDataReuniao().getTime() - new Date().getTime();
        if(tempo > 0 && tempo < 1000){
            reuniao.setStatusReuniao(StatusReuniao.PROXIMO);
        }else{
            reuniao.setStatusReuniao(StatusReuniao.AGUARDANDO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao));
    }

    @PutMapping("/update/{codigo}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "codigo") Integer codigo,
            @RequestBody @Valid ReuniaoDTO reuniaoDTO) {
        if (!reuniaoService.existsById(codigo)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta reunião não existe!");
        }

        Reuniao reuniao = reuniaoService.findById(codigo).get();
        BeanUtils.copyProperties(reuniaoDTO, reuniao);

        Long tempo = reuniao.getDataReuniao().getTime() - new Date().getTime();
        if(tempo > 0 && tempo < 1000){
            reuniao.setStatusReuniao(StatusReuniao.PROXIMO);
        }else if(tempo < 0){
            reuniao.setStatusReuniao(StatusReuniao.PENDENTE);
        }else{
            reuniao.setStatusReuniao(StatusReuniao.AGUARDANDO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(reuniaoService.save(reuniao));
    }
}
