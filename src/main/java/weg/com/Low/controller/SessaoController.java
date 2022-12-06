package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.SessaoDTO;
import weg.com.Low.dto.UsuarioDTO;
import weg.com.Low.model.entity.Sessao;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.service.SessaoService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/sessao")
public class SessaoController {
    private SessaoService sessaoService;

    @GetMapping
    public ResponseEntity<List<Sessao>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(sessaoService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Sessao> sessaoOptional = sessaoService.findById(codigo);
        if (sessaoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sessão não encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(sessaoOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid SessaoDTO sessaoDTO) {
        Sessao sessao = new Sessao();
        BeanUtils.copyProperties(sessaoDTO, sessao);
        return ResponseEntity.status(HttpStatus.OK).body(sessaoService.save(sessao));
    }
}
