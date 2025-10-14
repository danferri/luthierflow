package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.*;
import br.com.danferri.luthierflow.domain.enums.StatusOS;
import br.com.danferri.luthierflow.repository.ClienteRepository;
import br.com.danferri.luthierflow.repository.InstrumentoRepository;
import br.com.danferri.luthierflow.repository.OrdemServicoRepository;

import br.com.danferri.luthierflow.repository.PecaRepository;
import jakarta.transaction.Transactional;
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

    @Autowired
    private PecaRepository pecaRepository;

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
                    if (osAtualizada.getTipoServico() != null) {
                        osExistente.setTipoServico(osAtualizada.getTipoServico());
                    }
                    if (osAtualizada.getDescricaoProblema() != null) {
                        osExistente.setDescricaoProblema(osAtualizada.getDescricaoProblema());
                    }
                    if (osAtualizada.getDiagnosticoServico() != null) {
                        osExistente.setDiagnosticoServico(osAtualizada.getDiagnosticoServico());
                    }
                    if (osAtualizada.getValorMaoDeObra() != null) {
                        osExistente.setValorMaoDeObra(osAtualizada.getValorMaoDeObra());
                    }
                    if (osAtualizada.getStatus() != null) {
                        osExistente.setStatus(osAtualizada.getStatus());

                        if (osAtualizada.getStatus() == StatusOS.FINALIZADO) {
                            osExistente.setDataFinalizacao(LocalDate.now());
                        }
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

    @Transactional
    public OrdemDeServico adicionarPeca(Long osId, Long pecaId, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        OrdemDeServico os = ordemServicoRepository.findById(osId)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada."));

        Peca peca = pecaRepository.findById(pecaId)
                .orElseThrow(() -> new IllegalArgumentException("Peça não encontrada."));

        if (peca.getQtdEstoque() < quantidade) {
            throw new IllegalStateException("Estoque insuficiente para a peça: " + peca.getNomePeca());
        }

        ItemServico item = os.getItens().stream()
                .filter(i -> i.getPeca().getId().equals(pecaId))
                .findFirst()
                .orElse(new ItemServico(os, peca, 0));

        item.setQtdUsada(item.getQtdUsada() + quantidade);
        os.getItens().add(item);

        peca.setQtdEstoque(peca.getQtdEstoque() - quantidade);
        pecaRepository.save(peca);

        return ordemServicoRepository.save(os);
    }

    @Transactional
    public OrdemDeServico removerPeca(Long osId, Long pecaId) {
        OrdemDeServico os = ordemServicoRepository.findById(osId)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada."));

        ItemServico itemParaRemover = os.getItens().stream()
                .filter(item -> item.getPeca().getId().equals(pecaId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Peça não encontrada nesta Ordem de Serviço."));

        Peca peca = itemParaRemover.getPeca();
        int quantidadeDevolvida = itemParaRemover.getQtdUsada();

        peca.setQtdEstoque(peca.getQtdEstoque() + quantidadeDevolvida);
        pecaRepository.save(peca);

        os.getItens().remove(itemParaRemover);

        return ordemServicoRepository.save(os);
    }
}


