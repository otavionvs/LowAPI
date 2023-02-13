package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoDespesa {
    interno("Interno"),
    externo("Externo");

    String tipoDespesa;
}
