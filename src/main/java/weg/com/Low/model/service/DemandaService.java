package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.enums.Status;
import weg.com.Low.repository.DemandaRepository;

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

    public List<Demanda> findAll() {
        return demandaRepository.findAll();
    }

    public Demanda save(Demanda entity) {
        return demandaRepository.save(entity);
    }

    //    public Demanda save(Demanda demanda) {
//        switch (demanda.getStatusDemanda()) {
//            case BACKLOG_CLASSIFICACAO -> {
//                notificacaoService.save(new Notificacao(
//                        null,
//                        demanda.getTituloDemanda(),
//                        demanda.getCodigoDemanda(),
//                        TipoNotificacao.CRIOU_DEMANDA,
//                        "Sua demanda foi criada!",
//                        LocalDateTime.now(),
//                        LocalDate.now(),
//                        StatusNotificacao.ATIVADA,
//                        (List<Usuario>) demanda.getSolicitanteDemanda()));
//            }
//            case CANCELLED -> {
//                notificacaoService.save(new Notificacao(
//                        null,
//                        demanda.getTituloDemanda(),
//                        demanda.getCodigoDemanda(),
//                        TipoNotificacao.CANCELOU_DEMANDA,
//                        "Sua demanda foi cancelada!",
//                        LocalDateTime.now(),
//                        LocalDate.now(),
//                        StatusNotificacao.ATIVADA,
//                        (List<Usuario>) demanda.getSolicitanteDemanda()));
//            }
//            default -> {
//                notificacaoService.save(new Notificacao(
//                        null,
//                        demanda.getTituloDemanda(),
//                        demanda.getCodigoDemanda(),
//                        TipoNotificacao.AVANCOU_STATUS_DEMANDA,
//                        "Sua demanda progrediu de status!",
//                        LocalDateTime.now(),
//                        LocalDate.now(),
//                        StatusNotificacao.ATIVADA,
//                        (List<Usuario>) demanda.getSolicitanteDemanda()));
//
//        }
//        }
//        return demandaRepository.save(demanda);
//    }

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


    public List<Demanda> search(
            String tituloDemanda, String solicitante, String codigoDemanda, String status,
            String tamanho, String analista, String departamento, String ordenar, Pageable page) {
        return demandaRepository.search(tituloDemanda.toLowerCase(), solicitante.toLowerCase(), codigoDemanda,
                status, tamanho, analista, departamento, ordenar, page);
    }

    public List<Demanda> search(
            String tituloDemanda, String solicitante, String codigoDemanda, String status, String departamento, String ordenar, Pageable page) {
        return demandaRepository.search(tituloDemanda.toLowerCase(), solicitante.toLowerCase(), codigoDemanda, status, departamento, ordenar, page);
    }

    public List<Demanda> search(String status, Pageable page) {
        return demandaRepository.search(status, page);
    }

    public List<Demanda> search(String status1, String status2, Pageable page) {
        return demandaRepository.search(status1, status2, page);
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
