package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Beneficio;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class RascunhoDTO {
    private String tituloDemanda;
    private String situacaoAtualDemanda;
    private String objetivoDemanda;
    private String frequenciaDeUsoDemanda;
    private String beneficioQualitativoDemanda;
    private Beneficio beneficioPotencialDemanda;
    private Beneficio beneficioRealDemanda;
    private List<CentroCustoDTO> centroCustosDemanda;
    private Integer codigoDemanda;
}
