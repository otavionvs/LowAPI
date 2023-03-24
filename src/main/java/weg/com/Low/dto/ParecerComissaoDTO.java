package weg.com.Low.dto;

import lombok.Data;
import weg.com.Low.model.enums.DecisaoProposta;

import javax.validation.constraints.NotNull;
@Data
public class ParecerComissaoDTO {
    @NotNull
    private String parecerComissaoProposta;
    @NotNull
    private DecisaoProposta decisaoProposta;
    private String recomendacaoProposta;

}
