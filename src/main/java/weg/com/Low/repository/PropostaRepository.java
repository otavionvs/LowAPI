package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Proposta;
import weg.com.Low.model.entity.Usuario;

import java.util.List;

@Repository
public  interface PropostaRepository extends JpaRepository<Proposta, Integer> {
    //Retorna a proposta que possuir aquela demanda
    @Query(value = "select * from proposta p " +
            "INNER JOIN demanda_analista da ON p.codigo_demanda_analista = da.codigo_demanda_analista " +
            "INNER JOIN demanda d ON da.demanda_codigo = d.codigo_demanda " +
            "WHERE LOWER(d.codigo_demanda) like :codigoDemanda ", nativeQuery = true)
    Proposta porDemanda(String codigoDemanda);

    List<Proposta> findAllBySolicitanteDemandaOrAnalista(Usuario usuario);

}
