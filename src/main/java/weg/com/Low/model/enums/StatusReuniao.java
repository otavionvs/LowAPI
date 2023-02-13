package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusReuniao {
    AGUARDANDO("aguardando"),
    PROXIMO("próximo"),
    PENDENTE("pendente"),
    CONCLUIDO("concluído"),
    CANCELADO("cancelado");

    String statusReuniao;
}
