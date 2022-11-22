package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ata")
@AllArgsConstructor
public class Ata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column()
    int idAta;
    @Column()
    String tipoAta;
    @Column()
    String tituloAta;
}
