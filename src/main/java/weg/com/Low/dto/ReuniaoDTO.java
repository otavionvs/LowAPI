package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Ata;
import weg.com.Low.model.entity.Comissao;
import weg.com.Low.model.entity.Proposta;
import weg.com.Low.model.entity.StatusReuniao;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
public class ReuniaoDTO {
    @NotNull
    @FutureOrPresent
    Date dataReuniao;
    @NotNull
    private Comissao comissaoReuniao;
    @NotEmpty
    private List<Proposta> propostasReuniao;
    private Ata ataReuniao;
    private StatusReuniao statusReuniao;
}
