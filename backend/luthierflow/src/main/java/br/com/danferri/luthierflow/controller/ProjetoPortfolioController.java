package br.com.danferri.luthierflow.controller;

import br.com.danferri.luthierflow.domain.ProjetoPortfolio;
import br.com.danferri.luthierflow.dto.ProjetoPortfolioResponseDTO;
import br.com.danferri.luthierflow.dto.ProjetoPortfolioUpdateRequestDTO;
import br.com.danferri.luthierflow.service.ProjetoPortfolioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/portfolio")
public class ProjetoPortfolioController {

    @Autowired
    private ProjetoPortfolioService projetoPortfolioService;

    @GetMapping
    public ResponseEntity<List<ProjetoPortfolioResponseDTO>> listarTodosOsProjetos() {
        List<ProjetoPortfolioResponseDTO> projetos = projetoPortfolioService.listarTodos().stream()
                .map(ProjetoPortfolioResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoPortfolioResponseDTO> buscarProjetoPorId(@PathVariable Long id) {
        return projetoPortfolioService.buscarPorId(id)
                .map(projeto -> ResponseEntity.ok(new ProjetoPortfolioResponseDTO(projeto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/publico")
    public ResponseEntity<List<ProjetoPortfolioResponseDTO>> listarProjetosPublicos() {
        List<ProjetoPortfolioResponseDTO> projetosPublicos = projetoPortfolioService.listarPublicados().stream()
                .map(ProjetoPortfolioResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projetosPublicos);
    }

    @PostMapping
    public ResponseEntity<ProjetoPortfolioResponseDTO> promoverOrdemDeServico(@RequestParam Long ordemDeServicoId) {
        try {
            ProjetoPortfolio novoProjeto = projetoPortfolioService.promoverParaPortfolio(ordemDeServicoId);
            return ResponseEntity.ok(new ProjetoPortfolioResponseDTO(novoProjeto));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().header("X-Error-Message", e.getMessage()).build();
        }
    }

    @PostMapping("/{id}/fotos")
    public ResponseEntity<ProjetoPortfolioResponseDTO> adicionarFoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "legenda", required = false, defaultValue = "") String legenda,
            @RequestParam(value = "ordemExibicao", required = false, defaultValue = "0") Integer ordemExibicao) {
        try {
            ProjetoPortfolio projetoAtualizado = projetoPortfolioService.adicionarFoto(id, file, legenda, ordemExibicao);
            return ResponseEntity.ok(new ProjetoPortfolioResponseDTO(projetoAtualizado));
        } catch (Exception e) {
            return ResponseEntity.status(500).header("X-Error-Message", e.getMessage()).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetoPortfolioResponseDTO> atualizarProjeto(
            @PathVariable Long id,
            @Valid @RequestBody ProjetoPortfolioUpdateRequestDTO dto) {

        return projetoPortfolioService.atualizar(id, dto)
                .map(projetoAtualizado -> ResponseEntity.ok(new ProjetoPortfolioResponseDTO(projetoAtualizado)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProjeto(@PathVariable Long id) {
        try {
            projetoPortfolioService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}