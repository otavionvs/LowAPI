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
            "INNER JOIN comissao c ON reuniao.codigo_comissao = c.codigo_comissao " +
            "INNER JOIN proposta_reuniao pr ON reuniao.codigo_reuniao = pr.codigo_reuniao " +
            "INNER JOIN proposta p ON pr.codigo_proposta = p.codigo_proposta " +
            "INNER JOIN demanda_analista da ON p.codigo_demanda_analista = da.codigo_demanda_analista " +
            "INNER JOIN demanda d ON da.demanda_codigo = d.codigo_demanda " +
            "INNER JOIN usuario a ON da.analista_codigo = a.codigo_usuario " +
            "INNER JOIN usuario s ON d.solicitante_demanda = s.codigo_usuario " +
            "WHERE LOWER(c.nome_comissao) like %:nomeComissao% " +
            "AND LOWER(reuniao.data_reuniao) like %:dataReuniao% " +
            "AND LOWER(reuniao.status_reuniao) like %:statusReuniao% " +
            "AND LOWER(p.ppm_proposta) like %:ppmProposta% " +
            "AND LOWER(a.nome_usuario) like %:analista% " +
            "AND LOWER(s.nome_usuario) like %:solicitante%", nativeQuery = true)
    List<Reuniao> search(String nomeComissao, String dataReuniao, String statusReuniao,
                         String ppmProposta, String analista, String solicitante, Pageable page);

    List<Reuniao> findByDataReuniaoBetweenAndStatusReuniao(Date date, Date date2, StatusReuniao statusBuscado);

    List<Reuniao> findByDataReuniaoBeforeAndStatusReuniao(Date date, StatusReuniao statusBuscado);

}
