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
import java.util.List;

@Entity
@Table(name = "notificacao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer codigoNotificacao;
    @Column
    private String tituloDemandaNotificacao;
    @Column
    private Integer codigo;
    @Column
    private TipoNotificacao tipoNotificacao;
    @Column
    private String descricaoNotificacao;
    @Column
    private LocalDateTime horaNotificacao;
    @Column
    private LocalDate dataNotificacao;
    @Column
    private StatusNotificacao statusNotificacao;
    @ManyToMany
    @JoinTable(name = "usuario_notificacoes", joinColumns =
    @JoinColumn(name = "codigo_notificacao", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "codigo_usuario", nullable = false))
    private List<Usuario> usuariosNotificacao;

}
