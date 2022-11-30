package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Moeda;

import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotNull;

@Getter
public class BeneficioDTO {
    @NotNull
    private Moeda moeda;
    @NotNull
    private String memoriaCalculo;
    @NotNull
    @NegativeOrZero
    private Double valor;
}
