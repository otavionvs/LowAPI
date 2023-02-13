package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.DemandaAnalista;
import weg.com.Low.model.entity.Notificacao;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.enums.StatusNotificacao;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.repository.DemandaAnalistaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DemandaAnalistaService {
    private DemandaAnalistaRepository demandaAnalistaRepository;
    private NotificacaoService notificacaoService;

    public List<DemandaAnalista> findAll() {
        return demandaAnalistaRepository.findAll();
    }

    public DemandaAnalista save(DemandaAnalista entity) {
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(entity.getAnalista());
        usuarios.add(entity.getGerenteNegocio());
        usuarios.add(entity.getDemandaDemandaAnalista().getSolicitanteDemanda());

        notificacaoService.save(new Notificacao(null,
                entity.getDemandaDemandaAnalista().getTituloDemanda(),
                entity.getDemandaDemandaAnalista().getCodigoDemanda(),
                TipoNotificacao.AVANCOU_STATUS_DEMANDA,
                "Sua demanda foi classificada!",
                LocalDateTime.now(),
                LocalDate.now(),
                StatusNotificacao.ATIVADA, usuarios));
        return demandaAnalistaRepository.save(entity);
    }

    public Optional<DemandaAnalista> findById(Integer codigo) {
        return demandaAnalistaRepository.findById(codigo);
    }

    public boolean existsById(Integer codigo) {
        return demandaAnalistaRepository.existsById(codigo);
    }

    public void deleteById(Integer codigo) {
        demandaAnalistaRepository.deleteById(codigo);
    }

    public List<DemandaAnalista> findByAnalista(Usuario analista) {
        return demandaAnalistaRepository.findByAnalista(analista);
    }

    public DemandaAnalista findByDemandaDemandaAnalista(Demanda demanda) {
        return demandaAnalistaRepository.findByDemandaDemandaAnalista(demanda);
    }

}
