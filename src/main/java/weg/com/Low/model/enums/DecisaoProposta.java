package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DecisaoProposta {
    APROVAR("Aprovar"),
    APROVAR_COM_RECOMENDACAO("Aprovar com Recomendação"),
    REAPRESENTAR_COM_RECOMENDACAO("Reapresentar com Recomendação"),
    REPROVAR("Reprovar");

    String decisao;
}
