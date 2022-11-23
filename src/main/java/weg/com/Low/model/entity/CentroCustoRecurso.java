package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "centro_pagante_recurso")
public class CentroCustoRecurso {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column()
    private int codigoCentroCustoRecurso;

    @ManyToOne
    @JoinColumn(name = "centros_pagantes")
    private CentroCusto centroPagante;

    @ManyToOne
    @JoinColumn(name = "recursos")
    private Recurso recurso;


    @Column(nullable = false)
    private double porcentagemCusto;

}