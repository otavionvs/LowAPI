package weg.com.Low.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weg.com.Low.model.service.ProcessorService;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequestMapping(path = "/api")
public class ProcessorController {
    private ProcessorService service;
    @Autowired
    public ProcessorController(ProcessorService service){
        this.service = service;
    }

    @PutMapping
    public ResponseEntity<String> execute(){
        service.execute();
        return ResponseEntity.ok().body(new String(LocalDateTime.now().toString()));
    }
}
