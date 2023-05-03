package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusMensagens {
    ENVIADA("enviada"),
    NAO_VISTA("nao vista"),
    VISTA("vista");

    String statusMensagens;
}
