package weg.com.Low.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Demanda;

import java.util.List;
import java.util.Optional;


@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Integer> {
    Optional<Demanda> findFirstByCodigoDemandaOrderByVersionDesc(Integer codigo);
    List<Demanda> findByCodigoDemanda(Integer codigo);
    boolean existsByCodigoDemanda(Integer codigo);
    Long countAllByCodigoDemanda(Integer codigoDemanda);
    //É necessario que todas as informações existam para que ele busque
    @Query(value = "select * from demanda " +
            "INNER JOIN usuario u ON demanda.solicitante_demanda = u.codigo_usuario " +
            "INNER JOIN demanda_analista da ON demanda.codigo_demanda = da.demanda_codigo " +
            "INNER JOIN usuario a ON da.analista_codigo = u.codigo_usuario " +
            "INNER JOIN departamento de ON u.departamento_codigo = de.codigo_departamento " +
            "WHERE LOWER(demanda.titulo_demanda) like %:tituloDemanda% " +
            "AND LOWER(demanda.codigo_demanda) like %:codigoDemanda% " +
            "AND LOWER(u.nome_usuario) like %:solicitante% " +
            "AND LOWER(demanda.status_demanda) like %:status% " +
            "AND LOWER(da.tamanho_demanda_analista) like %:tamanho% " +
            "AND LOWER(a.nome_usuario) like %:analista% " +
            "AND LOWER(de.nome_departamento) like %:departamento%", nativeQuery = true)
    List<Demanda> search(String tituloDemanda, String solicitante, String codigoDemanda, String status,
                         String tamanho, String analista, String departamento, Pageable page);

    //Para o caso da demanda não ter demanda analista
    @Query(value = "select * from demanda " +
            "INNER JOIN usuario u ON demanda.solicitante_demanda = u.codigo_usuario " +
            "INNER JOIN departamento de ON u.departamento_codigo = de.codigo_departamento " +
            "WHERE LOWER(demanda.titulo_demanda) like %:tituloDemanda% " +
            "AND LOWER(demanda.codigo_demanda) like %:codigoDemanda% " +
            "AND LOWER(u.nome_usuario) like %:solicitante% " +
            "AND LOWER(demanda.status_demanda) like %:status% " +
            "AND LOWER(de.nome_departamento) like %:departamento%", nativeQuery = true)
    List<Demanda> search(String tituloDemanda, String solicitante, String codigoDemanda,
                         String status, String departamento, Pageable page);

    @Query(value = "select * from demanda " +
            "WHERE LOWER(demanda.status_demanda) like %:status%", nativeQuery = true)
    List<Demanda> search(String status, Pageable page);

    @Query(value = "select * from demanda " +
            "WHERE LOWER(demanda.status_demanda) like %:status1% OR " +
            "LOWER(demanda.status_demanda) like %:status2%", nativeQuery = true)
    List<Demanda> search(String status1, String status2, Pageable page);

}
