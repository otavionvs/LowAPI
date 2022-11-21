package weg.com.Low.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
public class DemandaAnalista extends Demanda{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    int codigoDemandaAnalista;
    @Column(nullable = false)
    String tamanho;

    @ManyToOne
    @JoinColumn(name = "codigo_")
    private BusinessUnit businessUnit1;

    @ManyToOne
    @JoinColumn(name = "codigo_business_unit2")
    private BusinessUnit businessUnit21;

}
