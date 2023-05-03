package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.MensagensDTO;
import weg.com.Low.model.entity.Mensagens;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.service.*;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.MensagensService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/mensagens")
public class MensagensController {
    private MensagensService mensagensService;
    private DemandaService demandaService;
    private UsuarioService usuarioService;
    private DemandaClassificadaService demandaClassificadaService;
    @GetMapping("/{codigo}")
    public ResponseEntity<?> findAllByDemanda(@PathVariable(value = "codigo") Integer codigo) {
        if(!demandaService.existsById(codigo)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda não encontrada!");
        }
        return ResponseEntity.ok(mensagensService.findAllByDemanda(demandaService.findLastDemandaById(codigo).get()));
    }

    @GetMapping("/demandasDiscutidas/{codigoUsuario}")
    public Object findAllByUsuario(@PathVariable(value = "codigoUsuario") Integer codigoUsuario) {

        if(!demandaService.existsById(codigoUsuario)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demandas não encontradas!");
        }
        Usuario usuario = usuarioService.findById(codigoUsuario).get();
        System.out.println(usuario);
        return ResponseEntity.ok(demandaClassificadaService.findBySolicitanteDemandaOrAnalista(usuario));
    }

    @MessageMapping("/demanda/{codigo}")
    @SendTo("/demanda/{codigo}/chat")
    public Mensagens save(MensagensDTO mensagensDTO) {
        Mensagens mensagens = new Mensagens();
        mensagensDTO.getDemandaMensagens().setVersion(demandaService.findLastDemandaById(mensagensDTO.getDemandaMensagens().getCodigoDemanda()).get().getVersion());
        BeanUtils.copyProperties(mensagensDTO, mensagens);
        return mensagensService.save(mensagens);
    }
}
