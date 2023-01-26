package weg.com.Low.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import weg.com.Low.dto.DemandaDTO;
import weg.com.Low.model.entity.Demanda;

import javax.validation.Valid;

public class DemandaUtil {
    private ObjectMapper objectMapper = new ObjectMapper();
    public Demanda convertJsonToModel(String demandaJson){
        DemandaDTO demandaDTO = convertJsonToDto(demandaJson);
        return convertDtoToModel(demandaDTO);
    }

    private DemandaDTO convertJsonToDto(String demandaJson){
        try {
            return this.objectMapper.readValue(demandaJson, DemandaDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private Demanda convertDtoToModel(@Valid DemandaDTO demandaDTO){
        return this.objectMapper.convertValue(demandaDTO, Demanda.class);
    }
}
