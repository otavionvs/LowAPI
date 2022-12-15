package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Demanda;


@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Integer> {

}
