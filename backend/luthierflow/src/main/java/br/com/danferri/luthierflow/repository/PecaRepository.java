package br.com.danferri.luthierflow.repository;

import br.com.danferri.luthierflow.domain.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Opcional, mas boa prática para indicar que é um componente de persistência
public interface PecaRepository extends JpaRepository<Peca, Long> {
    // A mágica do Spring Data JPA: não precisamos escrever nenhum código aqui!
    // Métodos como save(), findById(), findAll(), deleteById() já estão disponíveis.
}