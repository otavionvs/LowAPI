package weg.com.Low.model.entity;

import lombok.Data;
import javax.persistence.*;
@Entity
@Table(name = "demanda_analista")
@Data
public class DemandaAnalista{
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

    @ManyToOne
    @JoinColumn(name = "codigo_arquivo")
    private Arquivo arquivoDemandaAnalista;

    @OneToOne
    @JoinColumn(name = "demanda_codigo")
    private Demanda demandaDemandaAnalista;
}
