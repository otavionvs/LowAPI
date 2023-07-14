package weg.com.Low.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import weg.com.Low.model.enums.NivelAcesso;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoUsuario;
    @Column(nullable = false, length = 100)
    private String nomeUsuario;
    @Column(nullable = false, length = 20, unique = true)
    private String userUsuario;
    @Column(nullable = false, unique = true)
    private String emailUsuario;
//    @Column(nullable = false)
//    private Date data_nascimentoUsuario;
//    @Column(nullable = false, length = 11)
//    private Integer telefoneUsuario;
    @Column(nullable = false, length = 150)
    @JsonIgnore
    private String senhaUsuario;
    @ManyToOne
    @JoinColumn(name = "departamento_codigo", nullable = false)
    private Departamento departamentoUsuario;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NivelAcesso nivelAcessoUsuario;
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "demandas_favoritas",
            joinColumns = {
                    @JoinColumn(name = "codigo_usuario"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "codigo_demanda", referencedColumnName = "codigo_demanda"),
                    @JoinColumn(name = "version", referencedColumnName = "version")

            })
    private List<Demanda> demandasFavoritas;

}
