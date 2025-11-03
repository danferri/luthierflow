package br.com.danferri.luthierflow.dto;

import br.com.danferri.luthierflow.domain.Instrumento;
import lombok.Data;

@Data
public class InstrumentoResponseDTO {

    private Long id;
    private String tipo;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private Long idCliente;
    private String nomeCliente;

    public InstrumentoResponseDTO(Instrumento instrumento) {
        this.id = instrumento.getId();
        this.tipo = instrumento.getTipo();
        this.marca = instrumento.getMarca();
        this.modelo = instrumento.getModelo();
        this.numeroSerie = instrumento.getNumeroSerie();

        if (instrumento.getCliente() != null) {
            this.idCliente = instrumento.getCliente().getId();
            this.nomeCliente = instrumento.getCliente().getNome();
        }
    }
}