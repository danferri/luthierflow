package br.com.danferri.luthierflow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "OS_PECA")
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemServico that = (ItemServico) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ItemServico{" +
                "id=" + id +
                ", peca=" + (peca != null ? peca.getId() : "null") +
                ", qtdUsada=" + qtdUsada +
                '}';
    }
}