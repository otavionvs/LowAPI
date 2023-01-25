package weg.com.Low.model.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "demanda_analista")
@Data
public class DemandaAnalista{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer codigoDemandaAnalista;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TamanhoDemanda tamanhoDemandaAnalista;
    @OneToOne
    @JoinColumn(name = "bu_solicitante", nullable = false)
    private BusinessUnit buSolicitanteDemandaAnalista;
    @ManyToMany
    @JoinTable(name = "bu_beneficiada", joinColumns =
    @JoinColumn(name = "codigo_demanda_analista", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "codigo_business_unit", nullable = false))
    private List<BusinessUnit> busBeneficiadasDemandaAnalista;

//    @ManyToOne
//    @JoinColumn(name = "codigo_arquivo")
//    private Arquivo arquivoDemandaAnalista;

    @OneToOne
    @JoinColumn(name = "secao_codigo", nullable = false)
    private Secao secaoDemandaAnalista;

    @OneToOne
    @JoinColumn(name = "demanda_codigo", nullable = false)
    private Demanda demandaDemandaAnalista;

    @OneToOne
    @JoinColumn(name = "analista_codigo", nullable = false)
    private Usuario analista;

    @OneToOne
    @JoinColumn(name = "gerente_negocio_codigo")
    private Usuario gerenteNegocio;
}
