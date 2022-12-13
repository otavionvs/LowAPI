package weg.com.Low.repository;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.BusinessUnit;
@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Integer> {
    boolean existsBynomeBusinessUnit(String nomeBusinessUnit);
}
