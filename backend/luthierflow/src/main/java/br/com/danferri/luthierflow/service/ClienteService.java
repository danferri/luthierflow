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
        return clienteRepository.findAll().stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ClienteResponseDTO> buscarPorId(Long id) {
        return clienteRepository.findById(id).map(ClienteResponseDTO::new);
    }

    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        if (clienteRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema");
        }
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefones(dto.getTelefones());
        cliente.setCep(dto.getCep());
        cliente.setRua(dto.getRua());
        cliente.setCidade(dto.getCidade());
        cliente.setEstado(dto.getEstado());

        Cliente clienteSalvo = clienteRepository.save(cliente);
        return new ClienteResponseDTO(clienteSalvo);
    }

    public Optional<ClienteResponseDTO> atualizar(Long id, ClienteRequestDTO dto) {
        return clienteRepository.findById(id)
                .map(clienteExistente -> {

                    if (dto.getNome() != null) {
                        clienteExistente.setNome(dto.getNome());
                    }
                    if (dto.getEmail() != null) {
                        clienteExistente.setEmail(dto.getEmail());
                    }
                    if (dto.getCpf() != null) {
                        clienteExistente.setCpf(dto.getCpf());
                    }
                    if (dto.getCep() != null) {
                        clienteExistente.setCep(dto.getCep());
                    }
                    if (dto.getRua() != null) {
                        clienteExistente.setRua(dto.getRua());
                    }
                    if (dto.getCidade() != null) {
                        clienteExistente.setCidade(dto.getCidade());
                    }
                    if (dto.getEstado() != null) {
                        clienteExistente.setEstado(dto.getEstado());
                    }
                    if (dto.getTelefones() != null && !dto.getTelefones().isEmpty()) {
                        clienteExistente.setTelefones(dto.getTelefones());
                    }

                    Cliente clienteAtualizado = clienteRepository.save(clienteExistente);

                    return new ClienteResponseDTO(clienteAtualizado);
                });
    }

    public void deletar(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setAtivo(false);
            clienteRepository.save(cliente);
        }
    }
}