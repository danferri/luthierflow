package br.com.danferri.luthierflow.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "INSTRUMENTO")
@Data
public class Instrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroSerie;
    private String tipo;
    private String modelo;
    private String marca;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
}
