package weg.com.Low.model.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import weg.com.Low.model.enums.Status;

import javax.persistence.*;
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
    @Column(name = "codigo_demanda")
    private Integer codigoDemanda;
    @Id
    @Column(name = "version")
    private Integer version;
    @Column(length = 100)
    private String tituloDemanda;
    @Column(columnDefinition = "longtext")
    private String situacaoAtualDemanda;
    @Column(columnDefinition = "longtext")
    private String objetivoDemanda;
    @Column()
    private String frequenciaDeUsoDemanda;
    @Column()
    @Enumerated(value = EnumType.STRING)
    private Status statusDemanda;
    @Column(columnDefinition = "longtext")
    private String beneficioQualitativoDemanda;
    @Column()
    private Date dataCriacaoDemanda = new Date();
    @Column(columnDefinition = "longtext")
    private String motivoReprovacaoDemanda;
    //Quem atualizou está demanda
    @Column
    private String autor;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "centro_custo_demanda",
        joinColumns = {
                @JoinColumn(name = "codigo_demanda", referencedColumnName = "codigo_demanda"),
                @JoinColumn(name = "version", referencedColumnName = "version")
        },
        inverseJoinColumns = @JoinColumn(name = "codigo_centro_custo"))
    private List<CentroCusto> centroCustosDemanda;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "beneficio_potencial_demanda")
    private Beneficio beneficioPotencialDemanda;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "beneficio_real_demanda")
    private Beneficio beneficioRealDemanda;
    @OneToOne
    @JoinColumn(name = "solicitante_demanda", nullable = false)
    private Usuario solicitanteDemanda;

    @OneToOne
    @JoinColumn(name = "analista_codigo")
    private Usuario analista;
    @OneToOne
    @JoinColumn(name = "gerente_negocio_codigo")
    private Usuario gerenteNegocio;

    @ManyToMany(mappedBy = "demandasFavoritas")
    private List<Usuario> usuariosFavoritos;

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

    public void setArquivosList(List<Arquivo> arquivos){
        for (int i = 0; i< arquivos.size(); i++){
            System.out.println(i);
            System.out.println(arquivos.get(i).getNomeArquivo());;
        };
//            this.arquivosDemanda.addAll(arquivos);
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
