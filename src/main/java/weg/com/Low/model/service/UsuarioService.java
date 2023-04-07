package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService {
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> findById(Integer codigo) {
        return usuarioRepository.findById(codigo);
    }

    public boolean existsById(Integer codigo) {
        return usuarioRepository.existsById(codigo);
    }

    public boolean existsByUserUsuario(String user) {
        return usuarioRepository.existsByUserUsuario(user);
    }

    public boolean existsByEmailUsuario(String email) {
        return usuarioRepository.existsByEmailUsuario(email);
    }

    public Page<Usuario> search(
            String nome, String email, String usuario, String departamento, Pageable page) {
        return usuarioRepository.search(nome.toLowerCase(), email.toLowerCase(), usuario.toLowerCase(), departamento, page);
    }

    public Optional<Usuario> findByUserUsuario(String nome) {
        return usuarioRepository.findByUserUsuario(nome);
    }
}
