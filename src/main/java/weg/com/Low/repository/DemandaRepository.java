package weg.com.Low.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Usuario;


@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Integer> {

    @Query(value = "select * from demanda d " +
            "WHERE LOWER(d.tituloDemanda) like %:tituloDemanda% " +
            "AND LOWER(d.codigoDemanda) like %:codigoDemanda% " +
            "AND LOWER(d.tituloDemanda) like %:usuario% " +
            "AND LOWER(d.departamento_codigo) like %:departamento%", nativeQuery = true)
    Page<Demanda> search(String tituloDemanda, String email, String usuario, Integer codigoDemanda, Pageable pageable);
}
