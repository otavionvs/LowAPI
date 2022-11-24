package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer codigoUsuario;
    @Column(nullable = false)
    private String nomeUsuario;
    @Column(nullable = false)
    private String userUsuario;
    @Column(nullable = false)
    private String emailUsuario;
//    @Column(nullable = false)
//    private Date data_nascimentoUsuario;
//    @Column(nullable = false, length = 11)
//    private Integer telefoneUsuario;
    @Column(nullable = false)
    private String senhaUsuario;
    @ManyToOne
    @JoinColumn(name = "departamento_codigo", nullable = false)
    private Departamento departamentoUsuario;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NivelAcesso nivelAcessoUsuario;
}
