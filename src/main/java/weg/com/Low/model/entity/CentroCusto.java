package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "centro_custo")
@Data
@AllArgsConstructor @NoArgsConstructor
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
