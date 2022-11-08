package weg.com.Low.model.entity;

import java.util.Date;
import java.util.List;

public class Proposta extends br.weg.sc.low.model.entities.Demanda {
    int idProposta;
    String escopoProposta, paybackProposta, parecerComissaoProposta, parecerDGProposta;
    Date periodoInicioProposta, periodoFimProposta;
    List<Recurso> recursoProposta;
    br.weg.sc.low.model.entities.Solicitante responsavelProposta;
    byte anexosProposta;
}
