package weg.com.Low.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.security.service.JpaService;
import weg.com.Low.security.users.UserJpa;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class AutenticacaoController {

    private TokenUtils tokenUtils = new TokenUtils();
    @Autowired
    private JpaService jpaService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<Object> autenticacao(
            @RequestBody @Valid UsuarioLoginDTO usuarioDTO,
            HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        usuarioDTO.getUsuarioUsuario(),usuarioDTO.getSenhaUsuario());

        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        if (authentication.isAuthenticated()) {
            response.addCookie(tokenUtils.gerarCookie(authentication));
            UserJpa userJpa = (UserJpa) authentication.getPrincipal();
            return ResponseEntity.ok(userJpa.getUsuario());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}