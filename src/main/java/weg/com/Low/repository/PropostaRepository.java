package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Proposta;
import weg.com.Low.model.entity.Recurso;
@Repository
public  interface PropostaRepository extends JpaRepository<Proposta, Integer> {
}
