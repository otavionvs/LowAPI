package weg.com.Low.model.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import weg.com.Low.dto.NotificacaoDTO;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class NotificacaoService {
    private SimpMessagingTemplate messagingTemplate;
    public void sendNotification(String tituloDemanda, String Descricao){
        LocalDateTime now = LocalDateTime.now();
        NotificacaoDTO notificacaoDTO = new NotificacaoDTO();
        notificacaoDTO.setDataNotificacao(now.toLocalDate());
        notificacaoDTO.setHoraNotificacao(now);
        notificacaoDTO.setTituloDemandaNotificacao("asdsfda");
        notificacaoDTO.setDescricaoNotificacao("teste");
        this.messagingTemplate.convertAndSend("/topic/notifications" + notificacaoDTO);
    }
}
