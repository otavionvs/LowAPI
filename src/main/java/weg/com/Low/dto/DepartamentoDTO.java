package weg.com.Low.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class DepartamentoDTO {
    @NotBlank
    private String nome;
}
