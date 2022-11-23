package weg.com.Low.model.service;

import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.BusinessUnit;
import weg.com.Low.repository.BusinessUnitRepository;

import java.util.List;

@Service
public class BusinessUnitService {
    private BusinessUnitRepository businessUnitRepository;

    public List<BusinessUnit> findAll() {
        return businessUnitRepository.findAll();
    }

    public <S extends BusinessUnit> S save(S entity) {
        return businessUnitRepository.save(entity);
    }

    public boolean existsById(Integer integer) {
        return businessUnitRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        businessUnitRepository.deleteById(integer);
    }

    public void delete(BusinessUnit entity) {
        businessUnitRepository.delete(entity);
    }
}
