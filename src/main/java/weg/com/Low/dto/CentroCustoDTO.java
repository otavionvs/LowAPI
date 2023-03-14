package weg.com.Low.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CentroCustoDTO {
    @NotNull
    private String nomeCentroCusto;
    @NotNull
    private Integer porcentagemCentroCusto;
}
