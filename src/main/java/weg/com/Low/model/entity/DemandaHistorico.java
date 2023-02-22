package weg.com.Low.model.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Campos iguais aos de demanda são mantidos como (nomeDemanda)
 * Campos novos são (nomeDemandaHistorico)
 */

@Entity
@Table(name = "demandaHistorico")
@Data
public class DemandaHistorico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoDemandaHistorico;
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
    private Date dataCriacaoDemandaHistorico = new Date();
    @Column(length = 1000)
    private String motivoReprovacaoDemanda;
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer versaoDemandaHistorico;
    @OneToOne
    @JoinColumn(name = "beneficio_potencial_demanda", nullable = false)
    private Beneficio beneficioPotencialDemanda;
    @OneToOne
    @JoinColumn(name = "beneficio_real_demanda", nullable = false)
    private Beneficio beneficioRealDemanda;
    @OneToOne
    @JoinColumn(name = "solicitante_demanda", nullable = false)
    private Usuario solicitanteDemanda;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "demanda_codigo")
    private Demanda demandaDemandaHistorico;

//    (fetch = FetchType.EAGER)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "centro_custos_demanda_historico", joinColumns =
    @JoinColumn(name = "codigo_demanda_historico", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "codigo_centro_custo_historico", nullable = false))
    private List<CentroCustoHistorico> centroCustosHistorico;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "arquivo_demanda_historico")
    private List<ArquivoHistorico> arquivosDemanda = new ArrayList<>();

//    public void setArquivosHistorico(MultipartFile[] files) {
//        try {
//            for (MultipartFile file : files) {
//                arquivosDemandaHistorico.add(new Arquivo(null,
//                        file.getOriginalFilename(),
//                        file.getContentType(),
//                        file.getBytes()));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


}
