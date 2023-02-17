package weg.com.Low.model.entity;

import lombok.Data;
import weg.com.Low.model.enums.Moeda;

import javax.persistence.*;

@Entity
@Table(name = "beneficio")
@Data
public class Beneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer codigoBeneficio;
    @Column(nullable = false, length = 3000)
    private String memoriaDeCalculoBeneficio;
    @Column(nullable = false)
    private Double valorBeneficio;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Moeda moedaBeneficio;
}
