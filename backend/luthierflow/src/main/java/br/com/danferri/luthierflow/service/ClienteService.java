package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Cliente;
import br.com.danferri.luthierflow.dto.ClienteRequestDTO;
import br.com.danferri.luthierflow.dto.ClienteResponseDTO;
import br.com.danferri.luthierflow.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteResponseDTO> listarTodosClientes() {
        // NOTE: Busca todos os clientes e os converte para DTO.
        return clienteRepository.findAll().stream()
                .map(ClienteResponseDTO::new) // Equivalente a .map(cliente -> new ClienteResponseDTO(cliente))
                .collect(Collectors.toList());
    }

    public Optional<ClienteResponseDTO> buscarPorId(Long id) {
        // NOTE: Busca o cliente e, se encontrar, converte para DTO.
        return clienteRepository.findById(id).map(ClienteResponseDTO::new);
    }

    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        // NOTE: Converte o DTO de requisição para a entidade Cliente.
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setCpf(dto.getCpf());
        cliente.setCep(dto.getCep());
        cliente.setRua(dto.getRua());
        cliente.setCidade(dto.getCidade());
        cliente.setEstado(dto.getEstado());

        Cliente clienteSalvo = clienteRepository.save(cliente);

        // NOTE: Converte a entidade salva de volta para um DTO de resposta.
        return new ClienteResponseDTO(clienteSalvo);
    }

    public Optional<ClienteResponseDTO> atualizar(Long id, ClienteRequestDTO dto) {
        // NOTE: Busca o cliente existente.
        return clienteRepository.findById(id)
                .map(clienteExistente -> {
                    // NOTE: Atualiza os dados do cliente existente com os dados do DTO.
                    clienteExistente.setNome(dto.getNome());
                    clienteExistente.setEmail(dto.getEmail());
                    clienteExistente.setCpf(dto.getCpf());
                    clienteExistente.setCep(dto.getCep());
                    clienteExistente.setRua(dto.getRua());
                    clienteExistente.setCidade(dto.getCidade());
                    clienteExistente.setEstado(dto.getEstado());

                    Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
                    // NOTE: Converte para DTO e retorna.
                    return new ClienteResponseDTO(clienteAtualizado);
                });
    }

    public void deletar(Long id) {
        // (Não precisa de alteração)
        clienteRepository.deleteById(id);
    }
}