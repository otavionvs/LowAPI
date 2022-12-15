package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.BusinessUnitDTO;
import weg.com.Low.model.entity.BusinessUnit;
import weg.com.Low.model.service.BusinessUnitService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("bu")
public class BusinessUnitController {
    private BusinessUnitService businessUnitService;

    @GetMapping
    public ResponseEntity<List<BusinessUnit>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(businessUnitService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<BusinessUnit> businessUnitOptional = businessUnitService.findById(codigo);
        if (businessUnitOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("businessUnit não encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(businessUnitOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid BusinessUnitDTO businessUnitDTO) {
        if(businessUnitService.existsBynomeBusinessUnit(businessUnitDTO.getNomeBusinessUnit())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este nome já existe, Tente Outro!");
        }
        BusinessUnit businessUnit = new BusinessUnit();
        BeanUtils.copyProperties(businessUnitDTO, businessUnit);
        return ResponseEntity.status(HttpStatus.OK).body(businessUnitService.save(businessUnit));
    }
}
