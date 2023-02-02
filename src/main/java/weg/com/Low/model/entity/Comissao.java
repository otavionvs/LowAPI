package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "comissao")
public class Comissao {
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer codigoComissao;

    @Column(nullable = false, length = 100)
    private String nomeComissao;
}
