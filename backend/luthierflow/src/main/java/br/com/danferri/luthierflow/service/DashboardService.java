package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.ItemServico;
import br.com.danferri.luthierflow.domain.OrdemDeServico;
import br.com.danferri.luthierflow.domain.enums.StatusOS;
import br.com.danferri.luthierflow.dto.DashboardStatsDTO;
import br.com.danferri.luthierflow.repository.OrdemServicoRepository;
import br.com.danferri.luthierflow.repository.PecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private PecaRepository pecaRepository;

    // Define o limite para "Estoque Baixo"
    private static final int ESTOQUE_BAIXO_LIMITE = 5;

    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // 1. Contar Ordens em Andamento
        stats.setOrdensEmAndamento(
                ordemServicoRepository.countByStatus(StatusOS.EM_ANDAMENTO)
        );

        // 2. Contar Orçamentos para Aprovar
        stats.setOrcamentosParaAprovar(
                ordemServicoRepository.countByStatus(StatusOS.ORCAMENTO)
        );

        // 3. Contar Itens com Estoque Baixo
        stats.setItensComEstoqueBaixo(
                pecaRepository.countByQtdEstoqueLessThan(ESTOQUE_BAIXO_LIMITE)
        );

        // 4. Calcular Total Faturado no Mês
        stats.setTotalFaturadoNoMes(
                this.calcularTotalFaturadoNoMes()
        );

        return stats;
    }

    private BigDecimal calcularTotalFaturadoNoMes() {
        LocalDate inicioDoMes = LocalDate.now().withDayOfMonth(1);
        LocalDate hoje = LocalDate.now();

        List<OrdemDeServico> ossFinalizadasNoMes =
                ordemServicoRepository.findAllByStatusAndDataFinalizacaoBetween(StatusOS.FINALIZADO, inicioDoMes, hoje);

        BigDecimal totalFaturado = BigDecimal.ZERO;

        for (OrdemDeServico os : ossFinalizadasNoMes) {
            // 1. Adiciona a Mão de Obra
            if (os.getValorMaoDeObra() != null) {
                totalFaturado = totalFaturado.add(os.getValorMaoDeObra());
            }

            // 2. Adiciona o valor das Peças
            for (ItemServico item : os.getItens()) {
                BigDecimal precoPeca = item.getPeca().getPrecoVenda();
                BigDecimal quantidade = new BigDecimal(item.getQtdUsada());
                totalFaturado = totalFaturado.add(precoPeca.multiply(quantidade));
            }
        }
        return totalFaturado;
    }
}