package br.com.danferri.luthierflow.dto;

import br.com.danferri.luthierflow.domain.ProjetoPortfolio;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProjetoPortfolioDTO {

    private Long id;
    private String tituloPublico;
    private String descricaoPublica;
    private String statusPublicacao;
    private LocalDate dataPublicacao;
    private OrdemServicoDTO ordemDeServico;
    private List<FotoPortfolioDTO> fotos;

    public ProjetoPortfolioDTO(ProjetoPortfolio projeto) {
        this.id = projeto.getId();
        this.tituloPublico = projeto.getTituloPublico();
        this.descricaoPublica = projeto.getDescricaoPublica();
        this.statusPublicacao = projeto.getStatusPublicacao();
        this.dataPublicacao = projeto.getDataPublicacao();
        this.ordemDeServico = new OrdemServicoDTO(projeto.getOrdemDeServico());
        this.fotos = projeto.getFotos().stream()
                .map(FotoPortfolioDTO::new)
                .collect(Collectors.toList());
    }
}