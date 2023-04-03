package weg.com.Low.security;

import lombok.Data;
import lombok.NonNull;
@Data
public class UsuarioLoginDTO {
    @NonNull
    private String usuarioUsuario;
    @NonNull
    private String senhaUsuario;
}
