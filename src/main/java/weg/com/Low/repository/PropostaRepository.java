package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weg.com.Low.model.entity.Proposta;
import weg.com.Low.model.entity.Recurso;

public  interface PropostaRepository extends JpaRepository<Proposta, Integer> {
}
