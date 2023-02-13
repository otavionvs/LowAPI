package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import weg.com.Low.dto.DepartamentoDTO;
import weg.com.Low.model.entity.Departamento;
import weg.com.Low.model.service.DepartamentoService;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/departamento")
public class DepartamentoController {
    private DepartamentoService departamentoService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;


    @GetMapping
    public ResponseEntity<List<Departamento>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(departamentoService.findAll());
    }



    @MessageMapping("/application")
    @SendTo("/all/messages")
    public Message send(final Message message) throws Exception {
        return message;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Departamento> departamentoOptional = departamentoService.findById(codigo);
        if (departamentoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Departamento não encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(departamentoOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DepartamentoDTO departamentoDTO) {
        Departamento departamento = new Departamento();
        BeanUtils.copyProperties(departamentoDTO, departamento);
        return ResponseEntity.status(HttpStatus.OK).body(departamentoService.save(departamento));
    }
}
