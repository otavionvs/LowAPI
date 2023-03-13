package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "secao")
public class Secao {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int codigoSecao;
    @Column(nullable = false, length = 100)
    String nomeSecao;
}
