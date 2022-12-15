package weg.com.Low.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Usuario;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByUserUsuario(String user);
    boolean existsByEmailUsuario(String email);

    @Query(value = "select * from Usuario u " +
            "WHERE LOWER(u.nome_usuario) like %:searchTerm% " +
            "OR LOWER(u.email_usuario) like %:searchTerm%", nativeQuery = true)
    List<Usuario> search(String searchTerm);
//    Page<Usuario> search(
//            @Param("searchTerm") String searchTerm,
//            Pageable pageable);
}
