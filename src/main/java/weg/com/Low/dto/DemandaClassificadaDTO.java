package weg.com.Low.dto;

import lombok.Data;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.BussinessUnit;
import weg.com.Low.model.enums.Secao;
import weg.com.Low.model.enums.TamanhoDemanda;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DemandaClassificadaDTO {
    @NotNull
    private TamanhoDemanda tamanhoDemandaClassificada;
    @NotNull
    private BussinessUnit buSolicitanteDemandaClassificada;
    @NotEmpty
    private List<BussinessUnit> busBeneficiadasDemandaClassificada;
    @NotNull
    private Integer codigoDemanda;
    @NotNull
    private Secao secaoDemandaClassificada;
}
