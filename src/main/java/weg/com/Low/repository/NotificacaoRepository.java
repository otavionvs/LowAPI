package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Notificacao;
import weg.com.Low.model.entity.Usuario;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
    List<Notificacao> findByUsuarioNotificacao(Usuario usuario);
}
