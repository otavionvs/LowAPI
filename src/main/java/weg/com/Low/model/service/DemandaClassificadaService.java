package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.DemandaClassificada;
import weg.com.Low.model.entity.Notificacao;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.repository.DemandaClassificadaRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DemandaClassificadaService {
    private DemandaClassificadaRepository demandaClassificadaRepository;
    private NotificacaoService notificacaoService;

    public List<DemandaClassificada> findAll() {
        return demandaClassificadaRepository.findAll();
    }

    public List<Demanda> findBySolicitanteDemandaOrAnalista(Usuario analista) {
        return demandaClassificadaRepository.findBySolicitanteDemandaOrAnalista(analista, analista);
    }

    public DemandaClassificada save(DemandaClassificada demanda) {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(demanda.getSolicitanteDemanda());
        usuarios.add(demanda.getAnalista());
        notificacaoService.save(new Notificacao(null, demanda.getTituloDemanda(), TipoNotificacao.AVANCOU_STATUS_DEMANDA,
                "A Demanda avançou um status!", new Date(), false, usuarios));
        return demandaClassificadaRepository.save(demanda);
    }

    public Optional<DemandaClassificada> findById(Integer codigo) {
        return demandaClassificadaRepository.findById(codigo);
    }

    public boolean existsById(Integer codigo) {
        return demandaClassificadaRepository.existsById(codigo);
    }

    public void deleteById(Integer codigo) {
        demandaClassificadaRepository.deleteById(codigo);
    }

//    public List<DemandaAnalista> findByAnalista(Usuario analista) {
//        return demandaAnalistaRepository.findByAnalista(analista);
//    }
//
//    public DemandaAnalista findByDemandaDemandaAnalista(Demanda demanda) {
//        return demandaAnalistaRepository.findByDemandaDemandaAnalista(demanda);
//    }

}
