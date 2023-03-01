package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "arquivoHistorico")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArquivoHistorico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigoArquivo;
    private String nomeArquivo;
    private String tipoArquivo;
    @Lob
    private byte[] dadosArquivo;
}
