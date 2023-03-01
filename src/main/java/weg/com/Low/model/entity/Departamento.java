package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "departamento")
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoDepartamento;
    @Column(nullable = false, length = 100)
    private String nomeDepartamento;
}
