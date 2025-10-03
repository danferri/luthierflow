package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Cliente;
import br.com.danferri.luthierflow.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodosClientes() {
        return clienteRepository.findAll();
    }

    public Cliente salvar(Cliente cliente) {
        Optional<Cliente> clienteExistente = clienteRepository.findByCpf(cliente.getCpf());
        if (clienteExistente.isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema");
        }
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> atualizar(Long id, Cliente clienteAtualizado) {
        return clienteRepository.findById(id)
                .map(clienteExistente -> {
                    clienteExistente.setNome(clienteAtualizado.getNome());
                    clienteExistente.setEmail(clienteAtualizado.getEmail());
                    clienteExistente.setCpf(clienteAtualizado.getCpf());
                    clienteExistente.setCep(clienteAtualizado.getCep());
                    clienteExistente.setRua(clienteAtualizado.getRua());
                    clienteExistente.setCidade(clienteAtualizado.getCidade());
                    clienteExistente.setEstado(clienteAtualizado.getEstado());
                    return clienteRepository.save(clienteExistente);
                });
    }

    public void deletar(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        }
    }
}
