package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "conversa")
@Data
public class Conversa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoConversa;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "mensagens_Conversa", joinColumns =
    @JoinColumn(name = "codigo_conversa", nullable = false),
    inverseJoinColumns = @JoinColumn(name = "codigo_mensagem", nullable = false))
    private List<Mensagens> mensagens;


}
