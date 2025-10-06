package br.com.danferri.luthierflow.repository;

import br.com.danferri.luthierflow.domain.Cliente;
import br.com.danferri.luthierflow.domain.Instrumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstrumentoRepository extends JpaRepository<Instrumento, Long> {
    List<Instrumento> findByClienteId(Long clienteId);

    Optional<Instrumento> findByNumeroSerieAndClienteId(String numeroDeSerie, Long clienteId);
}
