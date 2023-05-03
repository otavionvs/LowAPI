package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.DemandaClassificada;
import weg.com.Low.model.entity.Usuario;

import java.util.List;

@Repository
public interface DemandaClassificadaRepository extends JpaRepository<DemandaClassificada, Integer> {
//    List<DemandaAnalista> findByAnalista(Usuario analista);
//    DemandaAnalista findByDemandaDemandaAnalista(Demanda demanda);
    List<Demanda> findBySolicitanteDemandaOrAnalista(Usuario solicitanteDemanda, Usuario analista);


}
