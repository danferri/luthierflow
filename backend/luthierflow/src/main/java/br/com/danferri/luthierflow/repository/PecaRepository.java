package br.com.danferri.luthierflow.repository;

import br.com.danferri.luthierflow.domain.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PecaRepository extends JpaRepository<Peca, Long> {
    Optional<Peca> findByNomePecaAndFabricante(String nomePeca, String fabricante);
}