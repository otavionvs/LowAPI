package weg.com.Low.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Status;
import weg.com.Low.model.entity.Usuario;

import java.util.List;


@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Integer> {

    //É necessario que todas as informações existam para que ele busque
    @Query(value = "select * from demanda d " +
            "INNER JOIN usuario u ON d.solicitante_demanda = u.codigo_usuario " +
            "INNER JOIN demanda_analista da ON d.codigo_demanda = da.demanda_codigo " +
            "WHERE LOWER(d.titulo_demanda) like %:tituloDemanda% " +
            "AND LOWER(d.codigo_demanda) like %:codigoDemanda% " +
            "AND LOWER(u.nome_usuario) like %:solicitante% " +
            "AND LOWER(d.status_demanda) like %:status% " +
            "AND LOWER(da.tamanho_demanda_analista) like %:tamanho% order by :sort LIMIT :size OFFSET :page ", nativeQuery = true)
    List<Demanda> search(String tituloDemanda, String solicitante, String codigoDemanda, String status,
                         String tamanho, Long page, int size, String sort);

    //Para o caso da demanda não ter demanda analista
    @Query(value = "select * from demanda d " +
            "INNER JOIN usuario u ON d.solicitante_demanda = u.codigo_usuario " +
            "WHERE LOWER(d.titulo_demanda) like %:tituloDemanda% " +
            "AND LOWER(d.codigo_demanda) like %:codigoDemanda% " +
            "AND LOWER(u.nome_usuario) like %:solicitante% " +
            "AND LOWER(d.status_demanda) like %:status% order by :sort LIMIT :size OFFSET :page ", nativeQuery = true)
    List<Demanda> search(String tituloDemanda, String solicitante, String codigoDemanda,
                         String status, Long page, int size, String sort);

    @Query(value = "select * from demanda d " +
            "WHERE LOWER(d.status_demanda) like %:status% LIMIT :size OFFSET :page", nativeQuery = true)
    List<Demanda> search(String status, Long page, int size);

}
