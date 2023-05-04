package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;
import weg.com.Low.dto.DemandaDTO;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.NivelAcesso;
import weg.com.Low.model.enums.Status;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.model.service.*;
import weg.com.Low.security.TokenUtils;
import weg.com.Low.util.DemandaUtil;
import weg.com.Low.util.GeradorPDF;
import weg.com.Low.util.PropostaUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/demanda")
public class DemandaController {
    private DemandaService demandaService;
    private BeneficioService beneficioService;
    private UsuarioService usuarioService;
    private CentroCustoService centroCustoService;
    private NotificacaoService notificacaoService;

    @GetMapping
    public ResponseEntity<List<Demanda>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Demanda> demandaOptional = demandaService.findLastDemandaById(codigo);
        if (demandaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(demandaOptional.get());
    }

    @GetMapping("versoes/{codigo}")
    public ResponseEntity<Object> findByIdAll(@PathVariable(value = "codigo") Integer codigo) {
        List<Demanda> demandas = demandaService.findByCodigoDemanda(codigo);
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(demandas);
    }

    @GetMapping("/pdf/{codigo}")
    public ResponseEntity<Object> download(@PathVariable(value = "codigo") Integer codigo) {
        Demanda demanda = demandaService.findLastDemandaById(codigo).get();
        if (demanda == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda encontrada!");
        }
        GeradorPDF geradorPDF = new GeradorPDF();
        ByteArrayOutputStream baos = geradorPDF.gerarPDFDemanda(demanda);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "documento.pdf");
        headers.setContentLength(baos.size());

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }

    //É necessário ter todos os campos mesmos que vazios("")
    //Este filtro deve ser usado para todos os usuários, porém quando for um solicitante ou gerente de negócio,
    //o fron-end deve mandar sempre o departamento correspondente ao usuário pré-definido, caso for analista, deve
    //deixar a opção aberta no filtro especializado.
    @GetMapping("/filtro")
    public ResponseEntity<List<Demanda>> search(
            @RequestParam("tituloDemanda") String tituloDemanda,
            @RequestParam("solicitante") String solicitante,
            @RequestParam("codigoDemanda") String codigoDemanda,
            @RequestParam("status") String status,
            @RequestParam("tamanho") String tamanho,
            @RequestParam("analista") String analista,
            @RequestParam("departamento") String departamento,
            @RequestParam("ordenar") String ordenar,
            @PageableDefault(
                    page = 0,
                    size = 24) Pageable page){

        //requisições com tamanho e analista, exigem demanda analista(Backlog_Aprovação)
        if(tamanho.equals("") && analista.equals("")){
            return ResponseEntity.status(HttpStatus.OK).body(demandaService.search(tituloDemanda, solicitante, codigoDemanda,
                    status, departamento, ordenar, page));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(demandaService.search(tituloDemanda, solicitante, codigoDemanda,
                    status, tamanho, analista, departamento, ordenar, page));
        }
    }

    //Retorna uma quantidade de demandas de cada status - para analista
    @GetMapping("/status")
    public ResponseEntity<List<List<Demanda>>> search(
            @PageableDefault(
                    page = 0,
                    size = 12,
            sort = "status_demanda",
            direction = Sort.Direction.ASC) Pageable page, HttpServletRequest request) {
        List<List<Demanda>> listaDemandas = new ArrayList<>();
        //envia o nome de cada status, usando o metodo search

        TokenUtils tokenUtils = new TokenUtils();
        Usuario usuario = usuarioService.findByUserUsuario(tokenUtils.getUsuarioUsernameByRequest(request)).get();

        if(usuario.getNivelAcessoUsuario() == NivelAcesso.Analista || usuario.getNivelAcessoUsuario() == NivelAcesso.GestorTI){
            for (int i = 0; i < 10; i++) {
                listaDemandas.add(demandaService.search(Status.values()[i] + "", page));
            }
        }else if(usuario.getNivelAcessoUsuario() == NivelAcesso.Solicitante || usuario.getNivelAcessoUsuario() == NivelAcesso.GerenteNegocio){
            for (int i = 0; i < 10; i++) {
                listaDemandas.add(demandaService.search(Status.values()[i] + "",usuario.getDepartamentoUsuario().getCodigoDepartamento() , page));
            }
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
        if(!arquivos[0].getOriginalFilename().equals("")){
            demanda.setArquivos(arquivos);
        }
        if (!usuarioService.existsById(demanda.getSolicitanteDemanda().getCodigoUsuario())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Solicitante não encontrado!");
        }

        if (!centroCustoService.verificaPorcentagemCentroCusto(demanda.getCentroCustosDemanda())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Falta completar as porcentagem de centro de custos");
        }

        centroCustoService.saveAll(demanda.getCentroCustosDemanda());

        if(demanda.getBeneficioPotencialDemanda().getValorBeneficio() != null &&
        demanda.getBeneficioPotencialDemanda().getMemoriaDeCalculoBeneficio() != null){
        demanda.setBeneficioPotencialDemanda(beneficioService.save(demanda.getBeneficioPotencialDemanda()));
        }else{
            demanda.setBeneficioPotencialDemanda(null);
        }
        if(demanda.getBeneficioRealDemanda().getValorBeneficio() !=null &&
        demanda.getBeneficioRealDemanda().getMemoriaDeCalculoBeneficio() != null){
        demanda.setBeneficioRealDemanda(beneficioService.save(demanda.getBeneficioRealDemanda()));
        }else{
            demanda.setBeneficioRealDemanda(null);
        }

        demanda.setStatusDemanda(Status.BACKLOG_CLASSIFICACAO);

        demanda.setVersion(0);
        demanda.setCodigoDemanda(demandaService.countByVersion() + 1);

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demanda, TipoNotificacao.CRIOU_DEMANDA));
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(
            @RequestParam("arquivos") MultipartFile[] arquivos, @RequestParam("demanda") String demandaJson) {
        DemandaUtil demandaUtil = new DemandaUtil();
        Demanda demandaNova = demandaUtil.convertJsonToModel(demandaJson);
        if(!arquivos[0].getOriginalFilename().equals("")){
            demandaNova.setArquivos(arquivos);
        }

        if (!demandaService.existsById(demandaNova.getCodigoDemanda())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta demanda não existe!");
        }

        centroCustoService.saveAll(demandaNova.getCentroCustosDemanda());

        if(demandaNova.getBeneficioPotencialDemanda().getValorBeneficio() != null &&
                demandaNova.getBeneficioPotencialDemanda().getMemoriaDeCalculoBeneficio() != null){
            demandaNova.setBeneficioPotencialDemanda(beneficioService.save(demandaNova.getBeneficioPotencialDemanda()));
        }else{
            demandaNova.setBeneficioPotencialDemanda(null);
        }

        if(demandaNova.getBeneficioRealDemanda().getValorBeneficio() !=null &&
                demandaNova.getBeneficioRealDemanda().getMemoriaDeCalculoBeneficio() != null){
            demandaNova.setBeneficioRealDemanda(beneficioService.save(demandaNova.getBeneficioRealDemanda()));
        }else{
            demandaNova.setBeneficioRealDemanda(null);
        }

        Demanda demanda = demandaService.findLastDemandaById(demandaNova.getCodigoDemanda()).get();

        demandaNova.setVersion(demanda.getVersion() + 1);

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demandaNova, TipoNotificacao.EDITOU_DEMANDA));
    }

    //Caso seja passado por parametro 1 - passa para o proximo status
    //Parametro != 1 - Cancela a demanda
    @PutMapping("update/status")
    public ResponseEntity<Object> updateAprovacao(
            @RequestParam("codigo") @NotNull Integer codigo,
            @RequestParam("decisao") @NotNull Integer decisao,
            HttpServletRequest request) {
        if (!demandaService.existsById(codigo)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta demanda não existe");
        }
        DemandaClassificada demanda = (DemandaClassificada) demandaService.findLastDemandaById(codigo).get();
        String demandaStatus = demanda.getStatusDemanda().getStatus();

        //Necessario para realizar um put
        DemandaClassificada demandaNova = new DemandaClassificada();
        BeanUtils.copyProperties(demanda, demandaNova);
        demandaNova.setVersion(demanda.getVersion() + 1);

        if (demandaStatus.equals(Status.BACKLOG_APROVACAO.getStatus())) {
            if (decisao == 1) {
                TokenUtils tokenUtils = new TokenUtils();
                String token = tokenUtils.buscarCookie(request);
                String user = tokenUtils.getUsuarioUsername(token);
                Usuario usuario = usuarioService.findByUserUsuario(user).get();
                demandaNova.setGerenteNegocio(usuario);

                demandaNova.setStatusDemanda(Status.BACKLOG_PROPOSTA);
            } else {
                demandaNova.setStatusDemanda(Status.CANCELLED);
            }
        } else if (demandaStatus.equals(Status.TO_DO.getStatus())) {
            if (decisao == 1) {
                demandaNova.setStatusDemanda(Status.DESIGN_AND_BUILD);
            } else {
                demandaNova.setStatusDemanda(Status.CANCELLED);
            }
        } else if (demandaStatus.equals(Status.DESIGN_AND_BUILD.getStatus())) {
            if (decisao == 1) {
                demandaNova.setStatusDemanda(Status.SUPPORT);
            } else {
                demandaNova.setStatusDemanda(Status.CANCELLED);
            }
        } else if (demandaStatus.equals(Status.SUPPORT.getStatus())) {
            if (decisao == 1) {
                demandaNova.setStatusDemanda(Status.DONE);
            } else {
                demandaNova.setStatusDemanda(Status.CANCELLED);
            }

        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta demanda não pertence ao status solicitado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demandaNova,
                (decisao == 1 ? TipoNotificacao.AVANCOU_STATUS_DEMANDA : TipoNotificacao.CANCELOU_DEMANDA)));
    }

    //Reprova uma demanda
    @PutMapping("/cancell/{codigoDemanda}")
    public ResponseEntity<Object> updateAprovacao(
            @PathVariable(value = "codigoDemanda") Integer codigoDemanda, @RequestBody @NotBlank String motivoReprovacao) {

        Demanda demanda = demandaService.findLastDemandaById(codigoDemanda).get();
        demanda.setMotivoReprovacaoDemanda(motivoReprovacao);
        demanda.setStatusDemanda(Status.CANCELLED);
        //Necessário para a realização de um PUT
        Demanda demandaNova = new Demanda();
        BeanUtils.copyProperties(demanda, demandaNova);
        demandaNova.setVersion(demanda.getVersion() + 1);
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(demandaNova, TipoNotificacao.CANCELOU_DEMANDA));
    }

//    //Não Deleta todas as demandas do codigo
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional demandaOptional = demandaService.findLastDemandaById(codigo);
        if (demandaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda não encontrada!");
        }
        Demanda demanda = (Demanda) demandaOptional.get();
        beneficioService.deleteById(demanda.getBeneficioPotencialDemanda().getCodigoBeneficio());
//        beneficioService.deleteById(demanda.getBeneficioRealDemanda().getCodigoBeneficio());

        demandaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.OK).body("Demanda Deletada!");
    }
}