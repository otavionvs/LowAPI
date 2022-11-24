package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.repository.BeneficioRepository;

import java.util.Optional;

@AllArgsConstructor
@Service
public class BeneficioService {
    private BeneficioRepository beneficioRepository;

    public BeneficioRepository save(BeneficioRepository beneficio) {
        return beneficioRepository.save(beneficio);
    }

    public Optional<BeneficioRepository> findById(Integer codigo) {
        return beneficioRepository.findById(codigo);
    }
}
