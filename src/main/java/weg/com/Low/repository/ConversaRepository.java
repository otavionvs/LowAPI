package weg.com.Low.repository;

import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Conversa;
import weg.com.Low.model.entity.Usuario;

import java.util.List;

@Repository
public interface ConversaRepository extends JpaRepository<Conversa, Integer> {
    @Query("SELECT c FROM Conversa c JOIN c.usuariosConversa u WHERE u = :usuario")
    List<Conversa> findByUsuario(@Param("usuario") Usuario usuario);
}
