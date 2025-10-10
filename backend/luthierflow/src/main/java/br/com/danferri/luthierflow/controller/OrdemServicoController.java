package br.com.danferri.luthierflow.controller;

import br.com.danferri.luthierflow.domain.OrdemDeServico;
import br.com.danferri.luthierflow.dto.AdicionarPecaRequestDTO;
import br.com.danferri.luthierflow.dto.OrdemServicoDTO;
import br.com.danferri.luthierflow.service.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @GetMapping
    public ResponseEntity<List<OrdemServicoDTO>> listarTodasAsOrdens() {
        List<OrdemServicoDTO> listaDto = ordemServicoService.listarTodas()
                .stream()
                .map(OrdemServicoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listaDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoDTO> buscarOrdemPorId(@PathVariable Long id) {
        return ordemServicoService.buscarPorId(id)
                .map(os -> ResponseEntity.ok(new OrdemServicoDTO(os)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrdemServicoDTO> adicionarOrdem(@RequestBody OrdemDeServico os,
                                                          @RequestParam Long clienteId,
                                                          @RequestParam(required = false) Long instrumentoId) {
        try {
            OrdemDeServico novaOs = ordemServicoService.salvar(os, clienteId, instrumentoId);
            return ResponseEntity.ok(new OrdemServicoDTO(novaOs));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{osId}/pecas")
    public ResponseEntity<OrdemServicoDTO> adicionarPecaNaOs(@PathVariable Long osId, @RequestBody AdicionarPecaRequestDTO request) {
        try {
            OrdemDeServico osAtualizada = ordemServicoService.adicionarPeca(osId, request.getPecaId(), request.getQuantidade());
            return ResponseEntity.ok(new OrdemServicoDTO(osAtualizada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemServicoDTO> atualizarOrdem(@PathVariable Long id, @RequestBody OrdemDeServico os) {
        return ordemServicoService.atualizar(id, os)
                .map(osAtualizada -> ResponseEntity.ok(new OrdemServicoDTO(osAtualizada)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOrdem(@PathVariable Long id) {
        if (ordemServicoService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{osId}/pecas/{pecaId}")
    public ResponseEntity<OrdemServicoDTO> removerPecaDaOs(@PathVariable Long osId, @PathVariable Long pecaId) {
        try {
            OrdemDeServico osAtualizada = ordemServicoService.removerPeca(osId, pecaId);
            return ResponseEntity.ok(new OrdemServicoDTO(osAtualizada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}