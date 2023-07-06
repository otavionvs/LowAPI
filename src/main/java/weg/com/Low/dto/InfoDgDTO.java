package weg.com.Low.dto;

import lombok.Data;
import weg.com.Low.model.entity.Arquivo;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
@Data
public class InfoDgDTO {
    private String decisaoDG;
    private String parecerDGProposta;
    private String numAtaDG;
    private String recomendacaoDGProposta;
}
