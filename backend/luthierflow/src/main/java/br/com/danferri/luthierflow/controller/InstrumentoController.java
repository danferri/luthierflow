package br.com.danferri.luthierflow.controller;

import br.com.danferri.luthierflow.domain.Instrumento;
import br.com.danferri.luthierflow.service.InstrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes/{clienteId}/instrumentos")
public class InstrumentoController {
    @Autowired
    private InstrumentoService instrumentoService;

    @PostMapping
    public ResponseEntity<Instrumento> adicionarInstrumento(@PathVariable Long clienteId, @RequestBody Instrumento instrumento) {
        return instrumentoService.salvar(clienteId, instrumento)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Instrumento>> listarInstrumentosDoCliente(@PathVariable Long clienteId) {
        List<Instrumento> instrumentos = instrumentoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(instrumentos);
    }

    @PutMapping("/{instrumentoId}")
    public ResponseEntity<Instrumento> atualizarInstrumento(@PathVariable Long clienteId, @PathVariable Long instrumentoId, @RequestBody Instrumento instrumento) {
        return instrumentoService.atualizar(clienteId, instrumentoId, instrumento)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{instrumentoId}")
    public  ResponseEntity<Void> deletarInstrumento(@PathVariable Long clienteId, @PathVariable Long instrumentoId) {
        if(instrumentoService.deletar(clienteId, instrumentoId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
