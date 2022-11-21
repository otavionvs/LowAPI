package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "pauta")
@AllArgsConstructor
public class Pauta {
    int idPauta;
    Date dataReuniao;

    @OneToOne
    @JoinColumn(name = "ata")
    private Ata ata;
}
