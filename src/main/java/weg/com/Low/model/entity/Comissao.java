package weg.com.Low.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "comissao")
public class Comissao {
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int codigoComissao;

    @Column
    private String nome;
}
