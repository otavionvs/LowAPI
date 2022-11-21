package weg.com.Low.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "arquivo")
public class Arquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int codigoArquivo;
    private String urlArquivo;
}
