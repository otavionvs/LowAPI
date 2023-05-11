package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.StatusNotificacao;
import weg.com.Low.model.enums.StatusReuniao;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.repository.ReuniaoRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@EnableScheduling
public class ReuniaoService {
    private ReuniaoRepository reuniaoRepository;
    private NotificacaoService notificacaoService;
    private DemandaClassificadaService demandaClassificadaService;
    private DemandaService demandaService;


    public List<Reuniao> findAll() {
        return reuniaoRepository.findAll();
    }

    public Reuniao save(Reuniao reuniao) {
//        notificacaoService.save(new Notificacao(
//                null,
//                reuniao.getDataReuniao().toString(),
//                reuniao.getCodigoReuniao(),
//                TipoNotificacao.MARCOU_REUNIAO,
//                "Uma reunião de uma demanda que você está envolvido foi marcada! ",
//                LocalDateTime.now(),
//                LocalDate.now(),
//                StatusNotificacao.ATIVADA,
//                usuarios
//                ));
        return reuniaoRepository.save(reuniao);
    }
    @PostConstruct
    public void inicializar() {
        atualizarStatusProximo();
    }

    //43200000 equivale a meio dia
    @Scheduled(fixedDelay = 43200000)
    public void atualizarStatusProximo() {
        Date agora = new Date();
        Date umaSemanaDepois = new Date(agora.getTime() + (7 * 24 * 60 * 60 * 1000));
        // recupera todas as reuniões que podem ter seu status alterado
        List<Reuniao> reunioes = reuniaoRepository.findByDataReuniaoBetweenAndStatusReuniao(agora, umaSemanaDepois, StatusReuniao.AGUARDANDO);

        for (Reuniao reuniao : reunioes) {
            reuniao.setStatusReuniao(StatusReuniao.PROXIMO);
            reuniaoRepository.save(reuniao);
        }


        List<Reuniao> reunioesPendentes = reuniaoRepository.findByDataReuniaoBeforeAndStatusReuniao(agora, StatusReuniao.PROXIMO);
        for (Reuniao reuniao : reunioesPendentes) {
            reuniao.setStatusReuniao(StatusReuniao.PENDENTE);
            reuniaoRepository.save(reuniao);
        }
    }

    public Optional<Reuniao> findById(Integer codigo) {
        return reuniaoRepository.findById(codigo);
    }

    public boolean existsById(Integer codigo) {
        return reuniaoRepository.existsById(codigo);
    }

    public void deleteById(Integer codigo) {
        reuniaoRepository.deleteById(codigo);
    }

    public List<Reuniao> search(String nomeComissao, String dataReuniao, String statusReuniao,
            String ppmProposta, String analista, String solicitante, Pageable page) {
        return reuniaoRepository.search(nomeComissao.toLowerCase(), dataReuniao, statusReuniao, ppmProposta,
                analista.toLowerCase(), solicitante.toLowerCase(), page);
    }
}
