package weg.com.Low.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weg.com.Low.model.entity.Arquivo;
import weg.com.Low.model.entity.Beneficio;
@Repository
public interface ArquivoRepository  extends JpaRepository<Arquivo, Integer> {
}
