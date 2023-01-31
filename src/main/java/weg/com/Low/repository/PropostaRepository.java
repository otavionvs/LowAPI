package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.DemandaAnalista;
import weg.com.Low.model.entity.Proposta;
@Repository
public  interface PropostaRepository extends JpaRepository<Proposta, Integer> {
    Proposta findByDemandaAnalistaProposta(DemandaAnalista demandaAnalista);
}
