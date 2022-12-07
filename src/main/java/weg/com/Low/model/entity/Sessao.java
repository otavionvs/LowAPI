package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "sessao")
public class Sessao {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    int codigoSessao;
    @Column(nullable = false)
    String nomeSessao;
}
