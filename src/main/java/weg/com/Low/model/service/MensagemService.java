package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Mensagem;
import weg.com.Low.repository.MensagemRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class MensagemService {
    private MensagemRepository mensagemRepository;




    public Mensagem save(Mensagem entity) {
        return mensagemRepository.save(entity);
    }
}
