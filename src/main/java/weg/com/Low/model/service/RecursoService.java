package weg.com.Low.model.service;

import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Recurso;
import weg.com.Low.repository.CentroCustoRepository;
import weg.com.Low.repository.RecursoRepository;

import java.util.List;

@Service
public class RecursoService {

    private RecursoRepository recursoRepository;

    public List<Recurso> findAll() {
        return recursoRepository.findAll();
    }

    public <S extends Recurso> S save(S entity) {
        return recursoRepository.save(entity);
    }

    public void deleteById(Integer integer) {
        recursoRepository.deleteById(integer);
    }
}
