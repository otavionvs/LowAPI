package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "reuniao")
@Data
public class Reuniao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer codigoReuniao;
    @Column(nullable = false)
    private Date dataReuniao;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusReuniao statusReuniao;
    @ManyToOne
    @JoinColumn(name = "codigo_comissao", nullable = false)
    private Comissao comissaoReuniao;
    @OneToOne
    @JoinColumn(name = "ata")
    private Ata ataReuniao;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "proposta_reuniao", joinColumns =
    @JoinColumn(name = "codigo_reuniao", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "codigo_proposta", nullable = false))
    private List<Proposta> propostasReuniao;

}
