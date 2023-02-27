package weg.com.Low.model.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import weg.com.Low.model.enums.Status;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Version;

@Entity
@Table(name = "demanda")
@Data
//@IdClass(DemandaId.class)
public class Demanda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoDemanda;
//    @EmbeddedId
//    private DemandaId codigoDemanda;
    @Column
    private Integer version;
    @Column(nullable = false, length = 100)
    private String tituloDemanda;
    @Column(nullable = false, length = 1000)
    private String situacaoAtualDemanda;
    @Column(nullable = false, length = 1000)
    private String objetivoDemanda;
    @Column(nullable = false)
    private String frequenciaDeUsoDemanda;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status statusDemanda;
    @Column(nullable = false, length = 1000)
    private String beneficioQualitativoDemanda;
    @Column()
    private Date dataCriacaoDemanda = new Date();
    @Column(length = 1000)
    private String motivoReprovacaoDemanda;
    @OneToOne
    @JoinColumn(name = "beneficio_potencial_demanda", nullable = false)
    private Beneficio beneficioPotencialDemanda;
    @OneToOne
    @JoinColumn(name = "beneficio_real_demanda", nullable = false)
    private Beneficio beneficioRealDemanda;
    @OneToOne
    @JoinColumn(name = "solicitante_demanda", nullable = false)
    private Usuario solicitanteDemanda;
    //    @OneToOne
//    @JoinColumn(name = "conversa_demanda", nullable = false)
//    private Conversa conversaDemanda;
    @ManyToMany
    @JoinTable(name = "centro_custos_demanda", joinColumns =
    @JoinColumn(name = "codigo_demanda", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "codigo_centro_custo", nullable = false))
    private List<CentroCusto> centroCustos;
    //    @ManyToMany
//    @JoinTable(name = "historico_demanda", joinColumns =
//    @JoinColumn(name = "codigo_demanda", nullable = false),
//            inverseJoinColumns = @JoinColumn(name = "codigo_historico", nullable = false))
//    private List<Historico> historicos;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "arquivo_demanda")
    private List<Arquivo> arquivosDemanda = new ArrayList<>();

    public void setArquivos(MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                arquivosDemanda.add(new Arquivo(null,
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
