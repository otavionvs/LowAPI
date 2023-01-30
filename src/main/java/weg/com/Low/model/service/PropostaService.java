package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.DemandaAnalista;
import weg.com.Low.model.entity.Proposta;
import weg.com.Low.repository.PropostaRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PropostaService {
    private PropostaRepository propostaRepository;

    public List<Proposta> findAll() {
        return propostaRepository.findAll();
    }

    public Optional<Proposta> findById(Integer integer) {
        return propostaRepository.findById(integer);
    }

    public Proposta save(Proposta entity) {
        return propostaRepository.save(entity);
    }

    public void deleteById(Integer codigo) {
        propostaRepository.deleteById(codigo);
    }

    public boolean existsById(Integer integer) {
        return propostaRepository.existsById(integer);
    }

    public Proposta findByDemandaAnalistaProposta(DemandaAnalista demandaAnalista) {
        return propostaRepository.findByDemandaAnalistaProposta(demandaAnalista);
    }
}
