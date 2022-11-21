package weg.com.Low.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "sessao")
public class Sessao {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    int codigoSessao;
    @Column(nullable = false)
    String nome;
}
