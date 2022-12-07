package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Beneficio;
import weg.com.Low.model.entity.Usuario;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class DemandaDTO {
    @NotBlank
    private String tituloDemanda;
    @NotBlank
    private String situacaoAtualDemanda;
    @NotBlank
    private String objetivoDemanda;
    @NotBlank
    private String FrequenciaDeUsoDemanda;
    @NotBlank
    private String BeneficioQualitativoDemanda;
    @NotNull
    private Beneficio beneficioPotencialDemanda;
    @NotNull
    private Beneficio beneficioRealDemanda;
    @NotNull
    private Usuario solicitanteDemanda;
}
