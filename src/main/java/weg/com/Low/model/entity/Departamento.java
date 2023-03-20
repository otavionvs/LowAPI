package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
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
