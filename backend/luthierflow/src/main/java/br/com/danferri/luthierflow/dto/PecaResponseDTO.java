package br.com.danferri.luthierflow.dto;

import br.com.danferri.luthierflow.domain.Peca;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PecaResponseDTO {

    private Long id;
    private String nomePeca;
    private String modelo;
    private String fabricante;
    private int qtdEstoque;
    private BigDecimal precoVenda;

    public PecaResponseDTO(Peca peca) {
        this.id = peca.getId();
        this.nomePeca = peca.getNomePeca();
        this.modelo = peca.getModelo();
        this.fabricante = peca.getFabricante();
        this.qtdEstoque = peca.getQtdEstoque();
        this.precoVenda = peca.getPrecoVenda();
    }
}