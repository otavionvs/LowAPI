package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.DemandaClassificadaDTO;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.NivelAcesso;
import weg.com.Low.model.enums.Status;
import weg.com.Low.model.service.DemandaClassificadaService;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.UsuarioService;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/demandaClassificada")
public class DemandaClassificadaController {
    private DemandaClassificadaService demandaClassificadaService;
    private UsuarioService usuarioService;
    private DemandaService demandaService;

    //Gets são feitos em DemandaController

    //verificar o analista
    //aprovação do gerente de negocio
    //verificar se demanda existe
    //verificar se demanda não esta sendo usada (status)
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DemandaClassificadaDTO demandaClassificadaDTO) {
        if(usuarioService.existsById(demandaClassificadaDTO.getAnalista().getCodigoUsuario())){
            Usuario usuario = (Usuario) usuarioService.findById(demandaClassificadaDTO.getAnalista().getCodigoUsuario()).get();
            if(usuario.getNivelAcessoUsuario() != NivelAcesso.Analista && usuario.getNivelAcessoUsuario() != NivelAcesso.GestorTI){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Acesso a criação de demanda negado!");
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        Optional demandaOptional = demandaService.findLastDemandaById(demandaClassificadaDTO.getCodigoDemanda());
        if(demandaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda não encontrada");
        }

        Demanda demanda = (Demanda) demandaOptional.get();

        if(demanda.getStatusDemanda() != Status.BACKLOG_CLASSIFICACAO){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Demanda já foi classificada!");
        }
        DemandaClassificada demandaClassificada = new DemandaClassificada();
        BeanUtils.copyProperties(demandaClassificadaDTO, demandaClassificada);
        BeanUtils.copyProperties(demanda, demandaClassificada);

        //Alguns atributos precisam ser setados manualmente
        demandaClassificada.setStatusDemanda(Status.BACKLOG_APROVACAO);
        demandaClassificada.setVersion(demandaClassificada.getVersion() + 1);

        //O sout n deve ser tirado
        System.out.println(demandaClassificada.getCentroCustosDemanda());

        //Alguns precisam ser salvos novamente no banco
        demandaClassificada.setArquivosClassificada(demanda.getArquivosDemanda());

        return ResponseEntity.status(HttpStatus.OK).body(demandaClassificadaService.save(demandaClassificada));
    }

//    //verificar o status da demanda
//    @PutMapping("/{codigo}")
//    public ResponseEntity<Object> update(
//            @PathVariable(value = "codigo") Integer codigo,
//            @RequestBody @Valid DemandaClassificadaDTO demandaClassificadaDTO) {
//        if (!demandaClassificadaService.existsById(codigo)) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta demandaClassificada não existe!");
//        }
//        DemandaClassificada demandaClassificada = demandaClassificadaService.findById(codigo).get();
//        BeanUtils.copyProperties(demandaClassificadaDTO, demandaClassificada);
//        return ResponseEntity.status(HttpStatus.OK).body(demandaClassificadaService.save(demandaClassificada));
//    }

//    //verificar o status da demanda
//    @DeleteMapping("/{codigo}")
//    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
//        if (!demandaClassificadaService.existsById(codigo)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("demandaClassificada não encontrada!");
//        }
//        demandaClassificadaService.deleteById(codigo);
//        return ResponseEntity.status(HttpStatus.OK).body("demandaClassificada Deletada!");
//    }

}
