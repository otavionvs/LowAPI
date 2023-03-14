package weg.com.Low.dto;

import lombok.Getter;
import weg.com.Low.model.entity.CentroCusto;
import weg.com.Low.model.enums.PerfilDespesa;
import weg.com.Low.model.enums.TipoDespesa;

import javax.validation.constraints.*;
import java.util.List;

@Getter
public class RecursoDTO {
    @NotBlank
    private String nomeRecurso;
    @NotNull
    @Positive
    private Integer quantidadeHorasRecurso;
    @NotNull
    @PositiveOrZero
    private Double valorHoraRecurso;
    @NotNull
    private TipoDespesa tipoDespesaRecurso;
    @NotNull
    private PerfilDespesa perfilDespesaRecurso;
    @NotNull
    private Integer periodoExMesesRecurso;



    @NotEmpty
    private List<CentroCusto> centroDeCustoRecurso;
}
