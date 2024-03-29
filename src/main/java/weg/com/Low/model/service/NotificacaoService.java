package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import weg.com.Low.dto.NotificacaoDTO;
import weg.com.Low.model.entity.Notificacao;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.repository.NotificacaoRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NotificacaoService {
    private NotificacaoRepository notificacaoRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    public void sendNotification(String tituloDemanda, String Descricao){
//        LocalDateTime now = LocalDateTime.now();
//        NotificacaoDTO notificacaoDTO = new NotificacaoDTO();
//        notificacaoDTO.setTituloDemandaNotificacao("asdsfda");
//        notificacaoDTO.setDescricaoNotificacao("teste");
//    }

    public Notificacao save(Notificacao entity) {
        messagingTemplate.convertAndSend("/usuario", new Notificacao());
        messagingTemplate.convertAndSend("/usuario/quantidade", 0);
        return notificacaoRepository.save(entity);
    }

    public List<Notificacao> findByUsuario(Usuario usuario){
        return notificacaoRepository.findByUsuarioNotificacao(usuario);
    }

    public int countByUsuarioNotificacaoAndLidoFalse(Usuario usuario) {
        return notificacaoRepository.countByUsuarioNotificacaoAndLidoFalse(usuario);
    }

    public Optional<Notificacao> findById(Integer integer) {
        return notificacaoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return notificacaoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        notificacaoRepository.deleteById(integer);
    }

    public List<Notificacao> findAll() {
        return notificacaoRepository.findAll();
    }
}
