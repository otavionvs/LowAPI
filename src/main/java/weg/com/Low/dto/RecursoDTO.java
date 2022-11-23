package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.CentroCusto;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

@Getter
public class RecursoDTO {
    private Integer codigoRecurso;
    private  Integer quantidadeHorasRecurso;
    private Double valorHoraRecurso;
    private String nomeRecurso;
    private String tipoDespesaRecurso;
    private  String perfilDespesa;
    private String periodoExecucaoRecurso;
    private List<CentroCusto> centroDeCustoRecurso;
}
