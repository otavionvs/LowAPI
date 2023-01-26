package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "arquivo")
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Arquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigoArquivo;
    private String nomeArquivo;
    private String tipoArquivo;
    @Lob
    private byte[] dadosArquivo;

}
