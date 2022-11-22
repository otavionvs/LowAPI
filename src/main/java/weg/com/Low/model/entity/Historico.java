package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "hitorico")
@Data
public class Historico {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer codigoHistorico;
    @Column(nullable = false)
    private Date nome;
}
