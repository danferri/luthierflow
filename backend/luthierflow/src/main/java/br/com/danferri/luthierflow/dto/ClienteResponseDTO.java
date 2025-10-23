package br.com.danferri.luthierflow.dto;

import br.com.danferri.luthierflow.domain.Cliente;
import lombok.Data;

import java.util.List;

@Data
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private List<String> telefones;
    private String cep;
    private String rua;
    private String cidade;
    private String estado;

    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.cpf = cliente.getCpf();
        this.telefones = cliente.getTelefones();
        this.cep = cliente.getCep();
        this.rua = cliente.getRua();
        this.cidade = cliente.getCidade();
        this.estado = cliente.getEstado();
    }
}