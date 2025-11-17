package br.com.danferri.luthierflow.controller;

import br.com.danferri.luthierflow.dto.InstrumentoRequestDTO;
import br.com.danferri.luthierflow.dto.InstrumentoResponseDTO;
import br.com.danferri.luthierflow.service.InstrumentoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instrumentos")
public class InstrumentoController {

    @Autowired
    private InstrumentoService instrumentoService;

    @GetMapping
    public ResponseEntity<List<InstrumentoResponseDTO>> listarTodos() {
        List<InstrumentoResponseDTO> instrumentos = instrumentoService.listarTodos();
        return ResponseEntity.ok(instrumentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstrumentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return instrumentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InstrumentoResponseDTO> salvar(@Valid @RequestBody InstrumentoRequestDTO dto) {
        InstrumentoResponseDTO instrumentoSalvo = instrumentoService.salvar(dto);

        return ResponseEntity.ok(instrumentoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstrumentoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody InstrumentoRequestDTO dto) {
        return instrumentoService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            instrumentoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}