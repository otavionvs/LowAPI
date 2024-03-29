package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.enums.Moeda;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
public class BeneficioDTO {
    @NotNull
    private Moeda moedaBeneficio;
    @NotNull
    private String memoriaDeCalculoBeneficio;
    @NotNull
    @PositiveOrZero
    private Double valorBeneficio;
}
