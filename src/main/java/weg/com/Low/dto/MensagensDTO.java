package weg.com.Low.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Usuario;

import java.util.Map;

@Getter
public class MensagensDTO {
    private String textoMensagens;
    private Demanda demandaMensagens;
    private Usuario usuarioMensagens;
    private Map<String, MultipartFile> multipartFile;
}
