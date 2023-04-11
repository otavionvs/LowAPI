package weg.com.Low.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.enums.Status;

import java.util.List;
import java.util.Optional;


@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Integer> {
    Optional<Demanda> findFirstByCodigoDemandaOrderByVersionDesc(Integer codigo);
    List<Demanda> findByCodigoDemanda(Integer codigo);
    boolean existsByCodigoDemanda(Integer codigo);
    Long countAllByCodigoDemanda(Integer codigoDemanda);
    Integer countByVersionIs(Integer versao);



    void deleteFirstByCodigoDemandaOrderByVersionDesc(Integer codigo);

    //É necessario que todas as informações existam para que ele busque
    @Query(value = "select * from demanda " +
            "INNER JOIN usuario u ON demanda.solicitante_demanda = u.codigo_usuario " +
            "INNER JOIN usuario a ON demanda.analista_codigo = u.codigo_usuario " +
            "INNER JOIN departamento de ON u.departamento_codigo = de.codigo_departamento " +
            "WHERE LOWER(demanda.titulo_demanda) like %:tituloDemanda% " +
            "AND LOWER(demanda.codigo_demanda) like %:codigoDemanda% " +
            "AND LOWER(u.nome_usuario) like %:solicitante% " +
            "AND LOWER(demanda.status_demanda) like %:status% " +
            "AND LOWER(demanda.tamanho_demanda_classificada) like %:tamanho% " +
            "AND LOWER(a.nome_usuario) like %:analista% " +
            "AND LOWER(de.nome_departamento) like %:departamento%", nativeQuery = true)
    List<Demanda> search(String tituloDemanda, String solicitante, String codigoDemanda, String status,
                         String tamanho, String analista, String departamento, Pageable page);

    //Para o caso da demanda não ter demanda classificada
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

    //Retorna a última versão de uma demanda de um status
    @Query(value = "SELECT d.* " +
            "FROM demanda d " +
            "INNER JOIN (" +
            "  SELECT codigo_demanda, MAX(version) AS max_version " +
            "  FROM demanda " +
            "  GROUP BY codigo_demanda " +
            ") d2 ON d.codigo_demanda = d2.codigo_demanda AND d.version = d2.max_version " +
            "WHERE d.status_demanda = :status", nativeQuery = true)
    List<Demanda> search(String status, Pageable page);

    //Retorna a última versão de uma demanda de um determinado status,
    //porém somente as do departamento que for repassado abaixo
    //Utilizado no solicitante especialmente
    @Query(value = "SELECT d.* " +
            "FROM demanda d " +
            "INNER JOIN (" +
            "  SELECT codigo_demanda, MAX(version) AS max_version " +
            "  FROM demanda " +
            "  GROUP BY codigo_demanda " +
            ") d2 ON d.codigo_demanda = d2.codigo_demanda AND d.version = d2.max_version " +
            "INNER JOIN usuario u ON d.solicitante_demanda = u.codigo_usuario " +
            "WHERE d.status_demanda = :status AND u.departamento_codigo = :codigoDepartamento ", nativeQuery = true)
    List<Demanda> search(String status, Integer codigoDepartamento, Pageable page);

    @Query(value = "select * from demanda d " +
            "INNER JOIN (" +
            "  SELECT codigo_demanda, MAX(version) AS max_version " +
            "  FROM demanda " +
            "  GROUP BY codigo_demanda " +
            ") d2 ON d.codigo_demanda = d2.codigo_demanda AND d.version = d2.max_version " +
            "WHERE LOWER(d.status_demanda) like %:status1% OR " +
            "LOWER(d.status_demanda) like %:status2%", nativeQuery = true)
    List<Demanda> search(String status1, String status2, Pageable page);

}
