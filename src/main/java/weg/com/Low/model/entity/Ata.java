package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ata")
@AllArgsConstructor
@Data
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
