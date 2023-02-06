package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Status;
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

    public List<Demanda> search(
            String tituloDemanda, String solicitante, String codigoDemanda, String status, String tamanho, Long page, int size, String sort) {
        return demandaRepository.search(tituloDemanda.toLowerCase(), solicitante.toLowerCase(), codigoDemanda, status, tamanho, page, size, sort);
    }

    public List<Demanda> search(
            String tituloDemanda, String solicitante, String codigoDemanda, String status, Long page, int size, String sort) {
        return demandaRepository.search(tituloDemanda.toLowerCase(), solicitante.toLowerCase(), codigoDemanda, status, page, size, sort);
    }

    public List<Demanda> search(String status, Long page, int size) {
        return demandaRepository.search(status, page, size);
    }

}
