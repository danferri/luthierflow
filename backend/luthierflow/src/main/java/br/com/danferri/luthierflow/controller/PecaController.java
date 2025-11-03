package br.com.danferri.luthierflow.controller;

import br.com.danferri.luthierflow.dto.PecaRequestDTO;
import br.com.danferri.luthierflow.dto.PecaResponseDTO;
import br.com.danferri.luthierflow.service.PecaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pecas")
public class PecaController {

    @Autowired
    private PecaService pecaService;

    @PostMapping
    public ResponseEntity<PecaResponseDTO> adicionarPeca(@Valid @RequestBody PecaRequestDTO dto) {
        PecaResponseDTO novaPeca = pecaService.salvar(dto);
        // Retorna 201 Created com a localização do novo recurso
        URI location = URI.create(String.format("/pecas/%s", novaPeca.getId()));
        return ResponseEntity.created(location).body(novaPeca);
    }

    @GetMapping
    public ResponseEntity<List<PecaResponseDTO>> listarTodasAsPecas() {
        List<PecaResponseDTO> pecas = pecaService.listarTodas();
        return ResponseEntity.ok(pecas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PecaResponseDTO> buscarPecaPorId(@PathVariable Long id) {
        try {
            PecaResponseDTO peca = pecaService.buscarPorId(id);
            return ResponseEntity.ok(peca);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PecaResponseDTO> atualizarPeca(@PathVariable Long id, @Valid @RequestBody PecaRequestDTO dto) {
        try {
            PecaResponseDTO pecaAtualizada = pecaService.atualizar(id, dto);
            return ResponseEntity.ok(pecaAtualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPeca(@PathVariable Long id) {
        try {
            pecaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}