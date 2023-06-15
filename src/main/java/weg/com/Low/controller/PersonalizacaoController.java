package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.PersonalizacaoDTO;
import weg.com.Low.model.entity.Personalizacao;
import weg.com.Low.model.service.PersonalizacaoService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/personalizacao")
public class PersonalizacaoController {
    private PersonalizacaoService personalizacaoService;
    @GetMapping("/ativa")
    public ResponseEntity<Object> findPersonalizacao(){
        return ResponseEntity.status(200).body(personalizacaoService.findAtiva());
    }
    @GetMapping
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.status(200).body(personalizacaoService.findAll()  );
    }

    @PostMapping
    public ResponseEntity<Object> savePersonalizacao(@RequestBody PersonalizacaoDTO personalizacaoDTO){
        ModelMapper modelMapper = new ModelMapper();
        if(personalizacaoDTO.getCoresPrimariasPersonalizacao().size() < 11 ||
                personalizacaoDTO.getCoresSecundariasPersonalizacao().size() < 11 ){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Você deve repassar cores todos os status");
        }
        Personalizacao personalizacao = modelMapper.map(personalizacaoDTO, Personalizacao.class);
        personalizacao.setAtivaPersonalizacao(false);
        personalizacao = personalizacaoService.save(personalizacao);
        return ResponseEntity.status(200).body(personalizacao);
    }

    @PutMapping("/ativa/{codigo}")
    public ResponseEntity<Object> savePersonalizacao(@PathVariable Integer codigo){
        List<Personalizacao> personalizacoes = personalizacaoService.findAll();
        for(Personalizacao personalizacao: personalizacoes){
            if(personalizacao.isAtivaPersonalizacao()){
                personalizacao.setAtivaPersonalizacao(false);
            }

            if(personalizacao.getCodigoPersonalizacao() == codigo){
                personalizacao.setAtivaPersonalizacao(true);
            }
        }
        personalizacoes = personalizacaoService.saveAll(personalizacoes);
        return ResponseEntity.status(200).body(personalizacoes);
    }
    @DeleteMapping("/{codigo}")
    public void deletePersonalizacao(@PathVariable(value = "codigo") Integer codigo){
        if(personalizacaoService.existsById(codigo)){
            personalizacaoService.deleteById(codigo);
        }
    }
}
