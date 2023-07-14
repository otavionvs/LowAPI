package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.UsuarioDTO;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.UsuarioService;
import weg.com.Low.security.TokenDTO;
import weg.com.Low.security.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/usuario")
public class UsuarioController {
    private UsuarioService usuarioService;
    private DemandaService demandaService;

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Usuario> usuarioOptional = usuarioService.findById(codigo);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        if (usuarioService.existsByEmailUsuario(usuarioDTO.getEmailUsuario())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este E-mail já está cadastrado!");
        }
        if (usuarioService.existsByUserUsuario(usuarioDTO.getUserUsuario())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este Usuario já existe, Tente Outro!");
        }
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario.setSenhaUsuario(encoder.encode(usuario.getSenhaUsuario()));
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuario));
    }

    //exclui ou adiciona
    @PutMapping("/demanda-favorita/{codigo}")
    public ResponseEntity<Object> save(@PathVariable(value = "codigo") Integer codigoDemanda, HttpServletRequest httpServletRequest) {
        System.out.println(codigoDemanda);
//        if (demandaService.existsById(codigoDemanda)) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Código de demanda não existe!");
//        }
        Demanda demanda = demandaService.findLastDemandaById(codigoDemanda).get();
        if (demanda == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Código de demanda não existe!");
        }

        TokenUtils tokenUtils = new TokenUtils();
        String username = tokenUtils.getUsuarioUsernameByRequest(httpServletRequest);
        Usuario usuario = usuarioService.findByUserUsuario(username).get();
        boolean removerFavoritos = false;
        if (!usuario.getDemandasFavoritas().isEmpty()) {
            for (Demanda demanda1 : usuario.getDemandasFavoritas()) {
                if (demanda1.getCodigoDemanda() == demanda.getCodigoDemanda()) {
                    System.out.println("TOREMOVE");
                    removerFavoritos = true;
                }
            }
        }
        if (removerFavoritos) {
            usuario.getDemandasFavoritas().removeIf(demanda1 -> demanda1.getCodigoDemanda() == demanda.getCodigoDemanda());
            System.out.println("ATREMOVE");
        } else {
            usuario.getDemandasFavoritas().add(demanda);

        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuario));
    }


    @GetMapping("/demanda-favorita")
    public ResponseEntity<Object> get(HttpServletRequest httpServletRequest) {
        TokenUtils tokenUtils = new TokenUtils();
        String username = tokenUtils.getUsuarioUsernameByRequest(httpServletRequest);
        Usuario usuario = usuarioService.findByUserUsuario(username).get();

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findByUsuariosFavoritos(usuario));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Usuario>> search(
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("usuario") String usuario,
            @RequestParam("departamento") String departamento,
            @PageableDefault(sort = "user_usuario",
                    direction = Sort.Direction.ASC,
                    page = 0,
                    size = 10) Pageable page) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.search(nome, email, usuario, departamento, page));
    }


}
