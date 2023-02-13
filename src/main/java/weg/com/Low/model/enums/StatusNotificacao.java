package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusNotificacao {
    ATIVADA("ativada"),
    DESATIVADA("desativada");

    String statusNotificacao;
}
