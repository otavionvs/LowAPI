package weg.com.Low.model.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import weg.com.Low.model.enums.Comissao;
import weg.com.Low.model.enums.StatusReuniao;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "reuniao")
@Data
public class Reuniao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoReuniao;
    @Column(nullable = false)
    private Date dataReuniao;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusReuniao statusReuniao;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Comissao comissaoReuniao;
    @OneToOne
    @JoinColumn(name = "ata_publicada")
    private Ata ataPublicadaReuniao;

    @Column
    private String numAtaDG;

    @Column(columnDefinition = "longtext")
    private String motivoCancelamentoReuniao;

    @OneToOne
    @JoinColumn(name = "ata_nao_publicada")
    private Ata ataNaoPublicadaReuniao;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "proposta_reuniao", joinColumns =
    @JoinColumn(name = "codigo_reuniao", nullable = false),
            inverseJoinColumns = {@JoinColumn(name = "codigo_proposta", referencedColumnName = "codigo_demanda", nullable = false),
            @JoinColumn(name = "version", referencedColumnName = "version", nullable = false)})
    private List<Proposta> propostasReuniao;

    @OneToOne()
    @JoinColumn(name = "id_reuniao")
    private Arquivo arquivoReuniao;


}

//@ManyToMany
//@JoinTable(
//        name = "tabela_secundaria",
//        joinColumns = @JoinColumn(name = "entidade_b_id"),
//        inverseJoinColumns = {
//                @JoinColumn(name = "entidade_a_chave1"),
//                @JoinColumn(name = "entidade_a_chave2")
//        }
//)
