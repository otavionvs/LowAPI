package weg.com.Low.model.service;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Proposta;
import weg.com.Low.repository.PropostaRepository;

import java.util.List;

@Service
public class PropostaService {
    private PropostaRepository propostaRepository;

    public List<Proposta> findAll() {
        return propostaRepository.findAll();
    }

    public <S extends Proposta> S save(S entity) {
        return propostaRepository.save(entity);
    }

    public void deleteById(Integer integer) {
        propostaRepository.deleteById(integer);
    }

    public <S extends Proposta> boolean exists(Example<S> example) {
        return propostaRepository.exists(example);
    }
}
