package weg.com.Low.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import weg.com.Low.dto.PropostaDTO;
import weg.com.Low.model.entity.Proposta;

import javax.validation.Valid;

public class PropostaUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Proposta convertJsonToModel(String propostaJson){
        PropostaDTO propostaDTO = convertJsonToDto(propostaJson);
        return convertDtoToModel(propostaDTO);
    }
    //Usado para fazer uma verificação com o valid
    private PropostaDTO convertJsonToDto(String propostaJson){
        try {
            return this.objectMapper.readValue(propostaJson, PropostaDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private Proposta convertDtoToModel(@Valid PropostaDTO propostaDTO){
        return this.objectMapper.convertValue(propostaDTO, Proposta.class);
    }
}
