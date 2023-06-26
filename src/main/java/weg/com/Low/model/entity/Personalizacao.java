package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Personalizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoPersonalizacao;
    @Column
    @ElementCollection
    private List<String> coresPrimariasPersonalizacao;
    @Column
    @ElementCollection
    private List<String> coresSecundariasPersonalizacao;
    @Column
    @ElementCollection
    private List<String> coresPrimariasReuniaoPersonalizacao;
    @Column
    @ElementCollection
    private List<String> coresSecundariasReuniaoPersonalizacao;
    @Column
    private String nomePersonalizacao;
    @Column
    private boolean ativaPersonalizacao;
}
