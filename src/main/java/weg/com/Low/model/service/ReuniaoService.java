package weg.com.Low.model.service;

import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Reuniao;
import weg.com.Low.repository.ReuniaoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReuniaoService {
    private ReuniaoRepository reuniaoRepository;

    public List<Reuniao> findAll() {
        return reuniaoRepository.findAll();
    }

    public Reuniao save(Reuniao entity) {
        return reuniaoRepository.save(entity);
    }

    public Optional<Reuniao> findById(Integer codigo) {
        return reuniaoRepository.findById(codigo);
    }

    public boolean existsById(Integer codigo) {
        return reuniaoRepository.existsById(codigo);
    }

    public void deleteById(Integer codigo) {
        reuniaoRepository.deleteById(codigo);
    }
}