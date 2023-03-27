package weg.com.Low.model.entity;

import lombok.Data;
import weg.com.Low.model.enums.PerfilDespesa;
import weg.com.Low.model.enums.TipoDespesa;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "recurso")
@Data
public class Recurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Enumerated(value = EnumType.STRING)
    private PerfilDespesa perfilDespesaRecurso;
    @Column(nullable = false)
    private Integer periodoExMesesRecurso;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "recurso_centro_custo", joinColumns = @JoinColumn(name = "codigo_recurso"),
            inverseJoinColumns = @JoinColumn(name = "codigo_centro_custo"))
    private List<CentroCusto> centroCustos;

}
