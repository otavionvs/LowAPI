package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.Arquivo;
import weg.com.Low.model.entity.Beneficio;
import weg.com.Low.model.entity.CentroCusto;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.enums.Status;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class DemandaCompletaDTO {
    private Integer codigoDemanda;
    private String tituloDemanda;
    private String situacaoAtualDemanda;
    private String objetivoDemanda;
    private String frequenciaDeUsoDemanda;
    private Status statusDemanda;
    private String beneficioQualitativoDemanda;
    private Date dataCriacaoDemanda;
    private String motivoReprovacaoDemanda;
    private Integer score;
    private List<CentroCusto> centroCustos;
    private Beneficio beneficioPotencialDemanda;
    private Beneficio beneficioRealDemanda;
    private Usuario solicitanteDemanda;

}
