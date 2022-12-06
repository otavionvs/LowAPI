package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class DemandaAnalistaDTO {
    @NotNull
    private TamanhoDemanda tamanhoDemandaAnalista;
    @NotNull
    private BusinessUnit BUsolicitanteDemandaAnalista;
    @NotEmpty
    private List<BusinessUnit> BUsBeneficiadasDemandaAnalista;
    @NotNull
    private Demanda demandaDemandaAnalista;
    @NotNull
    private Usuario analista;
    @NotNull
    private Sessao sessao;
}
