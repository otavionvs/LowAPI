package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

//@Embeddable
@Data
public class DemandaId implements Serializable {

    private Integer codigoDemanda;

    private Integer version;
}
