package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Not;
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
import weg.com.Low.security.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/notificacao")
public class NotificacaoController {
    private NotificacaoService notificacaoService;
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Notificacao>> findAllByUsuarioRequest(HttpServletRequest request) {
        List<Notificacao> notificacoes = notificacaoService.findByUsuario
                (usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get());
        for(Notificacao notificacao: notificacoes){
            if(notificacao.getLido() == false){
                notificacao.setLido(true);
                notificacaoService.save(notificacao);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(notificacoes);
    }

    @MessageMapping("/ws")
    @SendTo("/usuario")
    public ResponseEntity<List<Notificacao>> findAllByUsuario(HttpServletRequest request) {
        List<Notificacao> notificacoes = notificacaoService.findByUsuario
                (usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get());
        for(Notificacao notificacao: notificacoes){
            if(notificacao.getLido() == false){
                notificacao.setLido(true);
                notificacaoService.save(notificacao);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(notificacoes);
    }

    @GetMapping("/quantidade")
    public ResponseEntity<Integer> findAllByUsuarioCountRequest(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(notificacaoService.countByUsuarioNotificacaoAndLidoFalse
                (usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get()));
    }

    @MessageMapping("/ws/quantidade")
    @SendTo("/usuario/quantidade")
    public ResponseEntity<Integer> findAllByUsuarioCount(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(notificacaoService.countByUsuarioNotificacaoAndLidoFalse
                (usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get()));
    }

}
