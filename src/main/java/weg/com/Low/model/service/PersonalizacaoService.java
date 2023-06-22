package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Personalizacao;
import weg.com.Low.repository.PersonalizacaoRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonalizacaoService {
    private PersonalizacaoRepository personalizacaoRepository;

    public List<Personalizacao> findAll() {
        return personalizacaoRepository.findAll();
    }

    public Personalizacao findAtiva(){
        return personalizacaoRepository.findByAtivaPersonalizacao(true);
    }

    public Personalizacao save(Personalizacao entity) {
        return personalizacaoRepository.save(entity);
    }

    public List<Personalizacao> saveAll(List<Personalizacao> entities) {
        return personalizacaoRepository.saveAll(entities);
    }

    public Optional<Personalizacao> findById(Integer integer) {
        return personalizacaoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return personalizacaoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        personalizacaoRepository.deleteById(integer);
    }
}
