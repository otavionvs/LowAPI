package weg.com.Low.dto;

import lombok.Getter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter
public class BusinessUnitDTO {
    @NotBlank
    private String nomeBusinessUnit;
}
