package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Peca;
import br.com.danferri.luthierflow.repository.PecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // Diz ao Spring que esta classe contém a lógica de negócio
public class PecaService {

    @Autowired // Injeção de Dependência: o Spring nos dá uma instância do PecaRepository
    private PecaRepository pecaRepository;

    public List<Peca> listarTodas() {
        return pecaRepository.findAll();
    }

    public Peca salvar(Peca peca) {
        // Aqui poderíamos adicionar regras de negócio, como validar se a peça já existe
        return pecaRepository.save(peca);
    }
}