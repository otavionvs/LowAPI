package weg.com.Low.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.coyote.Request;
import org.springframework.security.core.Authentication;
import org.springframework.web.util.WebUtils;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.service.UsuarioService;
import weg.com.Low.security.users.UserJpa;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class TokenUtils {
    private final String senhaForte = "c127a7b6adb013a5ff879ae71afa62afa4b4ceb72afaa54711dbcde67b6dc325";
    private UsuarioService usuarioService;
    public String gerarToken(Authentication authentication) {
        UserJpa userJpa = (UserJpa) authentication.getPrincipal();
        return Jwts.builder()
                .setIssuer("Low API")
                .setSubject(userJpa.getUsuario().getUserUsuario())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 1800000000))
                .signWith(SignatureAlgorithm.HS256, senhaForte)
                .compact();
    }


    public Boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Cookie gerarCookie(Authentication authentication){
        Cookie cookie = new Cookie("jwt", gerarToken(authentication));
        cookie.setPath("/");
        cookie.setMaxAge(5000);
        return cookie;
    }

    public String getUsuarioUsername(String token) {
        String usuario = Jwts.parser()
                .setSigningKey(senhaForte)
                .parseClaimsJws(token)
                .getBody().getSubject();

        return usuario;
    }

    public String getUsuarioUsernameByRequest(HttpServletRequest request){
        String token = buscarCookie(request);
        return getUsuarioUsername(token);
    }
    public String buscarCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request,"jwt");
        if(cookie != null){
            return cookie.getValue();
        }
        throw new RuntimeException("Cookie não encontrado");
    }
}
