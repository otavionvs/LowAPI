package weg.com.Low.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Reuniao;
import weg.com.Low.model.enums.StatusReuniao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ReuniaoRepository extends JpaRepository<Reuniao, Integer> {

    @Query(value = "select * from reuniao " +
            "INNER JOIN proposta_reuniao pr ON reuniao.codigo_reuniao = pr.codigo_reuniao " +
            "INNER JOIN demanda d ON pr.codigo_proposta = d.codigo_demanda " +
            "INNER JOIN usuario a ON d.analista_codigo = a.codigo_usuario " +
            "INNER JOIN usuario s ON d.solicitante_demanda = s.codigo_usuario " +
            "WHERE LOWER(reuniao.comissao_reuniao) like %:nomeComissao% " +
            "AND LOWER(reuniao.data_reuniao) like %:dataReuniao% " +
            "AND LOWER(reuniao.status_reuniao) like %:statusReuniao% " +
            "AND LOWER(d.codigoppmproposta) like %:ppmProposta% " +
            "AND LOWER(a.nome_usuario) like %:analista% " +
            "AND LOWER(s.nome_usuario) like %:solicitante% "+
            "order by " +
            "case when :ordenar = '1' then reuniao.data_reuniao end asc," +
            "case when :ordenar = '2' then reuniao.data_reuniao end desc"

            , nativeQuery = true)
    List<Reuniao> search(String nomeComissao, String dataReuniao, String statusReuniao,
                         String ppmProposta, String analista, String solicitante, String ordenar, Pageable page);

    List<Reuniao> findByDataReuniaoBetweenAndStatusReuniao(Date date, Date date2, StatusReuniao statusBuscado);

    List<Reuniao> findByDataReuniaoBeforeAndStatusReuniao(Date date, StatusReuniao statusBuscado);

}
