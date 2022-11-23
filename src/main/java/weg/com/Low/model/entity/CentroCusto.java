package weg.com.Low.model.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "centro_custo")
@Data
public class CentroCusto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer codigoCentroCusto;
    @Column(nullable = false)
    private String nome;
}
