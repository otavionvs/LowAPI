package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoAtaProposta {
    PUBLICADA("Publicada"),
    NAO_PUBLICADA("Não Publicada");
    String valor;
}
