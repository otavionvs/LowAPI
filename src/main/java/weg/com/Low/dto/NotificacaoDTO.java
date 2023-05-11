package weg.com.Low.dto;

import lombok.Data;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.enums.TipoNotificacao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class NotificacaoDTO {
    @NotBlank
    private String tituloDemandaNotificacao;
    @NotNull
    private TipoNotificacao tipoNotificacao;
    @NotBlank
    private String descricaoNotificacao;
    @NotEmpty
    private List<Usuario> usuariosNotificacao;

}
