//package weg.com.Low.model.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import javax.persistence.*;
//
//@Entity
//@Data
//@AllArgsConstructor
//@Table(name = "centro_pagante_recurso")
//public class CentroCustoRecurso {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column
//    private Integer codigoCentroCustoRecurso;
//
//    @ManyToOne
//    @JoinColumn(name = "centros_pagante_codigo", nullable = false)
//    private CentroCusto centroPagante;
//
//    @ManyToOne
//    @JoinColumn(name = "recurso_codigo", nullable = false)
//    private Recurso recurso;
//
//    @Column(nullable = false)
//    private Double porcentagemCusto;
//}