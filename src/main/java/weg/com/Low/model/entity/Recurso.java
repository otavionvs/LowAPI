package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "recurso")
@Data
public class Recurso {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer codigoRecurso;
    @Column(nullable = false, length = 65)
    private String nomeRecurso;
    @Column(nullable = false, length = 5)
    private  Integer quantidadeHorasRecurso;
    @Column(nullable = false)
    private Double valorHoraRecurso;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TipoDespesa tipoDespesaRecurso;
    @Column(nullable = false)
    private PerfilDespesa perfilDespesaRecurso;
    @Column(nullable = false)
    private Integer periodoExecucaoRecurso;

}
