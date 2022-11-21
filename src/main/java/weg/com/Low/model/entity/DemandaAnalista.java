package weg.com.Low.model.entity;

import java.util.Date;
import java.util.List;

public class DemandaAnalista extends Demanda{
    int idProposta;
    String escopoProposta, paybackProposta, parecerComissaoProposta, parecerDGProposta;
    Date periodoInicioProposta, periodoFimProposta;
    List<Recurso> recursoProposta;
    Usuario responsavelProposta;
    byte anexosProposta;
}
