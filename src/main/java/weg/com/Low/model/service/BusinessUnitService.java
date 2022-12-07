package weg.com.Low.model.service;

import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.BusinessUnit;
import weg.com.Low.repository.BusinessUnitRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessUnitService {
    private BusinessUnitRepository businessUnitRepository;

    public List<BusinessUnit> findAll() {
        return businessUnitRepository.findAll();
    }

    public Optional<BusinessUnit> findById(Integer codigo) {
        return businessUnitRepository.findById(codigo);
    }

    public BusinessUnit save(BusinessUnit entity) {
        return businessUnitRepository.save(entity);
    }

    public boolean existsBynomeBusinessUnit(String nomeBusinessUnit) {
        return businessUnitRepository.existsBynomeBusinessUnit(nomeBusinessUnit);
    }
}
