package br.com.danferri.luthierflow.dto;

import br.com.danferri.luthierflow.domain.Instrumento;
import lombok.Data;

@Data
public class InstrumentoDTO {
    private Long id;
    private String tipo;
    private String modelo;
    private String marca;

    public InstrumentoDTO(Instrumento instrumento) {
        this.id = instrumento.getId();
        this.tipo = instrumento.getTipo();
        this.modelo = instrumento.getModelo();
        this.marca = instrumento.getMarca();
    }
}