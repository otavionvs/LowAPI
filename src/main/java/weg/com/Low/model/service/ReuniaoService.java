package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.StatusReuniao;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.repository.ReuniaoRepository;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
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

    public List<Reuniao> findAll() {
        return reuniaoRepository.findAll();
    }

    @Transactional
    public Reuniao save(Reuniao reuniao, TipoNotificacao tipoNotificacao) {
        Usuario usuario = reuniao.getPropostasReuniao().get(0).getAnalista();
        SimpleDateFormat formatoDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        switch (tipoNotificacao) {
            case MARCOU_REUNIAO -> {
                notificacaoService.save(new Notificacao(null, "Reunião Marcada!", tipoNotificacao,
                        "Reunião com a " + reuniao.getComissaoReuniao() + " marcada para " + formatoDataHora.format(reuniao.getDataReuniao()), LocalDateTime.now(), false, usuario));
            }
            case EDITOU_REUNIAO -> {
                notificacaoService.save(new Notificacao(null, "Reunião Alterada!", tipoNotificacao,
                        "Reunião com a " + reuniao.getComissaoReuniao() + " foi alterada para " + formatoDataHora.format(reuniao.getDataReuniao()), LocalDateTime.now(), false, usuario));
            }
            case FINALIZOU_REUNIAO -> {
                notificacaoService.save(new Notificacao(null, "Reunião Finalizada!", tipoNotificacao,
                        "Reunião com a " + reuniao.getComissaoReuniao() + " finalizada!", LocalDateTime.now(), false, usuario));
            }
            case DESMARCOU_REUNIAO -> {
                notificacaoService.save(new Notificacao(null, "Reunião Desmarcada!", tipoNotificacao,
                        "Reunião com a " + reuniao.getComissaoReuniao() + " desmarcada!", LocalDateTime.now(), false, usuario));
            }
            case REUNIAO_PROXIMA -> {
                notificacaoService.save(new Notificacao(null, "Reunião está Próxima!", tipoNotificacao,
                        "Reunião com a " + reuniao.getComissaoReuniao() + " está próxima! Data: " + formatoDataHora.format(reuniao.getDataReuniao()), LocalDateTime.now(), false, usuario));
            }
            case REUNIAO_PENDETE -> {
                notificacaoService.save(new Notificacao(null, "Reunião Pendente!", tipoNotificacao,
                        "Reunião com a " + reuniao.getComissaoReuniao() + " está pendente e precisa ser realizada ou remarcada!",
                        LocalDateTime.now(), false, usuario));
            }
        }
        return reuniaoRepository.save(reuniao);
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
            save(reuniao, TipoNotificacao.REUNIAO_PROXIMA);
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

    public Page<Reuniao> search(String nomeComissao, String dataReuniao, String statusReuniao,
                                String ppmProposta, String analista, String solicitante, String ordenar, Pageable page) {
        return reuniaoRepository.search(nomeComissao.toLowerCase(), dataReuniao, statusReuniao, ppmProposta,
                analista.toLowerCase(), solicitante.toLowerCase(), ordenar, page);
    }
}
