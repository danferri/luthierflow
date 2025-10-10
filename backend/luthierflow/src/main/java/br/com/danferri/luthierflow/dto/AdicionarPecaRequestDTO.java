package br.com.danferri.luthierflow.dto;

import lombok.Data;

@Data
public class AdicionarPecaRequestDTO {
    private Long pecaId;
    private int quantidade;
}