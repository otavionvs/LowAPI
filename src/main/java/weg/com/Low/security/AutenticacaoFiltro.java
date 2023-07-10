package weg.com.Low.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import weg.com.Low.security.service.JpaService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class AutenticacaoFiltro extends OncePerRequestFilter {

    private TokenUtils tokenUtils;

    private JpaService jpaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().startsWith("/low/login") ||
        request.getRequestURI().equals("/low/logout") ||
                request.getRequestURI().equals("/low/personalizacao/ativa") ||
                request.getRequestURI().equals("/low/departamento") ||
                request.getRequestURI().equals("/low/usuario") ||
        request.getRequestURI().startsWith("/swagger-ui")||
                request.getRequestURI().startsWith("/v3/api-docs")){
            filterChain.doFilter(request,response);
            return;
        }
        String token = tokenUtils.buscarCookie(request);
        response.addCookie(tokenUtils.renovarCookie(request));
        Boolean valido = tokenUtils.validarToken(token);
        if (valido) {
            String usuarioUsername = tokenUtils.getUsuarioUsername(token);
            UserDetails usuario = jpaService.loadUserByUsername(usuarioUsername);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(usuario.getUsername(),
                            null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(
                    usernamePasswordAuthenticationToken
            );
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
