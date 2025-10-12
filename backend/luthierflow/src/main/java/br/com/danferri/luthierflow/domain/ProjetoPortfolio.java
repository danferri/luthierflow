package br.com.danferri.luthierflow.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "PROJETO_PORTIFOLIO")
@Data
@EqualsAndHashCode(of = "id")
public class ProjetoPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tituloPublico;

    @Column(columnDefinition = "TEXT")
    private String descricaoPublica;

    private String statusPublicacao; // Ex: "RASCUNHO", "PUBLICADO"

    private LocalDate dataPublicacao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ordem_servico", unique = true)
    private OrdemDeServico ordemDeServico;

    @OneToMany(
            mappedBy = "projetoPortfolio",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FotoPortfolio> fotos = new ArrayList<>();

    // Método auxiliar para facilitar a adição de fotos
    public void adicionarFoto(FotoPortfolio foto) {
        this.fotos.add(foto);
        foto.setProjetoPortfolio(this);
    }
}

