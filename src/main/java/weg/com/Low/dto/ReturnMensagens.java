package weg.com.Low.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import weg.com.Low.model.entity.Usuario;

import java.util.Date;
@Data
@AllArgsConstructor
public class ReturnMensagens {
    private Integer qtdMensagensNaoLidas;
    private Date horaUltimaMensagem;
    private Integer codigoDemanda;
    private Usuario usuarioAguardando;
}
