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
import org.springframework.web.multipart.MultipartFile;
import weg.com.Low.dto.DemandaDTO;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.service.BeneficioService;
import weg.com.Low.model.service.CentroCustoService;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.UsuarioService;
import weg.com.Low.util.DemandaUtil;

import javax.validation.Valid;
import java.util.ArrayList;
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

    @GetMapping("/filtro")
    public ResponseEntity<Page<Demanda>> search(
            @RequestParam("tituloDemanda") String tituloDemanda,
            @RequestParam("solicitante") String solicitante,
            @RequestParam("codigoDemanda") String codigoDemanda,
            @RequestParam("status") String status,
            @RequestParam("tamanho") String tamanho,
            @PageableDefault(
                    page = 0,
                    size = 10) Pageable page) {
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.search(tituloDemanda, solicitante, codigoDemanda, status, tamanho, page));
    }

    //Retorna uma quantidade de demandas de cada status
    @GetMapping("/status")
    public ResponseEntity<List<Page<Demanda>>> search(
            @PageableDefault(
                    page = 0,
                    size = 12) Pageable page) {
        List<Page<Demanda>> listaDemandas = new ArrayList<>();

        //envia o nome de cada status, usando o metodo search
        for (int i = 0; i < 10; i++) {
            listaDemandas.add(demandaService.search(Status.values()[i] + "", page));
        }

        return ResponseEntity.status(HttpStatus.OK).body(listaDemandas);
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestParam("arquivos") MultipartFile[] arquivos, @RequestParam("demanda") String demandaJson) {
        DemandaUtil demandaUtil = new DemandaUtil();
        Demanda demanda =  demandaUtil.convertJsonToModel(demandaJson);
        System.out.println(arquivos);
        demanda.setArquivos(arquivos);
        System.out.println(demanda.getArquivosDemanda());
        if(!usuarioService.existsById(demanda.getSolicitanteDemanda().getCodigoUsuario())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Solicitante não encontrado!");
        }
        List<CentroCusto> centroCustos = demanda.getCentroCustos();
        for (int i = 0; i < demanda.getCentroCustos().size(); i ++){
            if(!centroCustoService.existsById(centroCustos.get(i).getCodigoCentroCusto())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Centro de Custo não encontrado!");
            }
        }


        Beneficio beneficioPotencial = new Beneficio();
        Beneficio beneficioReal = new Beneficio();

        BeanUtils.copyProperties(demanda.getBeneficioPotencialDemanda(), beneficioPotencial);
        BeanUtils.copyProperties(demanda.getBeneficioRealDemanda(), beneficioReal);

        beneficioPotencial = beneficioService.save(beneficioPotencial);
        beneficioReal = beneficioService.save(beneficioReal);

        demanda.setBeneficioPotencialDemanda(beneficioPotencial);
        demanda.setBeneficioRealDemanda(beneficioReal);
        demanda.setStatusDemanda(Status.BACKLOG_CLASSIFICACAO);

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demanda));
    }

    @PutMapping("update/{codigo}")
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

    @PutMapping("update/backlog/{codigo}")
    public ResponseEntity<Object> updateAprovacao(
            @PathVariable(value = "codigo") Integer codigoDemanda,
            @RequestParam("decisao") String decisao) {
        System.out.println("deciiiiiiiiii" + decisao);

        if (!demandaService.existsById(codigoDemanda)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta demanda não existe");
        }

        Demanda demanda = demandaService.findById(codigoDemanda).get();

        System.out.println("ok" + demanda);

        if (decisao == "Aprovado") {
            demanda.setStatusDemanda(Status.BACKLOG_PROPOSTA);
        } else {
            demanda.setStatusDemanda(Status.CANCELLED);
        }

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demanda));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional demandaOptional = demandaService.findById(codigo);
        if (demandaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda não encontrada!");
        }
        Demanda demanda = (Demanda) demandaOptional.get();
        beneficioService.deleteById(demanda.getBeneficioPotencialDemanda().getCodigoBeneficio());
        beneficioService.deleteById(demanda.getBeneficioRealDemanda().getCodigoBeneficio());

        demandaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.OK).body("Demanda Deletada!");
    }


}
