package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.StatusNotificacao;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.repository.ReuniaoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReuniaoService {
    private ReuniaoRepository reuniaoRepository;
    private NotificacaoService notificacaoService;

    public List<Reuniao> findAll() {
        return reuniaoRepository.findAll();
    }

    public Reuniao save(Reuniao reuniao, TipoNotificacao tipoNotificacao) {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(reuniao.getPropostasReuniao().get(0).getAnalista());
        switch (tipoNotificacao){
            case MARCOU_REUNIAO -> {
                notificacaoService.save(new Notificacao(null, "Reunião Marcada!", tipoNotificacao,
                        "Reunião com a " + reuniao.getComissaoReuniao(), LocalDateTime.now(), false, usuarios));
            }
            case EDITOU_REUNIAO -> {
                notificacaoService.save(new Notificacao(null, "Reunião Alterada!", tipoNotificacao,
                        "Reunião com a " + reuniao.getComissaoReuniao(), LocalDateTime.now(), false, usuarios));
            }
            case FINALIZOU_REUNIAO -> {
                notificacaoService.save(new Notificacao(null, "Reunião Finalizada!", tipoNotificacao,
                        "Reunião com a " + reuniao.getComissaoReuniao(), LocalDateTime.now(), false, usuarios));
            }
            case DESMARCOU_REUNIAO -> {
                notificacaoService.save(new Notificacao(null, "Reunião Desmarcada!", tipoNotificacao,
                        "Reunião com a " + reuniao.getComissaoReuniao(), LocalDateTime.now(), false, usuarios));
            }
        }
        return reuniaoRepository.save(reuniao);
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
