package weg.com.Low.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Table(name = "centro_custo")
@Data
public class CentroCusto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column()
    private Integer codigoCentroCusto;
    @Column()
    private String nome;

}
