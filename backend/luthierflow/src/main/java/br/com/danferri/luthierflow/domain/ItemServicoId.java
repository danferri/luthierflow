package br.com.danferri.luthierflow.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemServicoId implements Serializable {

    @Column(name = "id_ordem_servico")
    private Long ordemServicoId;

    @Column(name = "id_peca")
    private Long pecaId;
}
