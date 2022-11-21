package weg.com.Low.model.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column()
    private int codigoProposta;

    @Column()
    private Date prazoProposta;

    @Column()
    private int codigoPPMProposta;

    @Column()
    private String jiraProposta;

    @Column()
    private Date PeriodoExeDemandaInicioProposta;

    @Column()
    private Date PeriodoExeDemandaFimProposta;

    @Column()
    private double paybackProposta;

    @Column()
    private Usuario responsavelProposta;

    @Column()
    private String areaResponsavelProposta;

}
