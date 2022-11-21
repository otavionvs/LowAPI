package weg.com.Low.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;
import java.util.List;
@Entity
public class DemandaAnalista extends Demanda{
    @Column
    int codigoDemandaAnalista;
    String tamanho;
}
