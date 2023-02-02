package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.DemandaAnalista;
import weg.com.Low.model.entity.Usuario;

import java.util.List;

@Repository
public interface DemandaAnalistaRepository extends JpaRepository<DemandaAnalista, Integer> {
    List<DemandaAnalista> findByAnalista(Usuario analista);
}
