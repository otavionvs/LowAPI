package weg.com.Low.dto;

import lombok.Data;
import weg.com.Low.model.entity.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DemandaAnalistaDTO {
    @NotNull
    private TamanhoDemanda tamanhoDemandaAnalista;
    @NotNull
    private BusinessUnit buSolicitanteDemandaAnalista;
    @NotEmpty
    private List<BusinessUnit> busBeneficiadasDemandaAnalista;
    @NotNull
    private Demanda demandaDemandaAnalista;
    @NotNull
    private Usuario analista;
    @NotNull
    private Secao secaoDemandaAnalista;
}
