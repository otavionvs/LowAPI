package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Moeda {
    Real("Real"), //0
    Dollar("Dollar"), //1
    Euro("Euro"), //2
    Libra("Libra"); //3

    String moeda;
}
