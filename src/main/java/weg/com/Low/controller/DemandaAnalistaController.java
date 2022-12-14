package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.DemandaAnalistaDTO;
import weg.com.Low.model.entity.DemandaAnalista;
import weg.com.Low.model.entity.NivelAcesso;
import weg.com.Low.model.entity.Status;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.service.DemandaAnalistaService;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.UsuarioService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/demandaAnalista")
public class DemandaAnalistaController {
    private DemandaAnalistaService demandaAnalistaService;
    private UsuarioService usuarioService;
    private DemandaService demandaService;

    @GetMapping
    public ResponseEntity<List<DemandaAnalista>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(demandaAnalistaService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<DemandaAnalista> demandaAnalistaOptional = demandaAnalistaService.findById(codigo);
        if (demandaAnalistaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DemandaAnalista não encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(demandaAnalistaOptional.get());
    }

    //verificar o analista
    //aprovação do gerente de negocio
    //verificar se demanda existe
    //verificar se demanda não esta sendo usada (status)
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DemandaAnalistaDTO demandaAnalistaDTO) {
        if(usuarioService.existsById(demandaAnalistaDTO.getAnalista().getCodigoUsuario())){
            Usuario usuario = (Usuario) usuarioService.findById(demandaAnalistaDTO.getAnalista().getCodigoUsuario()).get();
            if(usuario.getNivelAcessoUsuario() != NivelAcesso.Analista && usuario.getNivelAcessoUsuario() != NivelAcesso.GestorTI){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Acesso a criação de demanda negado!");
            }
        }
        if(!demandaService.existsById(demandaAnalistaDTO.getDemandaDemandaAnalista().getCodigoDemanda())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda não encontrada");
        }

        if(demandaAnalistaDTO.getDemandaDemandaAnalista().getStatus() != Status.BACKLOG_CLASSIFICACAO){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Demanda já foi classificada!");
        }

        DemandaAnalista demandaAnalista = new DemandaAnalista();
        BeanUtils.copyProperties(demandaAnalistaDTO, demandaAnalista);
        demandaAnalistaDTO.getDemandaDemandaAnalista().setStatus(Status.BACKLOG_APROVACAO);
        return ResponseEntity.status(HttpStatus.OK).body(demandaAnalistaService.save(demandaAnalista));
    }

    //verificar o status da demanda
    @PutMapping("/{codigo}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "codigo") Integer codigo,
            @RequestBody @Valid DemandaAnalistaDTO demandaAnalistaDTO) {
        if (!demandaAnalistaService.existsById(codigo)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta demandaAnalista não existe!");
        }
        DemandaAnalista demandaAnalista = demandaAnalistaService.findById(codigo).get();
        BeanUtils.copyProperties(demandaAnalistaDTO, demandaAnalista);
        return ResponseEntity.status(HttpStatus.OK).body(demandaAnalistaService.save(demandaAnalista));
    }

    //verificar o status da demanda
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        if (!demandaAnalistaService.existsById(codigo)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("demandaAnalista não encontrada!");
        }
        demandaAnalistaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.OK).body("demandaAnalista Deletada!");
    }

}
