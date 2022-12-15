package weg.com.Low.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class FiltroDemandaDTO {
    private String nome_usuario;
    private String email_usuario;
}
