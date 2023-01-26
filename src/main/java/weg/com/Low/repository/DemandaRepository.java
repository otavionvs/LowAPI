package weg.com.Low.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Status;
import weg.com.Low.model.entity.Usuario;


@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Integer> {

    @Query(value = "select * from demanda d " +
            "INNER JOIN usuario u ON d.solicitante_demanda = u.codigo_usuario " +
            "INNER JOIN demanda_analista da ON d.codigo_demanda = da.demanda_codigo " +
            "WHERE LOWER(d.titulo_demanda) like %:tituloDemanda% " +
            "AND LOWER(d.codigo_demanda) like %:codigoDemanda% " +
            "AND LOWER(u.nome_usuario) like %:solicitante% " +
            "AND LOWER(d.status_demanda) like %:status% " +
            "AND LOWER(da.tamanho_demanda_analista) like %:tamanho% ", nativeQuery = true)
    Page<Demanda> search(String tituloDemanda, String solicitante, String codigoDemanda, String status, String tamanho, Pageable pageable);

    @Query(value = "select * from demanda d " +
            "WHERE LOWER(d.status_demanda) like %:status% ", nativeQuery = true)
    Page<Demanda> search(String status, Pageable pageable);

}
