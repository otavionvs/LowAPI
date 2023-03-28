package weg.com.Low.model.entity;

import lombok.Data;
import weg.com.Low.model.enums.BussinessUnit;
import weg.com.Low.model.enums.Secao;
import weg.com.Low.model.enums.TamanhoDemanda;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "demanda_classificada")
@Data
public class DemandaClassificada extends Demanda{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false)
//    private Integer codigoDemandaAnalista;
//    @EmbeddedId
//    DemandaId codigoDemandaAnalista;

    @Enumerated(value = EnumType.STRING)
    @Column
    private TamanhoDemanda tamanhoDemandaClassificada;

    @Enumerated(value = EnumType.STRING)
    @Column
    private BussinessUnit buSolicitanteDemandaClassificada;
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "bu_beneficiada", joinColumns = {
//    @JoinColumn(name = "codigo_demanda", referencedColumnName = "codigo_demanda", nullable = false), @JoinColumn(name = "version", referencedColumnName = "version", nullable = false)},
//            inverseJoinColumns = @JoinColumn(name = "codigo_business_unit", nullable = false))
//    private List<BusinessUnit> busBeneficiadasDemandaAnalista;

//    @CollectionTable(name = "busBeneficiadasDemandaClassificada", joinColumns = {@JoinColumn(name = "codigo_demanda", @JoinColumn(name = "version")}))
    @ElementCollection
    @Enumerated(value = EnumType.STRING)
    @Column
    private List<BussinessUnit> busBeneficiadasDemandaClassificada;

//    @ManyToOne
//    @JoinColumn(name = "codigo_arquivo")
//    private Arquivo arquivoDemandaAnalista;

    @Enumerated(value = EnumType.STRING)
    @Column
    private Secao secaoDemandaClassificada;

//    @OneToOne
//    @JoinColumn(name = "demanda_codigo", nullable = false)
//    private Demanda demandaDemandaAnalista;

    @OneToOne
    @JoinColumn(name = "analista_codigo")
    private Usuario analista;

    @OneToOne
    @JoinColumn(name = "gerente_negocio_codigo")
    private Usuario gerenteNegocio;
}
