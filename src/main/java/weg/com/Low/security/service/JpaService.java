package weg.com.Low.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.repository.UsuarioRepository;
import weg.com.Low.security.users.UserJpa;

import java.util.Optional;
@Service
public class JpaService implements UserDetailsService {
    @Autowired
    public UsuarioRepository pessoaRepository;


    @Override
    public UserDetails loadUserByUsername(
            String username) throws UsernameNotFoundException {
        Optional<Usuario> pessoaOptional;
        try{
            pessoaOptional = pessoaRepository.findByUserUsuario(username);
        }catch (NumberFormatException e){
            pessoaOptional = pessoaRepository.findByEmailUsuario(username);
        }
        if (pessoaOptional.isPresent()) {
            return new UserJpa(pessoaOptional.get());
        }
        throw new UsernameNotFoundException("Usuário não encontrado!");
    }
}
