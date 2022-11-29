package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.UsuarioDTO;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.service.UsuarioService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    private UsuarioService usuarioService;

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
        if(usuarioService.existsByUserUsuario(usuarioDTO.getUserUsuario())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este Usuario já existe, Tente Outro!");
        }
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);
//        usuario.setNivelAcesso(nivelAcesso.);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuario));
    }

}
