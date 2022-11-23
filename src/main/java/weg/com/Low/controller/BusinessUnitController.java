package weg.com.Low.controller;

import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.BusinessUnitDTO;
import weg.com.Low.model.entity.BusinessUnit;
import weg.com.Low.model.entity.CentroCusto;
import weg.com.Low.model.service.BusinessUnitService;

import javax.validation.Valid;
import java.util.List;
@Controller
@RequestMapping("/low-api/business-unit")
public class BusinessUnitController {
    private BusinessUnitService businessUnitService;
    @GetMapping
    public ResponseEntity<List<BusinessUnit>> findAll() {
        return ResponseEntity.status(200).body(businessUnitService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(
            @RequestBody @Valid BusinessUnitDTO businessUnitDTO
    ) {

        if(businessUnitService.existsById(businessUnitDTO.getIdBussinessUnit())){
            return ResponseEntity.status(401).body("Código já existe");
        }

        BusinessUnit bu = new BusinessUnit();
        BeanUtils.copyProperties(businessUnitDTO, bu);

        return ResponseEntity.status(200).body(businessUnitService.findAll());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(value = "id") Integer id) {
        if (!businessUnitService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nâo foi encontrado!");
        }
        businessUnitService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Deletado com sucesso!");
    }

    public void delete(BusinessUnit entity) {
        businessUnitService.delete(entity);
    }
}
