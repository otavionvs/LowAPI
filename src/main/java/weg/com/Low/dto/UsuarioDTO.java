package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Departamento;
import weg.com.Low.model.entity.NivelAcesso;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class UsuarioDTO {
    @NotBlank
    private String nomeUsuario;
    @NotBlank
    private String userUsuario;
    @NotBlank
    private String emailUsuario;
    @NotBlank
    private String senhaUsuario;
    @NotNull
    private NivelAcesso nivelAcessoUsuario;
    @NotEmpty
    private Departamento departamentoUsuario;
}
