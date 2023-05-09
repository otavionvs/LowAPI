package weg.com.Low.model.entity;

import lombok.Data;
import weg.com.Low.model.enums.TipoAtaProposta;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Proposta extends DemandaClassificada {
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

    @Column(columnDefinition = "longtext")
    private String parecerComissaoProposta;

    @Column
    private TipoAtaProposta tipoAtaProposta;

    @Column(columnDefinition = "longtext")
    private String recomendacaoProposta;

    @Column
    private String ultimaDecisaoComissao;

    @Column(columnDefinition = "longtext")
    private String escopoDemandaProposta;

    @Column(columnDefinition = "longtext")
    private String responsavelProposta;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "proposta_recurso", joinColumns = {
    @JoinColumn(name = "codigo_proposta", referencedColumnName = "codigo_demanda"), @JoinColumn(name = "version", referencedColumnName = "version")},
            inverseJoinColumns = @JoinColumn(name = "codigo_recurso"))
    private List<Recurso> recursosProposta;

//    @ManyToOne
//    @JoinColumn(name = "codigo_arquivo")
//    private Arquivo arquivoProposta;

}
