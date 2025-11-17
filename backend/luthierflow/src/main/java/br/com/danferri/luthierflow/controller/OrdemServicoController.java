package br.com.danferri.luthierflow.controller;

import br.com.danferri.luthierflow.domain.OrdemDeServico;
import br.com.danferri.luthierflow.domain.ProjetoPortfolio;
import br.com.danferri.luthierflow.dto.*;
import br.com.danferri.luthierflow.service.OrdemServicoService;
import br.com.danferri.luthierflow.service.ProjetoPortfolioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @Autowired
    private ProjetoPortfolioService projetoPortfolioService;

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponseDTO>> listarTodasAsOrdens() {
        List<OrdemServicoResponseDTO> listaDto = ordemServicoService.listarTodas()
                .stream()
                .map(OrdemServicoResponseDTO::new) // Usa o DTO de Resposta (renomeado)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listaDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoResponseDTO> buscarOrdemPorId(@PathVariable Long id) {
        return ordemServicoService.buscarPorId(id)
                .map(os -> ResponseEntity.ok(new OrdemServicoResponseDTO(os)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrdemServicoResponseDTO> adicionarOrdem(@Valid @RequestBody OrdemServicoCreateRequestDTO dto) {
        try {
            OrdemDeServico novaOs = ordemServicoService.salvar(dto);
            URI location = URI.create(String.format("/ordens-servico/%s", novaOs.getId()));
            return ResponseEntity.created(location).body(new OrdemServicoResponseDTO(novaOs));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemServicoResponseDTO> atualizarOrdem(@PathVariable Long id, @Valid @RequestBody OrdemServicoUpdateRequestDTO dto) {
        return ordemServicoService.atualizar(id, dto)
                .map(osAtualizada -> ResponseEntity.ok(new OrdemServicoResponseDTO(osAtualizada)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{osId}/pecas")
    public ResponseEntity<OrdemServicoResponseDTO> adicionarPecaNaOs(@PathVariable Long osId, @RequestBody AdicionarPecaRequestDTO request) {
        try {
            OrdemDeServico osAtualizada = ordemServicoService.adicionarPeca(osId, request.getPecaId(), request.getQuantidade());
            return ResponseEntity.ok(new OrdemServicoResponseDTO(osAtualizada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/promover-portfolio")
    public ResponseEntity<ProjetoPortfolioResponseDTO> promoverParaPortfolio(@PathVariable Long id) {
        try {
            ProjetoPortfolio novoProjeto = projetoPortfolioService.promoverParaPortfolio(id);
            return ResponseEntity.ok(new ProjetoPortfolioResponseDTO(novoProjeto));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{osId}/pecas/{pecaId}")
    public ResponseEntity<OrdemServicoResponseDTO> removerPecaDaOs(@PathVariable Long osId, @PathVariable Long pecaId) {
        try {
            OrdemDeServico osAtualizada = ordemServicoService.removerPeca(osId, pecaId);
            return ResponseEntity.ok(new OrdemServicoResponseDTO(osAtualizada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOrdem(@PathVariable Long id) {
        if (ordemServicoService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}