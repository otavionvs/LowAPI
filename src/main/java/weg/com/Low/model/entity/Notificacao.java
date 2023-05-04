package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import weg.com.Low.model.enums.StatusNotificacao;
import weg.com.Low.model.enums.TipoNotificacao;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "notificacao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoNotificacao;
    @Column(nullable = false)
    private String tituloDemandaNotificacao;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TipoNotificacao tipoNotificacao;
    @Column(nullable = false)
    private String descricaoNotificacao;
    @Column
    private Date dataNotificacao = new Date();
    @Column
    private Boolean lido = false;
    @ManyToMany
    @JoinTable(name = "usuario_notificacoes", joinColumns =
    @JoinColumn(name = "codigo_notificacao", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "codigo_usuario", nullable = false))
    private List<Usuario> usuariosNotificacao;

}
