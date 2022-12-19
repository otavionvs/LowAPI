package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Secao;
import weg.com.Low.repository.SecaoRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SecaoService {
    private SecaoRepository sessaoRepository;

    public List<Secao> findAll() {
        return sessaoRepository.findAll();
    }

    public Secao save(Secao entity) {
        return sessaoRepository.save(entity);
    }

    public Optional<Secao> findById(Integer codigo) {
        return sessaoRepository.findById(codigo);
    }
}
