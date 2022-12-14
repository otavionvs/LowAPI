package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.CentroCustoRecurso;
import weg.com.Low.repository.CentroCustoRecursoRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CentroCustoRecursoService {
    private CentroCustoRecursoRepository centroCustoRecursoRepository;

    public List<CentroCustoRecurso> findAll() {
        return centroCustoRecursoRepository.findAll();
    }

    public CentroCustoRecurso save(CentroCustoRecurso entity) {
        return centroCustoRecursoRepository.save(entity);
    }

    public Optional<CentroCustoRecurso> findById(Integer codigo) {
        return centroCustoRecursoRepository.findById(codigo);
    }
}
