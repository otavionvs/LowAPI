package weg.com.Low.controller;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import weg.com.Low.model.entity.BusinessUnit;
import weg.com.Low.model.service.BusinessUnitService;

import java.util.List;

public class BusinessUnitController {
    private BusinessUnitService businessUnitService;
    @GetMapping
    public ResponseEntity<List<BusinessUnit>> findAll() {
        return ResponseEntity.status(200).body(businessUnitService.findAll());
    }

    @PostMapping
    public <S extends BusinessUnit> S save(S entity) {
        return businessUnitService.save(entity);
    }

    public boolean existsById(Integer integer) {
        return businessUnitService.existsById(integer);
    }

    public void deleteById(Integer integer) {
        businessUnitService.deleteById(integer);
    }

    public void delete(BusinessUnit entity) {
        businessUnitService.delete(entity);
    }
}
