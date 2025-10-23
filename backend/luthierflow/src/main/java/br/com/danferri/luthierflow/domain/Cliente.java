package br.com.danferri.luthierflow.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity (name = "CLIENTE")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do cliente é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "O CPF do cliente é obrigatório.")
    @Column(unique = true, nullable = false)
    private String cpf;

    @Email(message = "O formato do e-mail é inválido.")
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CLIENTE_TELEFONE", joinColumns = @JoinColumn(name = "id_cliente"))
    @Column(name = "telefone")
    private List<String> telefones = new ArrayList<>();

    private String rua;
    private String cidade;
    private String estado;
    private String cep;
}
