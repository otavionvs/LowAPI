package weg.com.Low.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "conversa")
@Data
public class Conversa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoConversa;

    @JsonIgnore
    @OneToMany(mappedBy = "conversa")
    private List<Mensagem> mensagemConversa;

    @ManyToMany
    @JoinTable(name = "usuarios_conversa", joinColumns =
    @JoinColumn(name = "codigo_conversa", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "codigo_usuario", nullable = false))
    private List<Usuario> usuariosConversa;
    @OneToOne
    private Demanda demandaConversa;
    @Column
    private boolean conversaAtiva;

    @Column
    private Integer qtdMensagensNaoLidas;
    @Column
    private Date horaUltimaMensagem;

    @JoinColumn
    @OneToOne
    private Usuario usuarioAguardando;

}
