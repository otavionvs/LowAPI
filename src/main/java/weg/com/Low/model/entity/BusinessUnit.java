package weg.com.Low.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "business_unit")
@Data
public class BusinessUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int codigoBusinessUnit;
    @Column(nullable = false)
    private String nomeBusinessUnit;
}
