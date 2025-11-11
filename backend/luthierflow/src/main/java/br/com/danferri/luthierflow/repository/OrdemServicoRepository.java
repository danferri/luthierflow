package br.com.danferri.luthierflow.repository;

import br.com.danferri.luthierflow.domain.OrdemDeServico;
import br.com.danferri.luthierflow.domain.enums.StatusOS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemDeServico, Long> {
    List<OrdemDeServico> findByClienteId(Long clienteId);

    long countByStatus(StatusOS status);

    List<OrdemDeServico> findAllByStatusAndDataFinalizacaoBetween(StatusOS status, LocalDate start, LocalDate end);
}
