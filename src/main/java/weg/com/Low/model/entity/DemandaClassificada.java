package weg.com.Low.model.entity;

import lombok.Data;
import weg.com.Low.model.enums.BussinessUnit;
import weg.com.Low.model.enums.Secao;
import weg.com.Low.model.enums.TamanhoDemanda;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class DemandaClassificada extends Demanda{
    @Enumerated(value = EnumType.STRING)
    @Column
    private TamanhoDemanda tamanhoDemandaClassificada;

    @Enumerated(value = EnumType.STRING)
    @Column
    private BussinessUnit buSolicitanteDemandaClassificada;

    @ElementCollection
    @Enumerated(value = EnumType.STRING)
    @Column
    private List<BussinessUnit> busBeneficiadasDemandaClassificada;

    @Enumerated(value = EnumType.STRING)
    @Column
    private Secao secaoDemandaClassificada;

    @Column
    private Double score;

    @Column
    private Date dataAprovacao;


    public void setAll(DemandaClassificada demandaClassificada){
        tamanhoDemandaClassificada = demandaClassificada.tamanhoDemandaClassificada;
        buSolicitanteDemandaClassificada = demandaClassificada.buSolicitanteDemandaClassificada;
        System.out.println(demandaClassificada.busBeneficiadasDemandaClassificada);
        busBeneficiadasDemandaClassificada = demandaClassificada.busBeneficiadasDemandaClassificada;
        secaoDemandaClassificada = demandaClassificada.secaoDemandaClassificada;
        this.setAnalista(demandaClassificada.getAnalista());
        this.setGerenteNegocio(demandaClassificada.getGerenteNegocio());
        this.setSolicitanteDemanda(demandaClassificada.getSolicitanteDemanda());
        setCodigoDemanda(demandaClassificada.getCodigoDemanda());
    }
}
