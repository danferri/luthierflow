package br.com.danferri.luthierflow.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PecaRequestDTO {

    private String nomePeca;
    private String modelo;
    private String fabricante;

    private Integer qtdEstoque;

    private BigDecimal precoVenda;
}