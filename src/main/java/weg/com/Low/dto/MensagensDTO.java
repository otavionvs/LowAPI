package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Usuario;

@Getter
public class MensagensDTO {
    private String textoMensagens;
    private Demanda demandaMensagens;
    private Usuario usuarioMensagens;
}
