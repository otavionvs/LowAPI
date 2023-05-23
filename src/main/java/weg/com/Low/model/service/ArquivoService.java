package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Arquivo;
import weg.com.Low.repository.ArquivoRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArquivoService {
    ArquivoRepository arquivoRepository;

    public <S extends Arquivo> List<S> findAll(Example<S> example) {
        return arquivoRepository.findAll(example);
    }

    public <S extends Arquivo> S save(S entity) {
        return arquivoRepository.save(entity);
    }

    public Optional<Arquivo> findById(Integer integer) {
        return arquivoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return arquivoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        arquivoRepository.deleteById(integer);
    }
}
