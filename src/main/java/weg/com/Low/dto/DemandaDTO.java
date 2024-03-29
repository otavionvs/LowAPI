package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Beneficio;
import weg.com.Low.model.entity.CentroCusto;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.enums.Status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class DemandaDTO {
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
    @NotNull
    private BeneficioDTO beneficioPotencialDemanda;
    @NotNull
    private BeneficioDTO beneficioRealDemanda;
    @NotEmpty
    private List<CentroCustoDTO> centroCustosDemanda;
    private Integer codigoDemanda;
    private Status statusDemanda;
}
