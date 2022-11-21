package weg.com.Low.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "business_unit")
public class BusinessUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private int idBussinessUnit;
    @Column(nullable = false)
    private String nome;
}
