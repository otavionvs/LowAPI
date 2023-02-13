package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NivelAcesso {
    Solicitante("Solicitante"), //0
    Analista("Analista"), //1
    GerenteNegocio("Gerente de Negocio"), //2
    GestorTI("Gestor de TI"); //3

    String nivelAcesso;
}
