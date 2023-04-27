package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Mensagens;

import java.util.List;

@Repository
public interface MensagensRepository extends JpaRepository<Mensagens, Integer> {
    List<Mensagens> findAllByDemandaMensagens(Demanda demanda);
}
