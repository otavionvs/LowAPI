package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.DemandaClassificada;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.repository.DemandaClassificadaRepository;

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

    public DemandaClassificada save(DemandaClassificada entity) {
        return demandaClassificadaRepository.save(entity);
    }

    //    public DemandaAnalista save(DemandaAnalista entity) {
//        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
//        usuarios.add(entity.getAnalista());
//        usuarios.add(entity.getGerenteNegocio());
//        usuarios.add(entity.getSolicitanteDemanda());
//
//        notificacaoService.save(new Notificacao(null,
//                entity.getTituloDemanda(),
//                entity.getCodigoDemanda(),
//                TipoNotificacao.AVANCOU_STATUS_DEMANDA,
//                "Sua demanda foi classificada!",
//                LocalDateTime.now(),
//                LocalDate.now(),
//                StatusNotificacao.ATIVADA, usuarios));
//
//        return demandaAnalistaRepository.save(entity);
//    }

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
