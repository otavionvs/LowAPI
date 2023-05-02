package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.MensagensDTO;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Mensagens;
import weg.com.Low.model.entity.Proposta;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.MensagensService;
import weg.com.Low.model.service.UsuarioService;

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

    @GetMapping("/{codigo}")
    public ResponseEntity<?> findAllByDemanda(@PathVariable(value = "codigo") Integer codigo) {
        if(!demandaService.existsById(codigo)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda não encontrada!");
        }
        return ResponseEntity.ok(mensagensService.findAllByDemanda(demandaService.findLastDemandaById(codigo).get()));
    }

    @MessageMapping("/demanda/{codigo}")
    @SendTo("/demanda/{codigo}/chat")
    public Object    save(@Payload MensagensDTO mensagensDTO) {
        System.out.println(mensagensDTO.getDemandaMensagens());
        System.out.println(mensagensDTO.getTextoMensagens());
        System.out.println(mensagensDTO.getUsuarioMensagens());

        Mensagens mensagens = new Mensagens();
        BeanUtils.copyProperties(mensagensDTO, mensagens);
        Demanda demanda = demandaService.save(demandaService.findLastDemandaById(mensagens.getDemandaMensagens().getCodigoDemanda()).get());
        mensagens.setDemandaMensagens(demanda);
        Usuario usuario = usuarioService.save(usuarioService.findById(mensagens.getUsuarioMensagens().getCodigoUsuario()).get());
        mensagens.setUsuarioMensagens(usuario);
        Mensagens mensagens1 = mensagensService.save(mensagens);
        return mensagens1;
    }
}
