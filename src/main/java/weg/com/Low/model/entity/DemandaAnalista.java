package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "demanda_analista")
@AllArgsConstructor
public class DemandaAnalista extends Demanda{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    int codigoDemandaAnalista;
    @Column(nullable = false)
    String tamanhoDemandaAnalista;

    @OneToOne
    @JoinColumn(name = "BU_solicitante")
    private BusinessUnit buSolicitanteDemandaAnalista;

    @OneToOne
    @JoinColumn(name = "BU_beneficiada")
    private BusinessUnit buBeneficiadaDemandaAnalista;

    @ManyToOne
    @JoinColumn(name = "codigo_sessao")
    private Sessao sessaoDemandaAnalista;

    @OneToMany
    @JoinColumn(name = "codigo_arquivo")
    private Arquivo arquivoDemandaAnalista;

    @OneToOne
    @JoinColumn(name = "demanda_codigo")
    private Demanda demandaDemandaAnalista;
}
