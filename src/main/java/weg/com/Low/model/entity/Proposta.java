package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "proposta")
@Entity
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column()
    private int codigoProposta;

    @Column(nullable = false)
    private Date prazoProposta;

    @Column()
    private int codigoPPMProposta;

    @Column(nullable = false)
    private String jiraProposta;

    @Column()
    private Date periodoExeDemandaInicioProposta;

    @Column()
    private Date periodoExeDemandaFimProposta;

    @Column()
    private double paybackProposta;

    @Column()
    private Usuario responsavelProposta;

    @Column()
    private String areaResponsavelProposta;

    @ManyToMany
    @JoinTable(name = "proposta_recurso", joinColumns =
    @JoinColumn(name = "codigo_proposta", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "codigo_recurso", nullable = false))
    private Collection<Recurso> recursos;

    @OneToMany
    @JoinColumn(name = "codigo_arquivo")
    private Arquivo arquivo;

}
