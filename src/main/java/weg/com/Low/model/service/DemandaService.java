package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.repository.DemandaRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DemandaService {
    private DemandaRepository demandaRepository;

    public List<Demanda> findAll() {
        return demandaRepository.findAll();
    }

    public Demanda save(Demanda entity) {
        return demandaRepository.save(entity);
    }

    public Optional<Demanda> findById(Integer codigo) {
        return demandaRepository.findById(codigo);
    }

    public boolean existsById(Integer codigo) {
        return demandaRepository.existsById(codigo);
    }

    public void deleteById(Integer codigo) {
        demandaRepository.deleteById(codigo);
    }

}
