package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import weg.com.Low.dto.DemandaDTO;
import weg.com.Low.dto.NotificacaoDTO;
import weg.com.Low.dto.StatusDTO;
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

    //É necessário ter todos os campos mesmos que vazios("")
    @GetMapping("/filtro")
    public ResponseEntity<List<Demanda>> search(
            @RequestParam("tituloDemanda") String tituloDemanda,
            @RequestParam("solicitante") String solicitante,
            @RequestParam("codigoDemanda") String codigoDemanda,
            @RequestParam("status") String status,
            @RequestParam("tamanho") String tamanho,
            @RequestParam("analista") String analista,
            @RequestParam("departamento") String departamento,
            @PageableDefault(
                    page = 0,
                    size = 24) Pageable page){
        //requisições com tamanho e analista, exigem demanda analista(Backlog_Aprovação)
        if(tamanho.equals("") && analista.equals("")){
            return ResponseEntity.status(HttpStatus.OK).body(demandaService.search(tituloDemanda, solicitante, codigoDemanda,
                    status, departamento, page));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(demandaService.search(tituloDemanda, solicitante, codigoDemanda,
                    status, tamanho, analista, departamento, page));
        }
    }

    //Retorna uma quantidade de demandas de cada status
    @GetMapping("/status")
    public ResponseEntity<List<List<Demanda>>> search(
            @PageableDefault(
                    page = 0,
                    size = 12,
            sort = "status_demanda",
            direction = Sort.Direction.ASC) Pageable page) {
        List<List<Demanda>> listaDemandas = new ArrayList<>();
        //envia o nome de cada status, usando o metodo search
        for (int i = 0; i < 10; i++) {
            listaDemandas.add(demandaService.search(Status.values()[i] + "", page));
        }

        return ResponseEntity.status(HttpStatus.OK).body(listaDemandas);
    }

    //Retorna uma lista com até dois status enviados
    @GetMapping("/filtro/status")
    public ResponseEntity<List<Demanda>> search(
            @RequestParam("status1") String status1,
            @RequestParam("status2") String status2,
            @PageableDefault(
                    page = 0,
                    size = 10) Pageable page){
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.search(status1,status2,page));
    }

    //Exige de outro formato para enviar as informações (body - form data)
    @PostMapping
    public ResponseEntity<Object> save(@RequestParam("arquivos") MultipartFile[] arquivos, @RequestParam("demanda") String demandaJson) {
        //Transforma o formato (json) para o modelo de objeto
        DemandaUtil demandaUtil = new DemandaUtil();
        Demanda demanda = demandaUtil.convertJsonToModel(demandaJson);
        demanda.setArquivos(arquivos);
        System.out.println(demanda.getArquivosDemanda());
        if (!usuarioService.existsById(demanda.getSolicitanteDemanda().getCodigoUsuario())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Solicitante não encontrado!");
        }
        List<CentroCusto> centroCustos = demanda.getCentroCustos();
        for (int i = 0; i < demanda.getCentroCustos().size(); i++) {
            if (!centroCustoService.existsById(centroCustos.get(i).getCodigoCentroCusto())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Centro de Custo não encontrado!");
            }
        }

        //Registra os beneficios enviados - evita repetir estrutura
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

    @PutMapping("/update/{codigo}")
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

    //Caso seja passado por parametro 1 - passa para o proximo status
    //Parametro != 1 - Cancela a demanda
    @PutMapping("update/status/{codigo}")
    public ResponseEntity<Object> updateAprovacao(
            @PathVariable(value = "codigo") Integer codigoDemanda,
            @RequestBody @Valid Integer decisao) {
        if (!demandaService.existsById(codigoDemanda)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta demanda não existe");
        }
        Demanda demanda = demandaService.findById(codigoDemanda).get();
        String demandaStatus = demanda.getStatusDemanda().getStatus();

        if (demandaStatus.equals(Status.BACKLOG_APROVACAO.getStatus())) {
            if (decisao == 1) {
                demanda.setStatusDemanda(Status.BACKLOG_PROPOSTA);
            } else {
                demanda.setStatusDemanda(Status.CANCELLED);
            }
        } else if (demandaStatus.equals(Status.TO_DO.getStatus())) {
            if (decisao == 1) {
                demanda.setStatusDemanda(Status.DESIGN_AND_BUILD);
            } else {
                demanda.setStatusDemanda(Status.CANCELLED);
            }
        } else if (demandaStatus.equals(Status.DESIGN_AND_BUILD.getStatus())) {
            if (decisao == 1) {
                demanda.setStatusDemanda(Status.SUPPORT);
            } else {
                demanda.setStatusDemanda(Status.CANCELLED);
            }
        } else if (demandaStatus.equals(Status.SUPPORT.getStatus())) {
            if (decisao == 1) {
                demanda.setStatusDemanda(Status.DONE);
            } else {
                demanda.setStatusDemanda(Status.CANCELLED);
            }
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta demanda não pertence ao status solicitado!");
        }

        System.out.println("Demanda 2 " + demanda);
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
