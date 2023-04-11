package weg.com.Low.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import weg.com.Low.security.service.JpaService;

import java.util.List;

@Configuration
@AllArgsConstructor
public class AutenticacaoConfig {

    private JpaService jpaService;


    // Configura as autorizações de acesso

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jpaService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }


    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration =
                new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:4200"
        ));
        corsConfiguration.setAllowedMethods(List.of(
                "POST", "DELETE", "GET", "PUT"
        ));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                // Libera o acesso sem autenticação para /login
                .antMatchers( "/login/**",
                        "/logout",
                        "/usuario",
                        "/departamento",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**").permitAll()
                .antMatchers(HttpMethod.GET, "/demanda").hasAnyAuthority("Solicitante", "Analista", "GerenteNegocio", "GestorTI")
                // Determina que todas as demais requisições terão de ser autenticadas
                .anyRequest().authenticated()
                .and().csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and().formLogin().permitAll()
                .and().logout().permitAll()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticacaoFiltro(new TokenUtils(), jpaService), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
    //Serve pra poder fazer a injeção de dependência na controller
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration ac) throws Exception {
        return ac.getAuthenticationManager();
    }
}
