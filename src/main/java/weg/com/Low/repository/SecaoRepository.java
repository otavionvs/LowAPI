package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Secao;

@Repository
public interface SecaoRepository extends JpaRepository<Secao, Integer> {
}
