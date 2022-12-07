package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.CentroCusto;
import weg.com.Low.model.entity.Recurso;

import javax.validation.constraints.NotNull;

@Getter
public class CentroCustoRecursoDTO {
    @NotNull
    private CentroCusto centroPagante;
    @NotNull
    private Recurso recurso;
    @NotNull
    private Double porcentagemCusto;
}
