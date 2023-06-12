package weg.com.Low.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weg.com.Low.model.entity.Conversa;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.repository.ConversaRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConversaService {
    private ConversaRepository conversaRepository;

    public Conversa save(Conversa entity) {
        return conversaRepository.save(entity);
    }

    public Optional<Conversa> findById(Integer aLong) {
        return conversaRepository.findById(aLong);
    }

    public boolean existsById(Integer aLong) {
        return conversaRepository.existsById(aLong);
    }

    public void deleteById(Integer aLong) {
        conversaRepository.deleteById(aLong);
    }

    public List<Conversa> findByUsuariosConversa(Usuario usuariosConversa) {
        return conversaRepository.findByUsuario(usuariosConversa);
    }
}
