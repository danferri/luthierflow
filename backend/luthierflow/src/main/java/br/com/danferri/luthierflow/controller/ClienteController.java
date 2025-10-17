package br.com.danferri.luthierflow.controller;

import br.com.danferri.luthierflow.dto.ClienteRequestDTO;
import br.com.danferri.luthierflow.dto.ClienteResponseDTO;
import br.com.danferri.luthierflow.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodosOsClientes() {
        // NOTE: Agora retorna uma lista de DTOs.
        List<ClienteResponseDTO> clientes = clienteService.listarTodosClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarClientePorId(@PathVariable Long id) {
        // NOTE: Agora retorna o DTO de resposta.
        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> adicionarCliente(@RequestBody @Valid ClienteRequestDTO clienteDTO) {
        // NOTE: Recebe o DTO de requisição e ativa a validação com @Valid.
        ClienteResponseDTO novoCliente = clienteService.salvar(clienteDTO);
        return ResponseEntity.ok(novoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable Long id, @RequestBody @Valid ClienteRequestDTO clienteDTO) {
        // NOTE: Também recebe o DTO de requisição para a atualização.
        return clienteService.atualizar(id, clienteDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}