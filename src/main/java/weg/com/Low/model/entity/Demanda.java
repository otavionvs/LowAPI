package weg.com.Low.model.entity;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.web.multipart.MultipartFile;
import weg.com.Low.dto.CentroCustoDTO;
import weg.com.Low.model.enums.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "demanda")
@Data
@IdClass(DemandaId.class)
public class Demanda{
    //O BD não suporta o Identity nesse caso
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_demanda")
    private Integer codigoDemanda;
    @Id
    @Column(name = "version")
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
    @Column(length = 1000)
    private String beneficioQualitativoDemanda;
    @Column()
    private Date dataCriacaoDemanda = new Date();
    @Column(length = 1000)
    private String motivoReprovacaoDemanda;
    @Column(length = 6)
    private Integer score;

//    @Column
//    @ElementCollection
//    @OneToMany(cascade = CascadeType.ALL)
    @ManyToMany
    @JoinTable(
        name = "centro_custo_demanda",
        joinColumns = {
                @JoinColumn(name = "codigo_demanda", referencedColumnName = "codigo_demanda"),
                @JoinColumn(name = "version", referencedColumnName = "version")
        },
        inverseJoinColumns = @JoinColumn(name = "codigo_centro_custo"))
    private List<CentroCusto> centroCustosDemanda;
    @OneToOne
    @JoinColumn(name = "beneficio_potencial_demanda")
    private Beneficio beneficioPotencialDemanda;
    @OneToOne
    @JoinColumn(name = "beneficio_real_demanda")
    private Beneficio beneficioRealDemanda;
    @OneToOne
    @JoinColumn(name = "solicitante_demanda", nullable = false)
    private Usuario solicitanteDemanda;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "arquivo_demanda",
            joinColumns = {
                    @JoinColumn(name = "codigo_demanda", referencedColumnName = "codigo_demanda"),
                    @JoinColumn(name = "version", referencedColumnName = "version")
            },
            inverseJoinColumns = @JoinColumn(name = "codigo_arquivo")
    )
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

    public void setArquivosClassificada(List<Arquivo> arquivos){
        arquivosDemanda = new ArrayList<>();
        try {
            for (Arquivo arquivo : arquivos) {
                arquivosDemanda.add(new Arquivo(null,
                        arquivo.getNomeArquivo(),
                        arquivo.getTipoArquivo(),
                        arquivo.getDadosArquivo()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
