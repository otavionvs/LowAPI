package weg.com.Low.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ComissaoDTO {
    @NotBlank
    private String nomeComissao;
}
