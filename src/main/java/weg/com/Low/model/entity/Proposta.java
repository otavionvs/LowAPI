package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "proposta")
@Entity
public class Proposta extends DemandaAnalista{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column
//    private Integer codigoProposta;

    @Column
    private Date prazoProposta;

    @Column
    private Integer codigoPPMProposta;

    @Column(length = 1000)
    private String jiraProposta;

    @Column
    private Date inicioExDemandaProposta;

    @Column
    private Date fimExDemandaProposta;

    @Column
    private Double paybackProposta;

    @OneToOne
    @JoinColumn(name = "codigo_responsavel")
    private Usuario responsavelProposta;

//    @OneToOne
//    @JoinColumn(name = "codigo_demandaAnalista")
//    private DemandaAnalista demandaAnalistaProposta;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "proposta_recurso", joinColumns = {
    @JoinColumn(name = "codigo_proposta", referencedColumnName = "codigo_demanda"), @JoinColumn(name = "version", referencedColumnName = "version")},
            inverseJoinColumns = @JoinColumn(name = "codigo_recurso"))
    private List<Recurso> recursosProposta;

    @ManyToOne
    @JoinColumn(name = "codigo_arquivo")
    private Arquivo arquivoProposta;

}
