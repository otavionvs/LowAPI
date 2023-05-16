package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Beneficio;
import weg.com.Low.model.entity.CentroCusto;
import weg.com.Low.model.entity.Recurso;
import weg.com.Low.model.entity.Usuario;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Getter
public class PropostaDTO {
    @NotNull
    private Integer codigoDemanda;
    @NotNull
    private Integer version;
    @NotBlank
    private String tituloDemanda;
    @NotBlank
    private String situacaoAtualDemanda;
    @NotBlank
    private String objetivoDemanda;
    @NotBlank
    private String frequenciaDeUsoDemanda;
    @NotBlank
    private String beneficioQualitativoDemanda;
    private String motivoReprovacaoDemanda;
//    @NotNull
    private Integer score;
    @NotEmpty
    private List<CentroCusto> centroCustosDemanda;
    @NotNull
    private Beneficio beneficioPotencialDemanda;
    @NotNull
    private Beneficio beneficioRealDemanda;
    @NotNull
    @FutureOrPresent
    private Date prazoProposta;
    @NotNull
    private Integer codigoPPMProposta;
    @NotBlank
    private String jiraProposta;
    @FutureOrPresent
    private Date inicioExDemandaProposta;
    @Future
    private Date fimExDemandaProposta;
    @NotNull
    private Double paybackProposta;
    private String parecerComissaoProposta;
    private String recomendacaoProposta;
    @NotNull
    private List<String> responsavelProposta;
    @NotEmpty
    private List<RecursoDTO> recursosProposta;
    @NotNull
    private Usuario solicitanteDemanda;
    @NotNull
    private String escopoDemandaProposta;
    private String statusDemanda;
    //parecer e sugestão - adicionar na demanda maior
//    private Arquivo arquivoProposta
}
