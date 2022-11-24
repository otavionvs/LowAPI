package weg.com.Low.controller;

import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import weg.com.Low.dto.PropostaDTO;
import weg.com.Low.model.entity.Proposta;
import weg.com.Low.model.service.PropostaService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("low-api/proposta")
public class PropostaController {
    private PropostaService propostaService;

    @GetMapping
    public List<Proposta> findAll() {
        return propostaService.findAll();
    }


    @PostMapping(
    )
    public ResponseEntity<Object> save(
        @RequestBody @Valid PropostaDTO propostaDTO
    ) {

        Proposta proposta = new Proposta();
        BeanUtils.copyProperties(propostaDTO,proposta);

        return ResponseEntity.status(HttpStatus.OK)
                .body(propostaService.save(proposta));
    }

    public void deleteById(Integer integer) {
        propostaService.deleteById(integer);
    }

    public <S extends Proposta> boolean exists(Example<S> example) {
        return propostaService.exists(example);
    }
}
