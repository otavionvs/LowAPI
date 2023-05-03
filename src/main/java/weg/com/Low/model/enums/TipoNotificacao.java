package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoNotificacao {
    CRIOU_DEMANDA("criou demanda"),
    EDITOU_DEMANDA("editou demanda"),
    AVANCOU_STATUS_DEMANDA("avancou status demanda"),
    CANCELOU_DEMANDA("cancelou demanda"),
    MARCOU_REUNIAO("marcou reuniao"),
    DESMARCOU_REUNIAO("desmarcou reuniao");

    String tipoNotificacao;
}
