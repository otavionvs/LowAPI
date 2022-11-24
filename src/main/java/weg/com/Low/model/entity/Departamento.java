package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "departamento")
@Data
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer codigoDepartamento;
    @Column(nullable = false)
    private String nome;
}
