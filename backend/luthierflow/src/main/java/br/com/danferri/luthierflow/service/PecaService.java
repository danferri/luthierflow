package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Peca;
import br.com.danferri.luthierflow.repository.PecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PecaService {

    @Autowired
    private PecaRepository pecaRepository;

    public List<Peca> listarTodas() {
        return pecaRepository.findAll();
    }

    public Optional<Peca> buscarPorId(Long id) {
        return pecaRepository.findById(id);
    }

    public Peca salvar(Peca peca) {
        Optional<Peca> pecaExistente = pecaRepository.findByNomePecaAndFabricanteAndModelo(
                peca.getNomePeca(), peca.getFabricante(), peca.getModelo());

        if (pecaExistente.isPresent()) {
            throw new IllegalArgumentException("Peça com mesmo nome, fabricante e modelo já cadastrada");
        }
        return pecaRepository.save(peca);
    }

    public Optional<Peca> atualizar(Long id, Peca pecaAtualizada) {
        return pecaRepository.findById(id)
                .map(pecaExistente -> {
                    if (pecaAtualizada.getNomePeca() != null) {
                        pecaExistente.setNomePeca(pecaAtualizada.getNomePeca());
                    }
                    if (pecaAtualizada.getFabricante() != null) {
                        pecaExistente.setFabricante(pecaAtualizada.getFabricante());
                    }
                    if (pecaAtualizada.getModelo() != null) {
                        pecaExistente.setModelo(pecaAtualizada.getModelo());
                    }

                    pecaExistente.setQtdEstoque(pecaAtualizada.getQtdEstoque());

                    if (pecaAtualizada.getPrecoVenda() != null) {
                        pecaExistente.setPrecoVenda(pecaAtualizada.getPrecoVenda());
                    }
                    return pecaRepository.save(pecaExistente);
                });
    }

    public boolean deletar(Long id) {
        if (pecaRepository.existsById(id)) {
            pecaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}