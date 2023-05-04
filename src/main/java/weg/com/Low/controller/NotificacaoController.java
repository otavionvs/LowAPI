package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.NotificacaoDTO;
import weg.com.Low.model.entity.Notificacao;
import weg.com.Low.model.service.NotificacaoService;
import weg.com.Low.model.service.UsuarioService;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/notificacao")
public class NotificacaoController {
    private NotificacaoService notificacaoService;
    private UsuarioService usuarioService;

    @GetMapping("/{codigoUsuario}")
    public ResponseEntity<List<Notificacao>> findAllByUsuario(@PathVariable(value = "codigoUsuario") Integer codigoUsuario) {
        List<Notificacao> notificacoes = notificacaoService.findAllByUsuario(codigoUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(notificacoes);
    }

//    @MessageMapping("/{codigo}")
//    @SendTo("/notificar/{codigo}")
//    public Notificacao save(NotificacaoDTO notificacaoDTO) {
//        Notificacao notificacao = new Notificacao();
//        BeanUtils.copyProperties(notificacaoDTO, notificacao);
//        return notificacaoService.save(notificacao);
//    }
}
