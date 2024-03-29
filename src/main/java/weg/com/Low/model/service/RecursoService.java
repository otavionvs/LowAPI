package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Recurso;
import weg.com.Low.repository.CentroCustoRepository;
import weg.com.Low.repository.RecursoRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RecursoService {

    private RecursoRepository recursoRepository;

    public List<Recurso> findAll() {
        return recursoRepository.findAll();
    }

    public Recurso save(Recurso entity) {
        return recursoRepository.save(entity);
    }

    public void deleteById(Integer codigo) {
        recursoRepository.deleteById(codigo);
    }
}
