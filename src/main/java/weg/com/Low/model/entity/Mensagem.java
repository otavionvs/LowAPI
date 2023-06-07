package weg.com.Low.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import weg.com.Low.model.enums.StatusMensagens;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mensagens")
@Data
public class Mensagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoMensagem;
    @Column(nullable = false)
    private String textoMensagem;
    @ManyToOne
    @JoinColumn(name = "codigo_usuario", nullable = false)
    private Usuario usuarioMensagem;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "codigo_conversa")
    private Conversa conversa;

    @Column
    private Date dataMensagem=new Date();

    @Column
    private StatusMensagens statusMensagem;
}
