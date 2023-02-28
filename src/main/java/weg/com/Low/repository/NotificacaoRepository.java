package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Notificacao;
import weg.com.Low.model.entity.Usuario;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
    @Query(value = "SELECT n.* FROM notificacao as n " +
            "join usuario_notificacoes as un on un.codigo_notificacao = n.codigo_notificacao " +
            "join usuario as u on u.codigo_usuario = un.codigo_usuario " +
            "where u.codigo_usuario = :usuario", nativeQuery = true)
    List<Notificacao> findAllByUsuario(Integer usuario);
}
