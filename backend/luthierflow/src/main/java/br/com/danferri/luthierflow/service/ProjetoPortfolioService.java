package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.FotoPortfolio;
import br.com.danferri.luthierflow.domain.OrdemDeServico;
import br.com.danferri.luthierflow.domain.ProjetoPortfolio;
import br.com.danferri.luthierflow.domain.enums.StatusOS;
import br.com.danferri.luthierflow.repository.OrdemServicoRepository;
import br.com.danferri.luthierflow.repository.ProjetoPortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProjetoPortfolioService {

    @Autowired
    private ProjetoPortfolioRepository projetoPortfolioRepository;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public List<ProjetoPortfolio> listarTodos() {
        return projetoPortfolioRepository.findAll();
    }

    public Optional<ProjetoPortfolio> buscarPorId(Long id) {
        return projetoPortfolioRepository.findById(id);
    }

    @Transactional
    public ProjetoPortfolio promoverParaPortfolio(Long ordemDeServicoId) {

        if (projetoPortfolioRepository.findByOrdemDeServicoId(ordemDeServicoId).isPresent()) {
            throw new IllegalStateException("Esta Ordem de Serviço já foi adicionada ao portfólio.");        }


        OrdemDeServico os = ordemServicoRepository.findById(ordemDeServicoId)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada."));

        if (os.getStatus() != StatusOS.FINALIZADO) {
            throw new IllegalStateException("Apenas Ordens de Serviço com status FINALIZADO podem ser adicionadas ao portfólio.");        }


        ProjetoPortfolio novoProjeto = new ProjetoPortfolio();
        novoProjeto.setOrdemDeServico(os);
        novoProjeto.setTituloPublico("Novo Projeto - " + os.getTipoServico() + " em " + os.getInstrumento().getMarca());
        novoProjeto.setDescricaoPublica("Descrição do trabalho realizado...");
        novoProjeto.setStatusPublicacao("RASCUNHO");

        return projetoPortfolioRepository.save(novoProjeto);
    }

    @Transactional
    public Optional<ProjetoPortfolio> atualizar(Long id, ProjetoPortfolio projetoAtualizado) {
        return projetoPortfolioRepository.findById(id)
                .map(projetoExistente -> {
                    projetoExistente.setTituloPublico(projetoAtualizado.getTituloPublico());
                    projetoExistente.setDescricaoPublica(projetoAtualizado.getDescricaoPublica());

                    String novoStatus = projetoAtualizado.getStatusPublicacao();
                    if ("PUBLICADO".equalsIgnoreCase(novoStatus) && !"PUBLICADO".equalsIgnoreCase(projetoExistente.getStatusPublicacao())) {
                        projetoExistente.setDataPublicacao(LocalDate.now());
                    }
                    projetoExistente.setStatusPublicacao(novoStatus);

                    return projetoPortfolioRepository.save(projetoExistente);
                });
    }

    @Transactional
    public ProjetoPortfolio adicionarFoto(Long portfolioId, MultipartFile file, String legenda, Integer ordemExibicao) {

        ProjetoPortfolio projeto = projetoPortfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto de Portfólio não encontrado."));

        String nomeArquivo = fileStorageService.storeFile(file);

        FotoPortfolio novaFoto = new FotoPortfolio();
        novaFoto.setUrlImagem(nomeArquivo);
        novaFoto.setLegenda(legenda);
        novaFoto.setOrdemExibicao(ordemExibicao);

        projeto.adicionarFoto(novaFoto);

        return projetoPortfolioRepository.save(projeto);
    }

    public void deletar(Long id) {
        if (!projetoPortfolioRepository.existsById(id)) {
            throw new IllegalArgumentException("Projeto de Portfólio não encontrado.");
        }
        projetoPortfolioRepository.deleteById(id);
    }
}