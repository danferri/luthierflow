package br.com.danferri.luthierflow.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.math.BigDecimal;

@Entity // Diz ao JPA que esta classe representa uma tabela no banco de dados
@Data   // Anotação do Lombok para criar getters, setters, toString, etc. automaticamente
public class Peca {

    @Id // Marca o atributo 'id' como a Chave Primária (PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Diz ao BD para gerar o valor do ID automaticamente
    private Long id;

    private String nomePeca;
    private String fabricante;
    private int qtdEstoque;
    private BigDecimal precoVenda;
}