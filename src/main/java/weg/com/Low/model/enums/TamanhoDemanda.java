package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TamanhoDemanda {
    MuitoPequeno("Muito Pequeno"),
    Pequeno("Pequeno"),
    Medio("Médio"),
    Grande("Grande"),
    MuitoGrande("Muito Grande");

    String tamanho;
}
