package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.CentroCusto;
import weg.com.Low.repository.CentroCustoRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CentroCustoService {
    private CentroCustoRepository centroCustoRepository;

    public <S extends CentroCusto> List<S> saveAll(Iterable<S> entities) {
        return centroCustoRepository.saveAll(entities);
    }

    public List<CentroCusto> findAll() {
        return centroCustoRepository.findAll();
    }

    public CentroCusto save(CentroCusto entity) {
        return centroCustoRepository.save(entity);
    }

    public boolean existsById(Integer integer) {
        return centroCustoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        centroCustoRepository.deleteById(integer);
    }

    public void delete(CentroCusto entity) {
        centroCustoRepository.delete(entity);
    }

    public boolean existsByNome(String nome) {
        return centroCustoRepository.existsByNomeCentroCusto(nome);
    }

    public boolean verificaPorcentagemCentroCusto(List<CentroCusto> listCentroCusto) {
        int somaPorcentagem = 0;
        for (CentroCusto centroCusto : listCentroCusto) {
            if (centroCusto.getPorcentagemCentroCusto() == null) {
                return false;
            }
            somaPorcentagem += centroCusto.getPorcentagemCentroCusto();

        }
        if (somaPorcentagem != 100) {
            return false;
        }
        return true;
    }
}
