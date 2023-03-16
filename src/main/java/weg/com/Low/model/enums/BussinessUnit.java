package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BussinessUnit {
    WMOI("WEG Motores Industrial"), //0
    WMOC("WEG Motores Comercial"), //1
    WEN("WEG Energia"), //2
    WAU("WEG Automação"), //3
    WDS("WEG Digital e Sistemas"), //4
    WDC("WEG Drives e Controls"), //5
    WTI("WEG Tintas"), //6
    WTD("WEG Transmissão e Distribuição");//7

    String bussinessUnit;
}
