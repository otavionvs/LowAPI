package weg.com.Low.model.service;

import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Comissao;
import weg.com.Low.repository.ComissaoRepository;

import java.util.List;
@Service
public class ComissaoService {
    private ComissaoRepository comissaoRepository;

    public void deleteById(Integer integer) {
        comissaoRepository.deleteById(integer);
    }

    public List<Comissao> findAll() {
        return comissaoRepository.findAll();
    }

    public <S extends Comissao> S save(S entity) {
        return comissaoRepository.save(entity);
    }

    public boolean existsById(Integer integer) {
        return comissaoRepository.existsById(integer);
    }

    public void delete(Comissao entity) {
        comissaoRepository.delete(entity);
    }

    public boolean existsByNome(String nome) {
        return comissaoRepository.existsByNome(nome);
    }
}
