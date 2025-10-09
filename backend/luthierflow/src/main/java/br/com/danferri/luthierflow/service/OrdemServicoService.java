package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Cliente;
import br.com.danferri.luthierflow.domain.Instrumento;
import br.com.danferri.luthierflow.domain.OrdemDeServico;
import br.com.danferri.luthierflow.domain.enums.StatusOS;
import br.com.danferri.luthierflow.repository.ClienteRepository;
import br.com.danferri.luthierflow.repository.InstrumentoRepository;
import br.com.danferri.luthierflow.repository.OrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrdemServicoService {
    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    public List<OrdemDeServico> listarTodas() {
        return ordemServicoRepository.findAll();
    }

    public Optional<OrdemDeServico> buscarPorId(Long id) {
        return ordemServicoRepository.findById(id);
    }

    public OrdemDeServico salvar(OrdemDeServico os, Long clienteId, Long instrumentoId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID: " + clienteId));

        Instrumento instrumento = null;
        if (instrumentoId != null) {
            instrumento = instrumentoRepository.findById(instrumentoId)
                    .filter(i -> i.getCliente().getId().equals(clienteId))
                    .orElseThrow(() -> new IllegalArgumentException("Instrumento não encontrado ou não pertence ao cliente informado."));
        }

        os.setCliente(cliente);
        os.setInstrumento(instrumento);
        os.setDataEntrada(LocalDate.now());
        os.setStatus(StatusOS.ORCAMENTO);

        return ordemServicoRepository.save(os);
    }

    public Optional<OrdemDeServico> atualizar(Long id, OrdemDeServico osAtualizada) {
        return ordemServicoRepository.findById(id)
                .map(osExistente -> {
                    osExistente.setTipoServico(osAtualizada.getTipoServico());
                    osExistente.setDescricaoProblema(osAtualizada.getDescricaoProblema());
                    osExistente.setDiagnosticoServico(osAtualizada.getDiagnosticoServico());
                    osExistente.setValorMaoDeObra(osAtualizada.getValorMaoDeObra());
                    osExistente.setStatus(osAtualizada.getStatus());

                    if (osAtualizada.getStatus() == StatusOS.FINALIZADO) {
                        osExistente.setDataFinalizacao(LocalDate.now());
                    }

                    return ordemServicoRepository.save(osExistente);
                });
    }

    public boolean deletar(Long id) {
        if (ordemServicoRepository.existsById(id)) {
            ordemServicoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}


