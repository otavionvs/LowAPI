package weg.com.Low.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
@Data
public class PersonalizacaoDTO {
    @NotNull
    private String nomePersonalizacao;
    private Integer codigoPersonalizacao;
    @NotNull
    private List<String> coresPrimariasPersonalizacao;
    @NotNull
    private List<String> coresSecundariasPersonalizacao;
    private boolean ativaPersonalizacao;
}
