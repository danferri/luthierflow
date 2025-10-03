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

    public Peca salvar(Peca peca) {
        Optional<Peca> pecaExistente = pecaRepository.findByNomePecaAndFabricante(peca.getNomePeca(), peca.getFabricante());

        if (pecaExistente.isPresent()) {
            throw new IllegalArgumentException("Peça com mesmo nome e fabricante já cadastrada");
        }
        return pecaRepository.save(peca);
    }

    public Optional<Peca> atualizar(Long id, Peca pecaAtualizada) {
        return pecaRepository.findById(id)
                .map(pecaExistente -> {
                    pecaExistente.setNomePeca(pecaAtualizada.getNomePeca());
                    pecaExistente.setFabricante(pecaAtualizada.getFabricante());
                    pecaExistente.setQtdEstoque(pecaAtualizada.getQtdEstoque());
                    pecaExistente.setPrecoVenda(pecaAtualizada.getPrecoVenda());
                    return pecaRepository.save(pecaExistente);
                });
    }

    public void deletar(Long id) {
        if (!pecaRepository.existsById(id)) {
            return;
        }
        pecaRepository.deleteById(id);
    }
}