package weg.com.Low.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.security.service.JpaService;
import weg.com.Low.security.users.UserJpa;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/low")
public class AutenticacaoController {

    private TokenUtils tokenUtils = new TokenUtils();
    @Autowired
    private JpaService jpaService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login/verify-token")
    public ResponseEntity<Object> autenticacao(HttpServletRequest request) {
        Boolean valido = false;
        UserDetails usuario = null;
        try{
            String token = tokenUtils.buscarCookie(request);
            valido = tokenUtils.validarToken(token);
            if(valido){
                String usuarioUsername = tokenUtils.getUsuarioUsername(token);
                usuario = jpaService.loadUserByUsername(usuarioUsername);
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(valido);
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Lógica de logout, como invalidar a sessão e remover o cookie de autenticação
        request.getSession().invalidate();
        Cookie cookie = new Cookie("jwt", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        // Redirecionar para a página de login ou retornar uma resposta personalizada
        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    @PostMapping("/login/auth")
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