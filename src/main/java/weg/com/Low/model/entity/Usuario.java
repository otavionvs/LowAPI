package weg.com.Low.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import weg.com.Low.model.enums.NivelAcesso;

import javax.persistence.*;

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
    @Column(nullable = false, length = 150)
    @JsonIgnore
    private String senhaUsuario;
    @Column
    private Boolean primeiroAcesso = false;
    @ManyToOne
    @JoinColumn(name = "departamento_codigo", nullable = false)
    private Departamento departamentoUsuario;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NivelAcesso nivelAcessoUsuario;

}
