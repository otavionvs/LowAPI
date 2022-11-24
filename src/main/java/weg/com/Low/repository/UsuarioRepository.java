package weg.com.Low.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByUserUsuario(String user);
    boolean existsByEmailUsuario(String email);
}
