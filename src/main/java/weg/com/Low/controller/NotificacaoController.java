package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.model.entity.Notificacao;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.service.NotificacaoService;
import weg.com.Low.model.service.UsuarioService;

import java.util.List;


@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/notificacao")
public class NotificacaoController {
    NotificacaoService notificacaoService;
    UsuarioService usuarioService;

    @GetMapping("/{codigoUsuario}")
    public ResponseEntity<List<Notificacao>> findAllByUsuario(@PathVariable(value = "codigoUsuario") Integer codigoUsuario) {
        List<Notificacao> notificacao = notificacaoService.findAllByUsuario(codigoUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(notificacao);
    }
}
