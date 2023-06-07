package weg.com.Low.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import weg.com.Low.model.entity.Conversa;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Usuario;

import java.util.Map;

@Getter
public class MensagemDTO {
    private String textoMensagem;
    private Conversa conversaMensagem;
    private Usuario usuarioMensagem;
    private Map<String, MultipartFile> multipartFile;
}
