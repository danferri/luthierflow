package br.com.danferri.luthierflow.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardStatsDTO {
    // Para o card "Ordens em Andamento" [cite: 42-43]
    private long ordensEmAndamento;

    // Para o card "Orçamentos para Aprovar" [cite: 44]
    private long orcamentosParaAprovar;

    // Para o card "Itens com Estoque Baixo" [cite: 45]
    private long itensComEstoqueBaixo;

    // Para o card "Total Faturado no Mês" [cite: 46]
    private BigDecimal totalFaturadoNoMes;
}