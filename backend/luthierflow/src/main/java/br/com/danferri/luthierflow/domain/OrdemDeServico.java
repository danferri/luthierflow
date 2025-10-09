package br.com.danferri.luthierflow.domain;

import br.com.danferri.luthierflow.domain.enums.StatusOS;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "ORDEM_SERVICO")
@Data
public class OrdemDeServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoServico;
    private LocalDate dataEntrada;
    private LocalDate dataFinalizacao;

    @Enumerated(EnumType.STRING)
    private StatusOS status;

    @Column(columnDefinition = "TEXT")
    private String descricaoProblema;

    @Column(columnDefinition = "TEXT")
    private String diagnosticoServico;

    private BigDecimal valorMaoDeObra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instrumento")
    private Instrumento instrumento;


    @OneToMany(mappedBy = "ordemDeServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemServico> itens = new HashSet<>();
}
