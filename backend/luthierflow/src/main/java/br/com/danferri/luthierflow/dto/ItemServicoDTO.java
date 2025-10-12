package br.com.danferri.luthierflow.dto;

import br.com.danferri.luthierflow.domain.ItemServico;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemServicoDTO {
    private Long pecaId;
    private String nomePeca;
    private String modelo;
    private BigDecimal precoVenda;
    private int quantidadeUsada;

    public ItemServicoDTO(ItemServico item) {
        this.pecaId = item.getPeca().getId();
        this.nomePeca = item.getPeca().getNomePeca();
        this.modelo =  item.getPeca().getModelo();
        this.precoVenda = item.getPeca().getPrecoVenda();
        this.quantidadeUsada = item.getQtdUsada();
    }
}