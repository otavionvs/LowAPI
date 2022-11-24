package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Arquivo;
import weg.com.Low.model.entity.Recurso;
import weg.com.Low.model.entity.Usuario;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
public class PropostaDTO {
    private int codigoProposta;
    private Date prazoProposta;
    private int codigoPPMProposta;
    private String jiraProposta;
    private Date periodoExeDemandaInicioProposta;
    private Date periodoExeDemandaFimProposta;
    private double paybackProposta;
    private Usuario responsavelProposta;
    private String areaResponsavelProposta;
    private List<Recurso> recursosProposta;
    private Arquivo arquivoProposta;
}
