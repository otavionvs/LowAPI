package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "beneficio")
@Data
public class Beneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer codigoBeneficio;
    @Column(nullable = false)
    private String memoriaDeCalculoBeneficio;
    @Column(nullable = false)
    private Double valorBeneficio;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Moeda moedaBeneficio;
}
