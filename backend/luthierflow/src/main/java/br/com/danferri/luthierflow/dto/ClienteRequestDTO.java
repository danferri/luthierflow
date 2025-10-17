package br.com.danferri.luthierflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// NOTE: Usamos um DTO específico para a entrada de dados.
// Ele não tem 'id' porque o cliente não deve enviar um ID ao criar um novo registro.
// Também adicionamos as anotações de validação aqui, no portão de entrada da API.
@Data
public class ClienteRequestDTO {

    @NotBlank(message = "O nome não pode estar em branco.")
    private String nome;

    @Email(message = "O formato do e-mail é inválido.")
    @NotBlank(message = "O e-mail não pode estar em branco.")
    private String email;

    @NotBlank(message = "O CPF não pode estar em branco.")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos.")
    private String cpf;

    // Adicione os outros campos que você espera receber do cliente
    private String cep;
    private String rua;
    private String cidade;
    private String estado;
}