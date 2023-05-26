package weg.com.Low.model.entity;

import lombok.Data;
import weg.com.Low.model.enums.Moeda;

import javax.persistence.*;

@Entity
@Table(name = "beneficio")
@Data
public class Beneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoBeneficio;
    @Column(columnDefinition = "longtext")
    private String memoriaDeCalculoBeneficio;
    @Column
    private Double valorBeneficio;
    @Column
    @Enumerated(value = EnumType.STRING)
    private Moeda moedaBeneficio;
}
