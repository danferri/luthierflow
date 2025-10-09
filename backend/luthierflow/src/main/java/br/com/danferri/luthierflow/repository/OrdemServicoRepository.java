package br.com.danferri.luthierflow.repository;

import br.com.danferri.luthierflow.domain.OrdemDeServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemDeServico, Long> {

    List<OrdemDeServico> findByClienteId(Long clienteId);
}
