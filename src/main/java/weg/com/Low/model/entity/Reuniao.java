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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    int codigoReuniao;
    @Column(nullable = false)
    Date dataReuniao;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    StatusReuniao statusReuniao;
    @ManyToOne
    @JoinColumn(name = "codigo_comissao")
    private Comissao comissaoReuniao;
    @OneToOne
    @JoinColumn(name = "ata")
    private Ata ataReuniao;
    @ManyToMany
    @JoinTable(name = "proposta_reuniao", joinColumns =
    @JoinColumn(name = "codigo_reuniao", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "codigo_proposta", nullable = false))
    private List<Proposta> propostasReuniao;

}
