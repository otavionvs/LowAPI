package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "arquivo")
@Data
public class Arquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int codigoArquivo;
    private String urlArquivo;
}
