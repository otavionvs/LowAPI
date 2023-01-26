package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.DemandaAnalista;
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
    private Integer codigoPPMProposta;
    @NotNull
    private String jiraProposta;
    @NotNull
    private List<RecursoDTO> recursosProposta;
    @NotNull
    private String escopoDemandaProposta;
    @FutureOrPresent
    private Date inicioExDemandaProposta;
    @Future
    private Date fimExDemandaProposta;
    @NotNull
    private Double paybackDemandaProposta;
    @NotNull
    private Usuario responsavelProposta;
    @NotNull
    private DemandaAnalista demandaAnalistaProposta;
//    private Arquivo arquivoProposta
}
