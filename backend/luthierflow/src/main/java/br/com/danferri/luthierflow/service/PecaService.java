package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Peca;
import br.com.danferri.luthierflow.dto.PecaRequestDTO;
import br.com.danferri.luthierflow.dto.PecaResponseDTO;
import br.com.danferri.luthierflow.repository.PecaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PecaService {

    @Autowired
    private PecaRepository pecaRepository;

    @Transactional(readOnly = true)
    public List<PecaResponseDTO> listarTodas() {
        return pecaRepository.findAll()
                .stream()
                .map(PecaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PecaResponseDTO buscarPorId(Long id) {
        return pecaRepository.findById(id)
                .map(PecaResponseDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Peça com ID " + id + " não encontrada."));
    }

    @Transactional
    public PecaResponseDTO salvar(PecaRequestDTO dto) {
        Optional<Peca> pecaExistente = pecaRepository.findByNomePecaAndFabricanteAndModelo(
                dto.getNomePeca(), dto.getFabricante(), dto.getModelo());

        if (pecaExistente.isPresent()) {
            throw new IllegalArgumentException("Peça com mesmo nome, fabricante e modelo já cadastrada");
        }

        Peca novaPeca = new Peca();
        novaPeca.setNomePeca(dto.getNomePeca());
        novaPeca.setFabricante(dto.getFabricante());
        novaPeca.setModelo(dto.getModelo());
        novaPeca.setQtdEstoque(dto.getQtdEstoque() != null ? dto.getQtdEstoque() : 0);
        novaPeca.setPrecoVenda(dto.getPrecoVenda());

        Peca pecaSalva = pecaRepository.save(novaPeca);

        return new PecaResponseDTO(pecaSalva);
    }

    @Transactional
    public PecaResponseDTO atualizar(Long id, PecaRequestDTO dto) {

        Peca pecaExistente = pecaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Peça com ID " + id + " não encontrada."));


        if (dto.getNomePeca() != null) {
            pecaExistente.setNomePeca(dto.getNomePeca());
        }
        if (dto.getFabricante() != null) {
            pecaExistente.setFabricante(dto.getFabricante());
        }
        if (dto.getModelo() != null) {
            pecaExistente.setModelo(dto.getModelo());
        }
        if (dto.getQtdEstoque() != null) { // Agora 'Integer' pode ser checado como nulo
            pecaExistente.setQtdEstoque(dto.getQtdEstoque());
        }
        if (dto.getPrecoVenda() != null) {
            pecaExistente.setPrecoVenda(dto.getPrecoVenda());
        }

        Peca pecaAtualizada = pecaRepository.save(pecaExistente);

        return new PecaResponseDTO(pecaAtualizada);
    }

    @Transactional
    public void deletar(Long id) {
        if (!pecaRepository.existsById(id)) {
            throw new EntityNotFoundException("Peça com ID " + id + " não encontrada.");
        }
        pecaRepository.deleteById(id);
    }
}