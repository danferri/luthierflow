package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Cliente;
import br.com.danferri.luthierflow.domain.Instrumento;
import br.com.danferri.luthierflow.dto.InstrumentoRequestDTO;
import br.com.danferri.luthierflow.dto.InstrumentoResponseDTO;
import br.com.danferri.luthierflow.repository.ClienteRepository;
import br.com.danferri.luthierflow.repository.InstrumentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstrumentoService {

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<InstrumentoResponseDTO> listarTodos() {
        return instrumentoRepository.findAll()
                .stream()
                .map(InstrumentoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InstrumentoResponseDTO> listarPorCliente(Long clienteId) {
        return instrumentoRepository.findAllByClienteId(clienteId)
                .stream()
                .map(InstrumentoResponseDTO::new)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Optional<InstrumentoResponseDTO> buscarPorId(Long id) {
        return instrumentoRepository.findById(id)
                .map(InstrumentoResponseDTO::new);
    }

    @Transactional
    public InstrumentoResponseDTO salvar(InstrumentoRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + dto.getIdCliente() + " não encontrado."));

        Instrumento instrumento = new Instrumento();
        instrumento.setTipo(dto.getTipo());
        instrumento.setMarca(dto.getMarca());
        instrumento.setModelo(dto.getModelo());
        instrumento.setNumeroSerie(dto.getNumeroSerie());
        instrumento.setCliente(cliente);

        Instrumento instrumentoSalvo = instrumentoRepository.save(instrumento);
        return new InstrumentoResponseDTO(instrumentoSalvo);
    }

    @Transactional
    public Optional<InstrumentoResponseDTO> atualizar(Long id, InstrumentoRequestDTO dto) {
        return instrumentoRepository.findById(id)
                .map(instrumentoExistente -> {
                    if (dto.getTipo() != null) {
                        instrumentoExistente.setTipo(dto.getTipo());
                    }
                    if (dto.getMarca() != null) {
                        instrumentoExistente.setMarca(dto.getMarca());
                    }
                    if (dto.getModelo() != null) {
                        instrumentoExistente.setModelo(dto.getModelo());
                    }
                    if (dto.getNumeroSerie() != null) {
                        instrumentoExistente.setNumeroSerie(dto.getNumeroSerie());
                    }

                    if (dto.getIdCliente() != null) {
                        Cliente novoCliente = clienteRepository.findById(dto.getIdCliente())
                                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + dto.getIdCliente() + " não encontrado."));
                        instrumentoExistente.setCliente(novoCliente);
                    }

                    Instrumento instrumentoAtualizado = instrumentoRepository.save(instrumentoExistente);
                    return new InstrumentoResponseDTO(instrumentoAtualizado);
                });
    }

    @Transactional
    public void deletar(Long id) {
        if (instrumentoRepository.existsById(id)) {
            instrumentoRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Instrumento com ID " + id + " não encontrado ou já arquivado.");
        }
    }
}