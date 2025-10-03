package br.com.danferri.luthierflow.controller;

import br.com.danferri.luthierflow.domain.Peca;
import br.com.danferri.luthierflow.service.PecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pecas")
public class PecaController {

    @Autowired
    private PecaService pecaService;

    @GetMapping
    public List<Peca> listarTodasAsPecas() {
        return pecaService.listarTodas();
    }

    @PostMapping
    public Peca adicionarPeca(@RequestBody Peca peca) {
        return pecaService.salvar(peca);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Peca> atualizarPeca(@PathVariable Long id, @RequestBody Peca peca) {
        return pecaService.atualizar(id, peca)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPeca(@PathVariable Long id) {
        pecaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
