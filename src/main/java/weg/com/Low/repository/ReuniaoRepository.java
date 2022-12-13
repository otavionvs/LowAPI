package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Reuniao;

@Repository
public interface ReuniaoRepository extends JpaRepository<Reuniao, Integer> {
}
