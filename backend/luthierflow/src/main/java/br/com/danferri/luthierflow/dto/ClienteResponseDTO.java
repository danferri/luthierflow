package br.com.danferri.luthierflow.dto;

import br.com.danferri.luthierflow.domain.Cliente;
import lombok.Data;

// NOTE: Este DTO representa os dados que sua API irá retornar.
// Ele garante que você só exponha os campos que o frontend precisa,
// protegendo dados sensíveis que possam existir na sua entidade 'Cliente'.
@Data
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String cep;
    private String rua;
    private String cidade;
    private String estado;

    // Construtor que facilita a conversão da Entidade para o DTO
    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.cpf = cliente.getCpf();
        this.cep = cliente.getCep();
        this.rua = cliente.getRua();
        this.cidade = cliente.getCidade();
        this.estado = cliente.getEstado();
    }
}