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

    @PostMapping
    public ResponseEntity<Peca> adicionarPeca(@RequestBody Peca peca) {
        Peca novaPeca = pecaService.salvar(peca);
        return ResponseEntity.ok(novaPeca);
    }

    @GetMapping
    public ResponseEntity<List<Peca>> listarTodasAsPecas() {
        List<Peca> pecas = pecaService.listarTodas();
        return ResponseEntity.ok(pecas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Peca> buscarPecaPorId(@PathVariable Long id) {
        return pecaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Peca> atualizarPeca(@PathVariable Long id, @RequestBody Peca peca) {
        return pecaService.atualizar(id, peca)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPeca(@PathVariable Long id) {
        if (pecaService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
