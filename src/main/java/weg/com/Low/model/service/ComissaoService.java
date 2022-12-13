package weg.com.Low.model.service;

import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Comissao;
import weg.com.Low.repository.ComissaoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ComissaoService {
    private ComissaoRepository comissaoRepository;

    public void deleteById(Integer codigo) {
        comissaoRepository.deleteById(codigo);
    }

    public List<Comissao> findAll() {
        return comissaoRepository.findAll();
    }

    public Comissao save(Comissao entity) {
        return comissaoRepository.save(entity);
    }

    public boolean existsById(Integer codigo) {
        return comissaoRepository.existsById(codigo);
    }

    public Optional<Comissao> findById(Integer codigo) {
        return comissaoRepository.findById(codigo);
    }

    public boolean existsByNome(String nome) {
        return comissaoRepository.existsByNome(nome);
    }
}
