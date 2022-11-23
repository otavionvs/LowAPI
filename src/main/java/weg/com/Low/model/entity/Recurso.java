package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "recurso")
@Data
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int idRecurso;
    @Column(nullable = false, length = 10)
    private  int quantidadeHorasRecurso;
    @Column(nullable = false)
    private double valorHoraRecurso;
    @Column(nullable = false, length = 80)
    private String nomeRecurso;
    @Column(nullable = false)
    private String tipoDespesaRecurso;
    @Column(nullable = false)
    private  String perfilDespesa;
    @Column(nullable = false)
    private String periodoExecucaoRecurso;

    @ManyToOne
    @JoinColumn(name = "codigo_centro_pagante")
    private CentroCusto centroDeCustoRecurso;

}
