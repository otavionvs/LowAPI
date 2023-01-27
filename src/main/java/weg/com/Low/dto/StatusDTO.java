package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Status;

import javax.validation.constraints.NotBlank;

@Getter
public class StatusDTO {
    @NotBlank
    private Status statusDemanda;
}
