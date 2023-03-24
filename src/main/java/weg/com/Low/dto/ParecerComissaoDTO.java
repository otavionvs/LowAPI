package weg.com.Low.dto;

import lombok.Data;
import weg.com.Low.model.enums.DecisaoProposta;
import weg.com.Low.model.enums.TipoAtaProposta;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
@Data
public class ParecerComissaoDTO {
    @NotNull
    private String parecerComissaoProposta;
    @NotNull
    private DecisaoProposta decisaoProposta;
    @NotNull
    private TipoAtaProposta tipoAtaProposta;
    private String recomendacaoProposta;

}
