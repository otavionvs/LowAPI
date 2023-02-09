package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PerfilDespesa {
    hardware("Hardware"),
        software("Software"),
    corporativo("Corporativo");

    String perfilDespesa;
}
