package weg.com.Low.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
    @NotNull
    private List<String> coresPrimariasReuniaoPersonalizacao;
    @NotNull
    private List<String> coresSecundariasReuniaoPersonalizacao;
    private boolean ativaPersonalizacao;
}
