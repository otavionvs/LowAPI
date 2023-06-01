package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Mensagens;
import weg.com.Low.repository.MensagensRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class MensagensService {
    private MensagensRepository mensagensRepository;


    public List<Mensagens> findAllByDemanda(Demanda demanda) {
        return mensagensRepository.findAllByDemandaMensagens(demanda);
    }

    public List<Mensagens> findAllByDemandaMensagens_CodigoDemanda(Integer codigoDemanda) {
        return mensagensRepository.findAllByDemandaMensagens_CodigoDemanda(codigoDemanda);
    }

    public Mensagens save(Mensagens entity) {
        return mensagensRepository.save(entity);
    }
}
