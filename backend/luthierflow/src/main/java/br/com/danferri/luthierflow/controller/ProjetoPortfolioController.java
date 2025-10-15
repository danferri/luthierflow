package br.com.danferri.luthierflow.controller;

import br.com.danferri.luthierflow.domain.ProjetoPortfolio;
import br.com.danferri.luthierflow.dto.ProjetoPortfolioDTO;
import br.com.danferri.luthierflow.service.ProjetoPortfolioService;
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
    public ResponseEntity<List<ProjetoPortfolioDTO>> listarTodosOsProjetos() {
        List<ProjetoPortfolioDTO> projetos = projetoPortfolioService.listarTodos().stream()
                .map(ProjetoPortfolioDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoPortfolioDTO> buscarProjetoPorId(@PathVariable Long id) {
        return projetoPortfolioService.buscarPorId(id)
                .map(projeto -> ResponseEntity.ok(new ProjetoPortfolioDTO(projeto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/publico")
    public ResponseEntity<List<ProjetoPortfolioDTO>> listarProjetosPublicos() {
        List<ProjetoPortfolioDTO> projetosPublicos = projetoPortfolioService.listarPublicados().stream()
                .map(ProjetoPortfolioDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projetosPublicos);
    }

    @PostMapping
    public ResponseEntity<ProjetoPortfolioDTO> promoverOrdemDeServico(@RequestParam Long ordemDeServicoId) {
        try {
            ProjetoPortfolio novoProjeto = projetoPortfolioService.promoverParaPortfolio(ordemDeServicoId);
            return ResponseEntity.ok(new ProjetoPortfolioDTO(novoProjeto));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().header("X-Error-Message", e.getMessage()).build();
        }
    }

    @PostMapping("/{id}/fotos")
    public ResponseEntity<ProjetoPortfolioDTO> adicionarFoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("legenda") String legenda,
            @RequestParam("ordemExibicao") Integer ordemExibicao) {
        try {
            ProjetoPortfolio projetoAtualizado = projetoPortfolioService.adicionarFoto(id, file, legenda, ordemExibicao);
            return ResponseEntity.ok(new ProjetoPortfolioDTO(projetoAtualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetoPortfolioDTO> atualizarProjeto(@PathVariable Long id, @RequestBody ProjetoPortfolio projeto) {
        return projetoPortfolioService.atualizar(id, projeto)
                .map(projetoAtualizado -> ResponseEntity.ok(new ProjetoPortfolioDTO(projetoAtualizado)))
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