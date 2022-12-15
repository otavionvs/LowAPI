package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Moeda;

import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
public class BeneficioDTO {
    @NotNull
    private Moeda moeda;
    @NotNull
    private String memoriaCalculo;
    @NotNull
    @PositiveOrZero
    private Double valor;
}
