package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Recurso;
import weg.com.Low.model.entity.Usuario;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
public class PropostaDTO {
    @NotNull
    @FutureOrPresent
    private Date prazoProposta;
    @NotNull
    private int codigoPPMProposta;
    @NotNull
    private String jiraProposta;
    @FutureOrPresent
    private Date periodoExeDemandaInicioProposta;
    @Future
    private Date periodoExeDemandaFimProposta;
    private Double paybackProposta;
    private Usuario responsavelProposta;
    private String areaResponsavelProposta;
    private List<Recurso> recursosProposta;
//    private Arquivo arquivoProposta;
}
