package weg.com.Low.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByUserUsuario(String user);

    boolean existsByEmailUsuario(String email);

    Optional<Usuario> findByEmailUsuario(String email);

    Optional<Usuario> findByUserUsuario(String nome);


    @Query(value = "select * from usuario u " +
            "INNER JOIN departamento d ON u.departamento_codigo = d.codigo_departamento " +
            "WHERE LOWER(u.nome_usuario) like %:nome% " +
            "AND LOWER(u.email_usuario) like %:email% " +
            "AND LOWER(u.user_usuario) like %:usuario% " +
            "AND LOWER(d.nome_departamento) like %:departamento%", nativeQuery = true)
    Page<Usuario> search(String nome, String email, String usuario, String departamento, Pageable pageable);

}
