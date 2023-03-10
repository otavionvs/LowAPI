package weg.com.Low.model.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "centro_custo")
@Data
public class CentroCusto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoCentroCusto;
    @Column(nullable = false, length = 100)
    private String nomeCentroCusto;

    @Column
    private Integer porcentagemCentroCusto;
}
