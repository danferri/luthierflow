package br.com.danferri.luthierflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrdemServicoCreateRequestDTO {

    @NotNull(message = "O ID do cliente é obrigatório.")
    private Long clienteId;

    private Long instrumentoId;

    @NotBlank(message = "O tipo de serviço é obrigatório.")
    private String tipoServico;

    @NotBlank(message = "A descrição do problema é obrigatória.")
    private String descricaoProblema;
}