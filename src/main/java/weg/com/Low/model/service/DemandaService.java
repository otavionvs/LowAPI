package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Notificacao;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.repository.DemandaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DemandaService {
    private DemandaRepository demandaRepository;
    private NotificacaoService notificacaoService;


    public Optional<Demanda> findFirstByCodigoDemandaAndVersionBefore(Integer codigoDemanda, Integer version) {
        return demandaRepository.findFirstByCodigoDemandaAndVersionBefore(codigoDemanda, version);
    }

    public Optional<Demanda> findFirstByCodigoDemandaAndVersion(Integer codigoDemanda, Integer version) {
        return demandaRepository.findFirstByCodigoDemandaAndVersion(codigoDemanda, version);
    }

    public List<Demanda> findBySolicitanteDemandaOrAnalista(Usuario solicitanteDemanda) {
        return demandaRepository.findBySolicitanteDemandaOrAnalista(solicitanteDemanda, solicitanteDemanda);
    }

    public List<Demanda> findAll() {
        return demandaRepository.findAll();
    }

    public Demanda save(Demanda demanda, TipoNotificacao tipoNotificacao) {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(demanda.getSolicitanteDemanda());
        switch (tipoNotificacao) {
            case CRIOU_DEMANDA -> {
                notificacaoService.save(new Notificacao(null, "Demanda Criada!", tipoNotificacao,
                        "Demanda: " + demanda.getTituloDemanda() + ", criada com sucesso! Logo entrará" +
                                " em processo de aprovação, fique atento a futuras mudanças.",
                        LocalDateTime.now(), false, usuarios));
            }
            case EDITOU_DEMANDA -> {
                if (demanda.getAnalista() != null) {
                    usuarios.add(demanda.getAnalista());
                }
                notificacaoService.save(new Notificacao(null, "Demanda Editada!", tipoNotificacao,
                        "Demanda: " + demanda.getTituloDemanda() +", foi editada!", LocalDateTime.now(), false, usuarios));
            }
            case AVANCOU_STATUS_DEMANDA -> {
                if (demanda.getGerenteNegocio() != null) {
                    usuarios.add(demanda.getGerenteNegocio());
                }
                usuarios.add(demanda.getAnalista());
                notificacaoService.save(new Notificacao(null, "Status Avançado!", tipoNotificacao,
                        "Demanda: " + demanda.getTituloDemanda() + ", avançou um status! O status atual é: " + demanda.getStatusDemanda().getStatus(), LocalDateTime.now(), false, usuarios));
            }
            case CANCELOU_DEMANDA -> {
                if (demanda.getAnalista() != null) {
                    usuarios.add(demanda.getAnalista());
                }
                notificacaoService.save(new Notificacao(null, "Demanda Cancelada!", tipoNotificacao,
                        "Demanda: " + demanda.getTituloDemanda() + ", foi cancelada!", LocalDateTime.now(), false, usuarios));
            }
        }
        return demandaRepository.save(demanda);
    }


    public List<Demanda> findByCodigoDemanda(Integer codigo) {
        return demandaRepository.findByCodigoDemanda(codigo);
    }

    public Optional<Demanda> findLastDemandaById(Integer codigo) {
        return demandaRepository.findFirstByCodigoDemandaOrderByVersionDesc(codigo);
    }

    public boolean existsById(Integer codigo) {
        return demandaRepository.existsByCodigoDemanda(codigo);
    }

    public void deleteById(Integer codigo) {
        demandaRepository.deleteFirstByCodigoDemandaOrderByVersionDesc(codigo);
    }

    public Page<Demanda> search(
            String tituloDemanda, String solicitante, String codigoDemanda, String status,
            String tamanho, String analista, String departamento, String ordenar, Pageable page) {
        return demandaRepository.search(tituloDemanda.toLowerCase(), solicitante.toLowerCase(), codigoDemanda,
                status, tamanho, analista, departamento, ordenar, page);
    }

    public Page<Demanda> search(
            String tituloDemanda, String solicitante, String codigoDemanda, String status, String departamento, String ordenar, Pageable page) {
        //çflaskdjfçlkajsdf
        return demandaRepository.search(tituloDemanda.toLowerCase(), solicitante.toLowerCase(), codigoDemanda, status, departamento, ordenar, page);
    }

    public List<Demanda> search(String status, Pageable page) {
        return demandaRepository.search(status, page);
    }

    public List<Demanda> search(String status1, String status2, Pageable page) {
        return demandaRepository.search(status1, status2, page);
    }

    public Integer countDemanda(String status) {
        return demandaRepository.countDemanda(status);
    }

    public Integer countByDepartamento(String status, Integer codigoDepartamento) {
        return demandaRepository.countByDepartamento(status, codigoDepartamento);
    }

    public List<Demanda> search(String status, Integer codigoDepartamento, Pageable page) {
        return demandaRepository.search(status, codigoDepartamento, page);
    }

    public Long countAllByCodigoDemanda(Integer codigoDemanda) {
        return demandaRepository.countAllByCodigoDemanda(codigoDemanda);
    }

    public Integer countByVersion() {
        return demandaRepository.countByVersionIs(0);
    }
}
