package br.com.danferri.luthierflow.controller;

import br.com.danferri.luthierflow.domain.OrdemDeServico;
import br.com.danferri.luthierflow.service.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @GetMapping
    public ResponseEntity<List<OrdemDeServico>> listarTodasAsOrdens() {
        return ResponseEntity.ok(ordemServicoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemDeServico> buscarOrdemPorId(@PathVariable Long id) {
        return ordemServicoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrdemDeServico> adicionarOrdem(@RequestBody OrdemDeServico os,
                                                         @RequestParam Long clienteId,
                                                         @RequestParam(required = false) Long instrumentoId) {
        try {
            OrdemDeServico novaOs = ordemServicoService.salvar(os, clienteId, instrumentoId);
            return ResponseEntity.ok(novaOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemDeServico> atualizarOrdem(@PathVariable Long id, @RequestBody OrdemDeServico os) {
        return ordemServicoService.atualizar(id, os)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOrdem(@PathVariable Long id) {
        if (ordemServicoService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
