package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.StatusNotificacao;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.repository.PropostaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PropostaService {
    private PropostaRepository propostaRepository;
    private NotificacaoService notificacaoService;
    private DemandaAnalistaService demandaAnalistaService;
    private DemandaService demandaService;

    public List<Proposta> findAll() {
        return propostaRepository.findAll();
    }

    public Optional<Proposta> findById(Integer integer) {
        return propostaRepository.findById(integer);
    }

    public Proposta save(Proposta proposta) {
        return propostaRepository.save(proposta);
    }

    //    public Proposta save(Proposta proposta) {
//        List<Usuario> usuarios = null;
//        DemandaAnalista demandaAnalista = demandaAnalistaService.findById(proposta.getCodigoDemanda()).get();
//        usuarios.add(demandaAnalista.getAnalista());
//        usuarios.add(demandaAnalista.getGerenteNegocio());
//        Demanda demanda = demandaService.findLastDemandaById(demandaAnalista.getCodigoDemanda()).get();
//        usuarios.add(demanda.getSolicitanteDemanda());
//
//        notificacaoService.save(new Notificacao(
//                null,
//                demanda.getTituloDemanda(),
//                        demanda.getCodigoDemanda(),
//                TipoNotificacao.AVANCOU_STATUS_DEMANDA,
//                "Sua demanda progrediu de estado!",
//                LocalDateTime.now(),
//                LocalDate.now(),
//                StatusNotificacao.ATIVADA,
//                usuarios
//        ));
//
//        return propostaRepository.save(proposta);
//    }

    public void deleteById(Integer codigo) {
        propostaRepository.deleteById(codigo);
    }

    public boolean existsById(Integer integer) {
        return propostaRepository.existsById(integer);
    }

    public Proposta porDemanda(String codigoDemanda) {
        return propostaRepository.porDemanda(codigoDemanda);
    }
}
