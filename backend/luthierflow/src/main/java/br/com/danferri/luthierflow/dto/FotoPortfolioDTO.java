package br.com.danferri.luthierflow.dto;

import br.com.danferri.luthierflow.domain.FotoPortfolio;
import lombok.Data;

@Data
public class FotoPortfolioDTO {

    private Long id;
    private String urlImagem;
    private String legenda;
    private Integer ordemExibicao;

    public FotoPortfolioDTO(FotoPortfolio foto) {
        this.id = foto.getId();
        this.urlImagem = foto.getUrlImagem();
        this.legenda = foto.getLegenda();
        this.ordemExibicao = foto.getOrdemExibicao();
    }
}