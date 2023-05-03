package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.repository.PropostaRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PropostaService {
    private PropostaRepository propostaRepository;
    private NotificacaoService notificacaoService;
    private DemandaClassificadaService demandaClassificadaService;
    private DemandaService demandaService;

    public List<Proposta> findAll() {
        return propostaRepository.findAll();
    }

    public Optional<Proposta> findById(Integer integer) {
        return propostaRepository.findById(integer);
    }

    public Proposta save(Proposta proposta) {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(proposta.getSolicitanteDemanda());
        usuarios.add(proposta.getGerenteNegocio());
        usuarios.add(proposta.getAnalista());
        notificacaoService.save(new Notificacao(null, proposta.getTituloDemanda(), TipoNotificacao.AVANCOU_STATUS_DEMANDA,
                        "A Demanda avançou um status!", new Date(), false, usuarios));
        return propostaRepository.save(proposta);
    }

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
