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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Integer codigoAta;
    @Column()
    String anoAta;

    //Tem também a data da reunião, mas isso está em Reuniao
    @Column(nullable = false, length = 100)
    String tituloAta;

//    @Column
    


}
