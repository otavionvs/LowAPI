package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Departamento;
import weg.com.Low.repository.DepartamentoRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartamentoService {
    private DepartamentoRepository departamentoRepository;

    public List<Departamento> findAll() {
        return departamentoRepository.findAll();
    }

    public Departamento save(Departamento entity) {
        return departamentoRepository.save(entity);
    }

    public Optional<Departamento> findById(Integer codigo) {
        return departamentoRepository.findById(codigo);
    }
}
