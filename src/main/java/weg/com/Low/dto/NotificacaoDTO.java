package weg.com.Low.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
@Data
public class NotificacaoDTO {

    private String tituloDemandaNotificacao;
    private String acaoNotificacao;
    private Integer codigo;
    private String descricaoNotificacao;
    private LocalDateTime horaNotificacao;
    private LocalDate dataNotificacao;

}
