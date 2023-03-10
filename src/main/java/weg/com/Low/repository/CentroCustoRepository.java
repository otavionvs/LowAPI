package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.CentroCusto;

@Repository
public interface CentroCustoRepository  extends JpaRepository<CentroCusto, Integer> {
    boolean existsByNomeCentroCusto(String nome);
}
