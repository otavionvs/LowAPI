package weg.com.Low.dto;

import lombok.Getter;
import lombok.Setter;
import weg.com.Low.model.entity.*;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ReuniaoDTO {
    @NotNull
    @FutureOrPresent
    Date dataReuniao;
    @NotNull
    private Comissao comissaoReuniao;

    private List<Demanda> demandasReuniao;
    private List<Proposta> propostasReuniao;

    private Ata ataReuniao;
    private StatusReuniao statusReuniao;

    private Integer codigoReuniao;
}
