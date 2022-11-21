package weg.com.Low.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Arquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int codigoArquivo;
    private String urlArquivo;
}
