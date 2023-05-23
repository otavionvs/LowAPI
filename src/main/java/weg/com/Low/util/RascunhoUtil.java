package weg.com.Low.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import weg.com.Low.dto.RascunhoDTO;
import weg.com.Low.model.entity.Demanda;

import javax.validation.Valid;

public class RascunhoUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Demanda convertJsonToModel(String rascunhoJson){
        RascunhoDTO rascunhoDTO = convertJsonToDto(rascunhoJson);
        return convertDtoToModel(rascunhoDTO);
    }
    //Usado para fazer uma verificação com o valid
    private RascunhoDTO convertJsonToDto(String rascunhoJson){
        try {
            return this.objectMapper.readValue(rascunhoJson, RascunhoDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private Demanda convertDtoToModel(@Valid RascunhoDTO rascunhoDTO){
        return this.objectMapper.convertValue(rascunhoDTO, Demanda.class);
    }
}
