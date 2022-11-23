package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "pauta")
@AllArgsConstructor
@Data
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    int codigoPauta;
    @Column(nullable = false)
    Date dataReuniao;

    @OneToOne
    @JoinColumn(name = "ata")
    private Ata ataPauta;

    @ManyToOne
    @JoinColumn(name = "codigo_comissao")
    private Comissao comissaoPauta;

}
