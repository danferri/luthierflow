package br.com.danferri.luthierflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "INSTRUMENTO")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SQLDelete(sql = "UPDATE INSTRUMENTO SET ativo = false WHERE id = ?")
@Where(clause = "ativo = true")
public class Instrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String numeroSerie;

    @NotBlank(message = "O tipo do instrumento é obrigatório.")
    private String tipo;

    @NotBlank(message = "O modelo do instrumento é obrigatório.")
    private String modelo;

    @NotBlank(message = "A marca do instrumento é obrigatória.")
    private String marca;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;

    @Column(nullable = false)
    private boolean ativo = true;
}
