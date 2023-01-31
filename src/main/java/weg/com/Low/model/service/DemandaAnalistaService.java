package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.DemandaAnalista;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.repository.DemandaAnalistaRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DemandaAnalistaService {
    private DemandaAnalistaRepository demandaAnalistaRepository;

    public List<DemandaAnalista> findAll() {
        return demandaAnalistaRepository.findAll();
    }

    public DemandaAnalista save(DemandaAnalista entity) {
        return demandaAnalistaRepository.save(entity);
    }

    public Optional<DemandaAnalista> findById(Integer codigo) {
        return demandaAnalistaRepository.findById(codigo);
    }

    public boolean existsById(Integer codigo) {
        return demandaAnalistaRepository.existsById(codigo);
    }

    public void deleteById(Integer codigo) {
        demandaAnalistaRepository.deleteById(codigo);
    }

    public List<DemandaAnalista> findByAnalista(Usuario analista) {
        return demandaAnalistaRepository.findByAnalista(analista);
    }

}
