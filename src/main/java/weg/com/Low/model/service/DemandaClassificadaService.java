package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.DemandaClassificada;
import weg.com.Low.model.entity.Notificacao;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.enums.TamanhoDemanda;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.repository.DemandaClassificadaRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class DemandaClassificadaService {
    private DemandaClassificadaRepository demandaClassificadaRepository;
    private NotificacaoService notificacaoService;

    public List<DemandaClassificada> findAll() {
        return demandaClassificadaRepository.findAll();
    }


    public DemandaClassificada save(DemandaClassificada demanda) {
        //Adiciona os usuarios que devem receber a notificação referente a ação
        List<Usuario> usuarios = new ArrayList<>(Arrays.asList(demanda.getSolicitanteDemanda(), demanda.getAnalista()));
        for(Usuario usuario: usuarios) {
            notificacaoService.save(new Notificacao(null, "Status Avançado!", TipoNotificacao.AVANCOU_STATUS_DEMANDA,
                    "Demanda: " + demanda.getTituloDemanda() + ", avançou um status! O status atual é: " + demanda.getStatusDemanda().getStatus(), LocalDateTime.now(), false, usuario));
        }
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

    @Scheduled(fixedDelay = 43200000) // Executar a cada 12 horas
    @Transactional
    public void atualizarScoreDemanda() {
        // Obtem todas as demandas que precisam ter o Score atualizado
        List<DemandaClassificada> demandas = demandaClassificadaRepository.findAll();

        for (DemandaClassificada demanda : demandas) {
            if (demanda.getDataAprovacao() != null) {
                // Calcule o tempo de abertura em dias após a aprovação
                int tempoAberturaDias = calcularTempoAberturaEmDias(demanda.getDataAprovacao());

                // Calculo do Score com base na fórmula fornecida
                double novoScore = (2 * (demanda.getBeneficioRealDemanda() == null ? 0 : demanda.getBeneficioRealDemanda().getValorBeneficio()) +
                        (demanda.getBeneficioPotencialDemanda() == null ? 0 : demanda.getBeneficioPotencialDemanda().getValorBeneficio())
                        + tempoAberturaDias) / (demanda.getTamanhoDemandaClassificada() == TamanhoDemanda.MuitoPequeno ? 20 :
                        (demanda.getTamanhoDemandaClassificada() == TamanhoDemanda.Pequeno ? 130 :
                                (demanda.getTamanhoDemandaClassificada() == TamanhoDemanda.Medio ? 350 :
                                        demanda.getTamanhoDemandaClassificada() == TamanhoDemanda.Grande ? 2000 : 3000)));

                // Atualize o Score da demanda
                demanda.setScore(novoScore);

                // Salve a demanda atualizada no repositório
                demandaClassificadaRepository.save(demanda);
            }
        }
    }

    private int calcularTempoAberturaEmDias(Date dataAprovacao) {
        Date dataAtual = new Date();

        long diffEmMilissegundos = dataAtual.getTime() - dataAprovacao.getTime();
        long diffEmDias = TimeUnit.DAYS.convert(diffEmMilissegundos, TimeUnit.MILLISECONDS);

        return (int) diffEmDias;
    }

    public Double gerarScore(DemandaClassificada demanda) {
        // Calcule o tempo de abertura em dias após a aprovação
        int tempoAberturaDias = calcularTempoAberturaEmDias(demanda.getDataAprovacao());

        // Calculo do Score com base na fórmula fornecida
        double novoScore = (2 * (demanda.getBeneficioRealDemanda() == null ? 0 : demanda.getBeneficioRealDemanda().getValorBeneficio()) +
                (demanda.getBeneficioPotencialDemanda() == null ? 0 : demanda.getBeneficioPotencialDemanda().getValorBeneficio())
                + tempoAberturaDias) / (demanda.getTamanhoDemandaClassificada() == TamanhoDemanda.MuitoPequeno ? 20 :
                (demanda.getTamanhoDemandaClassificada() == TamanhoDemanda.Pequeno ? 130 :
                        (demanda.getTamanhoDemandaClassificada() == TamanhoDemanda.Medio ? 350 :
                                demanda.getTamanhoDemandaClassificada() == TamanhoDemanda.Grande ? 2000 : 3000)));

        return novoScore;
    }


//    public List<DemandaAnalista> findByAnalista(Usuario analista) {
//        return demandaAnalistaRepository.findByAnalista(analista);
//    }
//
//    public DemandaAnalista findByDemandaDemandaAnalista(Demanda demanda) {
//        return demandaAnalistaRepository.findByDemandaDemandaAnalista(demanda);
//    }

}
