package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "demanda")
@Data
public class Demanda {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer codigoDemanda;
    @Column(nullable = false)
    private String tituloDemanda;
    @Column(nullable = false)
    private String situacaoAtualDemanda;
    @Column(nullable = false)
    private String objetivoDemanda;
    @Column(nullable = false)
    private String FrequenciaDeUsoDemanda;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Column(nullable = false)
    private String BeneficioQualitativoDemanda;
    @OneToOne
    @JoinColumn(name = "beneficio_potencial_demanda", nullable = false)
    private Beneficio beneficioPotencialDemanda;
    @OneToOne
    @JoinColumn(name = "beneficio_real_demanda", nullable = false)
    private Beneficio beneficioRealDemanda;
    @OneToOne
    @JoinColumn(name = "solicitante_demanda", nullable = false)
    private Usuario solicitanteDemanda;
//    @OneToOne
//    @JoinColumn(name = "conversa_demanda", nullable = false)
//    private Conversa conversaDemanda;
//    @ManyToMany
//    @JoinTable(name = "centro_custos_demanda", joinColumns =
//    @JoinColumn(name = "codigo_demanda", nullable = false),
//            inverseJoinColumns = @JoinColumn(name = "codigo_centro_custo", nullable = false))
//    private List<CentroCusto> centroCustos;
//    @ManyToMany
//    @JoinTable(name = "historico_demanda", joinColumns =
//    @JoinColumn(name = "codigo_demanda", nullable = false),
//            inverseJoinColumns = @JoinColumn(name = "codigo_historico", nullable = false))
//    private List<Historico> historicos;


}
