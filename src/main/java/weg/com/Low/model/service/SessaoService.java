package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Sessao;
import weg.com.Low.repository.SessaoRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SessaoService {
    private SessaoRepository sessaoRepository;

    public List<Sessao> findAll() {
        return sessaoRepository.findAll();
    }

    public Sessao save(Sessao entity) {
        return sessaoRepository.save(entity);
    }

    public Optional<Sessao> findById(Integer codigo) {
        return sessaoRepository.findById(codigo);
    }
}
