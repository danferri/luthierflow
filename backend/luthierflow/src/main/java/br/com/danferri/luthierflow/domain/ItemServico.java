package br.com.danferri.luthierflow.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "OS_PECA")
@Data
@NoArgsConstructor
public class ItemServico {

    @EmbeddedId
    private ItemServicoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ordemServicoId")
    @JoinColumn(name = "id_ordem_servico")
    private OrdemDeServico ordemDeServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pecaId")
    @JoinColumn(name = "id_peca")
    private Peca peca;

    @Column(name = "qtd_usada", nullable = false)
    private int qtdUsada;

    public ItemServico(OrdemDeServico ordemDeServico, Peca peca, int qtdUsada) {
        this.ordemDeServico = ordemDeServico;
        this.peca = peca;
        this.qtdUsada = qtdUsada;
        this.id = new ItemServicoId(ordemDeServico.getId(), peca.getId());
    }
}
