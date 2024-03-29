package weg.com.Low.security.users;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import weg.com.Low.model.entity.Usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserJpa implements UserDetails {

    private Usuario usuario;
    public Collection<GrantedAuthority> authorities;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    public UserJpa(Usuario pessoa){
        this.usuario = pessoa;
    }

    @Override
    public String getPassword() {
        return usuario.getSenhaUsuario();
    }

    @Override
    public String getUsername() {
        return usuario.getEmailUsuario();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(
                this.getUsuario().getNivelAcessoUsuario().toString()
        ));
        return authorities;
    }
}
