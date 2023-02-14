package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.DemandaAnalista;
import weg.com.Low.model.entity.Notificacao;
import weg.com.Low.model.entity.Proposta;
import weg.com.Low.model.entity.Usuario;
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

    public List<Proposta> findAll() {
        return propostaRepository.findAll();
    }

    public Optional<Proposta> findById(Integer integer) {
        return propostaRepository.findById(integer);
    }

//    public Proposta save(Proposta proposta) {
//        notificacaoService.save(new Notificacao(
//                proposta.getDemandaAnalistaProposta().getDemandaDemandaAnalista().getTituloDemanda(),
//                proposta.getDemandaAnalistaProposta().getDemandaDemandaAnalista().getCodigoDemanda(),
//                TipoNotificacao.AVANCOU_STATUS_DEMANDA,
//                "Sua demanda objete um progresso!",
//                LocalDateTime.now(),
//                LocalDate.now(),
//                StatusNotificacao.ATIVADA,
//                proposta.getRecursosProposta()
//        ));

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
