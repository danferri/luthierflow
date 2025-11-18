package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.FotoPortfolio;
import br.com.danferri.luthierflow.domain.OrdemDeServico;
import br.com.danferri.luthierflow.domain.ProjetoPortfolio;
import br.com.danferri.luthierflow.domain.enums.StatusOS;
import br.com.danferri.luthierflow.dto.ProjetoPortfolioUpdateRequestDTO;
import br.com.danferri.luthierflow.repository.OrdemServicoRepository;
import br.com.danferri.luthierflow.repository.ProjetoPortfolioRepository;
import jakarta.persistence.EntityNotFoundException;
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
            throw new IllegalStateException("Esta Ordem de Serviço já foi adicionada ao portfólio.");
        }

        OrdemDeServico os = ordemServicoRepository.findById(ordemDeServicoId)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada."));

        if (os.getStatus() != StatusOS.FINALIZADO) {
            throw new IllegalStateException("Apenas Ordens de Serviço com status FINALIZADO podem ser adicionadas ao portfólio.");
        }

        ProjetoPortfolio novoProjeto = new ProjetoPortfolio();
        novoProjeto.setOrdemDeServico(os);

        String titulo = os.getTipoServico() + " em " + os.getInstrumento().getMarca() + " " + os.getInstrumento().getModelo();
        novoProjeto.setTituloPublico(titulo);

        novoProjeto.setDescricaoPublica("Descrição do trabalho realizado...");
        novoProjeto.setStatusPublicacao("RASCUNHO"); // Status inicial Rascunho

        return projetoPortfolioRepository.save(novoProjeto);
    }

    @Transactional
    public Optional<ProjetoPortfolio> atualizar(Long id, ProjetoPortfolioUpdateRequestDTO dto) {
        return projetoPortfolioRepository.findById(id)
                .map(projetoExistente -> {

                    if (dto.getTituloPublico() != null) {
                        projetoExistente.setTituloPublico(dto.getTituloPublico());
                    }

                    if (dto.getDescricaoPublica() != null) {
                        projetoExistente.setDescricaoPublica(dto.getDescricaoPublica());
                    }

                    if (dto.getStatusPublicacao() != null) {
                        String novoStatus = dto.getStatusPublicacao();

                        if ("PUBLICADO".equalsIgnoreCase(novoStatus) && !"PUBLICADO".equalsIgnoreCase(projetoExistente.getStatusPublicacao())) {
                            projetoExistente.setDataPublicacao(LocalDate.now());
                        }
                        projetoExistente.setStatusPublicacao(novoStatus);
                    }

                    return projetoPortfolioRepository.save(projetoExistente);
                });
    }

    @Transactional
    public ProjetoPortfolio adicionarFoto(Long portfolioId, MultipartFile file, String legenda, Integer ordemExibicao) {

        ProjetoPortfolio projeto = projetoPortfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto de Portfólio não encontrado."));

        String nomeArquivo = fileStorageService.storeFile(file);

        FotoPortfolio novaFoto = new FotoPortfolio();
        novaFoto.setUrlImagem(nomeArquivo);
        novaFoto.setLegenda(legenda);
        novaFoto.setOrdemExibicao(ordemExibicao);

        projeto.adicionarFoto(novaFoto);

        return projetoPortfolioRepository.save(projeto);
    }

    public List<ProjetoPortfolio> listarPublicados() {
        return projetoPortfolioRepository.findByStatusPublicacao("PUBLICADO");
    }

    public void deletar(Long id) {
        if (!projetoPortfolioRepository.existsById(id)) {
            throw new IllegalArgumentException("Projeto de Portfólio não encontrado.");
        }
        projetoPortfolioRepository.deleteById(id);
    }

    @Transactional
    public void removerFoto(Long projetoId, Long fotoId) {
        ProjetoPortfolio projeto = projetoPortfolioRepository.findById(projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado."));

        FotoPortfolio fotoParaRemover = projeto.getFotos().stream()
                .filter(f -> f.getId().equals(fotoId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Foto não encontrada neste projeto."));

        fileStorageService.deleteFile(fotoParaRemover.getUrlImagem());

        projeto.getFotos().remove(fotoParaRemover);

        projetoPortfolioRepository.save(projeto);
    }
}