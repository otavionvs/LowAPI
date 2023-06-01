package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.repository.PropostaRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

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

    @Transactional
    public Proposta save(Proposta proposta, TipoNotificacao tipoNotificacao) {
        //Adiciona os usuarios que devem receber a notificação referente a ação
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(proposta.getSolicitanteDemanda());

        if (!usuarios.contains(proposta.getAnalista())) {
            usuarios.add(proposta.getAnalista());
        }

        if (!usuarios.contains(proposta.getGerenteNegocio())) {
            usuarios.add(proposta.getGerenteNegocio());
        }

        for (Usuario usuario : usuarios) {
            if (tipoNotificacao.equals(TipoNotificacao.AVANCOU_STATUS_DEMANDA)) {
                notificacaoService.save(new Notificacao(null, "Status Avançado!", TipoNotificacao.AVANCOU_STATUS_DEMANDA,
                        "Demanda: " + proposta.getTituloDemanda() + ", avançou um status! O status atual é: " + proposta.getStatusDemanda().getStatus(), LocalDateTime.now(), false, usuario));
            }
        }
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
