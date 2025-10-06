package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Cliente;
import br.com.danferri.luthierflow.domain.Instrumento;
import br.com.danferri.luthierflow.repository.ClienteRepository;
import br.com.danferri.luthierflow.repository.InstrumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class InstrumentoService {
    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Instrumento> listarPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId) ) {
            return Collections.emptyList();
        }
        return instrumentoRepository.findByClienteId(clienteId);
    }

    public Optional<Instrumento> salvar(Long clientId, Instrumento instrumento) {

        Optional<Instrumento> instrumentoExistente = instrumentoRepository.findByNumeroSerieAndClienteId(instrumento.getNumeroSerie(), clientId);
        if (instrumentoExistente.isPresent()) {
            throw new IllegalArgumentException("Instrumento com o mesm o número de série já cadastrado para este cliente.");
        }

        Optional<Cliente> clienteOpt = clienteRepository.findById(clientId);
        if (clienteOpt.isEmpty()) {
            return Optional.empty();
        }

        Cliente cliente = clienteOpt.get();
        instrumento.setCliente(cliente);
        return  Optional.of(instrumentoRepository.save(instrumento));
    }

    public Optional<Instrumento> buscarPorId(Long instrumentoId) {
        return instrumentoRepository.findById(instrumentoId);
    }

    public Optional<Instrumento> atualizar(Long clienteId, Long instrumentoId, Instrumento instrumentoAtualizado) {
        return instrumentoRepository.findById(instrumentoId)
                .filter(instrumento -> instrumento.getCliente().getId().equals(clienteId))
                .map(instrumentoExistente -> {
                   instrumentoExistente.setMarca(instrumentoAtualizado.getMarca());
                   instrumentoExistente.setModelo(instrumentoAtualizado.getModelo());
                   instrumentoExistente.setTipo(instrumentoAtualizado.getTipo());
                   instrumentoExistente.setNumeroSerie(instrumentoAtualizado.getNumeroSerie());
                   return instrumentoRepository.save(instrumentoExistente);
                });
    }

    public boolean deletar(Long clienteId, Long instrumentoId) {
        Optional<Instrumento> instrumentoOpt = instrumentoRepository.findById(instrumentoId);
        if (instrumentoOpt.isPresent() && instrumentoOpt.get().getCliente().getId().equals(clienteId)) {
            instrumentoRepository.deleteById(instrumentoId);
            return true;
        }
        return false;
    }
}