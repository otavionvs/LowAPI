package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import weg.com.Low.dto.RascunhoDTO;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Rascunho;
import weg.com.Low.model.enums.Moeda;
import weg.com.Low.model.enums.Status;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.UsuarioService;
import weg.com.Low.security.TokenUtils;
import weg.com.Low.util.DemandaUtil;
import weg.com.Low.util.RascunhoUtil;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/rascunho")
public class RascunhoController {
    private DemandaService demandaService;
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Object> save(
            @RequestBody RascunhoDTO rascunhoDTO,
            HttpServletRequest request) {
        Demanda rascunho = new Demanda();
        BeanUtils.copyProperties(rascunhoDTO, rascunho);

        rascunho.setStatusDemanda(Status.DRAFT);

        rascunho.setVersion(0);
        rascunho.setCodigoDemanda(demandaService.countByVersion() + 1);

        rascunho.setSolicitanteDemanda(usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get());

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(rascunho, TipoNotificacao.SEM_NOTIFICACAO));
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(
            @RequestParam("arquivos") MultipartFile[] arquivos, @RequestParam("rascunho") String rascunhoJson) {
        System.out.println("Entrou");
        System.out.println(rascunhoJson);
        //Transforma o formato (json) para o modelo de objeto
        RascunhoUtil rascunhoUtil = new RascunhoUtil();
        Demanda rascunhoNovo = rascunhoUtil.convertJsonToModel(rascunhoJson);
        if (!arquivos[0].getOriginalFilename().equals("")) {
            rascunhoNovo.setArquivos(arquivos);
        }

        if (!demandaService.existsById(rascunhoNovo.getCodigoDemanda())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este Rascunho não existe!");
        }
        Demanda rascunho = demandaService.findLastDemandaById(rascunhoNovo.getCodigoDemanda()).get();

        if(rascunhoNovo.getBeneficioRealDemanda().getMoedaBeneficio() == null){
        rascunhoNovo.getBeneficioRealDemanda().setMoedaBeneficio(Moeda.Real);
        }
        if(rascunhoNovo.getBeneficioPotencialDemanda().getMoedaBeneficio() == null) {
        rascunhoNovo.getBeneficioPotencialDemanda().setMoedaBeneficio(Moeda.Real);
        }

        if(rascunho.getBeneficioPotencialDemanda() != null){
        rascunhoNovo.getBeneficioPotencialDemanda().setCodigoBeneficio(rascunho.getBeneficioPotencialDemanda().getCodigoBeneficio());
        }
        if(rascunho.getBeneficioRealDemanda() != null){
        rascunhoNovo.getBeneficioRealDemanda().setCodigoBeneficio(rascunho.getBeneficioRealDemanda().getCodigoBeneficio());
        }
        rascunhoNovo.setSolicitanteDemanda(rascunho.getSolicitanteDemanda());
        rascunhoNovo.setVersion(0);

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.save(rascunhoNovo, TipoNotificacao.SEM_NOTIFICACAO));
    }

}
