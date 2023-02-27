package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.StatusNotificacao;
import weg.com.Low.model.enums.TipoNotificacao;
import weg.com.Low.repository.ReuniaoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReuniaoService {
    private ReuniaoRepository reuniaoRepository;
    private NotificacaoService notificacaoService;
    private DemandaAnalistaService demandaAnalistaService;
    private DemandaService demandaService;

    public List<Reuniao> findAll() {
        return reuniaoRepository.findAll();
    }

    public Reuniao save(Reuniao reuniao) {
        List<Proposta> propostas = reuniao.getPropostasReuniao();
        List<Usuario> usuarios = null;
        for (Proposta proposta: propostas){
            DemandaAnalista demandaAnalista = demandaAnalistaService.findById(proposta.getCodigoDemanda()).get();
            usuarios.add(demandaAnalista.getAnalista());
            usuarios.add(demandaAnalista.getGerenteNegocio());
            Demanda demanda = demandaService.findById(demandaAnalista.getCodigoDemanda()).get();
            usuarios.add(demanda.getSolicitanteDemanda());
        }
        notificacaoService.save(new Notificacao(
                null,
                reuniao.getDataReuniao().toString(),
                reuniao.getCodigoReuniao(),
                TipoNotificacao.MARCOU_REUNIAO,
                "Uma reunião de uma demanda que você está envolvido foi marcada! ",
                LocalDateTime.now(),
                LocalDate.now(),
                StatusNotificacao.ATIVADA,
                usuarios
                ));
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
