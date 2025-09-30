package br.com.danferri.luthierflow.controller;

import br.com.danferri.luthierflow.domain.Peca;
import br.com.danferri.luthierflow.service.PecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Diz ao Spring que esta classe é um controlador de API REST
@RequestMapping("/pecas") // Todas as requisições para "/pecas" serão tratadas por esta classe
public class PecaController {

    @Autowired
    private PecaService pecaService;

    @GetMapping // Mapeia requisições HTTP GET para "/pecas" para este método
    public List<Peca> listarTodasAsPecas() {
        return pecaService.listarTodas();
    }

    @PostMapping // Mapeia requisições HTTP POST para "/pecas" para este método
    public Peca adicionarPeca(@RequestBody Peca peca) {
        return pecaService.salvar(peca);
    }
}
