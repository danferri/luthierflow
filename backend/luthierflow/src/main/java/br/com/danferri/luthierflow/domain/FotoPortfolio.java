package br.com.danferri.luthierflow.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity(name = "FOTO_PORTIFOLIO")
@Data
@EqualsAndHashCode(of = "id")
public class FotoPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String urlImagem;

    private String legenda;

    private Integer ordemExibicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto_portifolio")
    @ToString.Exclude
    private ProjetoPortfolio projetoPortfolio;
}
