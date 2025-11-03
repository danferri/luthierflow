package br.com.danferri.luthierflow.controller;

import br.com.danferri.luthierflow.dto.ClienteRequestDTO;
import br.com.danferri.luthierflow.dto.ClienteResponseDTO;
import br.com.danferri.luthierflow.dto.InstrumentoResponseDTO;
import br.com.danferri.luthierflow.service.ClienteService;
import br.com.danferri.luthierflow.service.InstrumentoService;
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

    @Autowired
    private InstrumentoService instrumentoService;

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodosOsClientes() {
        List<ClienteResponseDTO> clientes = clienteService.listarTodosClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarClientePorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{clienteId}/instrumentos")
    public ResponseEntity<List<InstrumentoResponseDTO>> listarInstrumentosPorCliente(@PathVariable Long clienteId) {
        List<InstrumentoResponseDTO> instrumentos = instrumentoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(instrumentos);
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> adicionarCliente(@RequestBody @Valid ClienteRequestDTO clienteDTO) {
        ClienteResponseDTO novoCliente = clienteService.salvar(clienteDTO);
        return ResponseEntity.ok(novoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable Long id, @RequestBody @Valid ClienteRequestDTO clienteDTO) {
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