package br.com.danferri.luthierflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

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

    @NotEmpty(message = "Pelo menos um telefone deve ser fornecido.")
    private List<String> telefones;

    private String cep;
    private String rua;
    private String cidade;
    private String estado;
}