package br.com.danferri.luthierflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InstrumentoRequestDTO {

    @NotBlank(message = "O tipo é obrigatório.")
    private String tipo;

    @NotBlank(message = "A marca é obrigatória.")
    private String marca;

    @NotBlank(message = "O modelo é obrigatório.")
    private String modelo;

    private String numeroSerie;

    @NotNull(message = "O ID do cliente é obrigatório.")
    private Long idCliente;

}