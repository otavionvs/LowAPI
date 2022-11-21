package weg.com.Low.model.entity;

import javax.persistence.*;

@Entity
public class BusinessUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private int idBussinessUnit;
    @Column(nullable = false)
    private String nome;
}
