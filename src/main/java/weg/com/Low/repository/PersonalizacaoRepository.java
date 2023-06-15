package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Notificacao;
import weg.com.Low.model.entity.Personalizacao;

@Repository
public interface PersonalizacaoRepository extends JpaRepository<Personalizacao, Integer> {
    public Personalizacao findByAtivaPersonalizacao(boolean ativa);
}
