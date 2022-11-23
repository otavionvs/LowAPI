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
    private int codigoComissao;

    @Column
    private String nome;
}
