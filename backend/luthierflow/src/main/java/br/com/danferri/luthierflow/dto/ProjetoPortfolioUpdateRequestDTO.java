package br.com.danferri.luthierflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjetoPortfolioUpdateRequestDTO {

    @NotBlank(message = "O título é obrigatório.")
    private String tituloPublico;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricaoPublica;

    @NotBlank(message = "O status de publicação é obrigatório (RASCUNHO ou PUBLICADO).")
    private String statusPublicacao;
}