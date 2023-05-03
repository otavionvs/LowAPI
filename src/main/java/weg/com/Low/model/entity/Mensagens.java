package weg.com.Low.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import weg.com.Low.model.enums.StatusMensagens;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mensagens")
@Data
public class Mensagens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoMensagens;
    @Column(nullable = false)
    private String textoMensagens;
    @ManyToOne
    @JoinColumn(name = "codigo_usuario", nullable = false)
    private Usuario usuarioMensagens;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "codigo_demanda", referencedColumnName = "codigo_demanda"),
            @JoinColumn(name = "version", referencedColumnName = "version")})
    private Demanda demandaMensagens;
    @Column
    private Date dataMensagens=new Date();

    @Column
    private StatusMensagens statusMensagens;
}
