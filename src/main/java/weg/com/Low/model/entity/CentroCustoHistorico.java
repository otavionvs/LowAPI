package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "centro_custo_historico")
@Data
public class CentroCustoHistorico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoCentroCustoHistorico;
    @Column(nullable = false, length = 100)
    private String nomeCentroCustoHistorico;
}
