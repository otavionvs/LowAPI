package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "proposta")
@Entity
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int codigoProposta;

    @Column(nullable = false)
    private Date prazoProposta;

    @Column(nullable = false)
    private int codigoPPMProposta;

    @Column(nullable = false)
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

    @OneToOne
    @JoinColumn(name = "codigo_demandaAnalista")
    private DemandaAnalista demandaAnalistaProposta;

    @ManyToMany
    @JoinTable(name = "proposta_recurso", joinColumns =
    @JoinColumn(name = "codigo_proposta"),
            inverseJoinColumns = @JoinColumn(name = "codigo_recurso"))
    private List<Recurso> recursosProposta;

    @ManyToOne
    @JoinColumn(name = "codigo_arquivo")
    private Arquivo arquivoProposta;

}
