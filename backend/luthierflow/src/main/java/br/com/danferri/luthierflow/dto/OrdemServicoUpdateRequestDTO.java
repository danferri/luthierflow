package br.com.danferri.luthierflow.dto;

import br.com.danferri.luthierflow.domain.enums.StatusOS;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrdemServicoUpdateRequestDTO {

    private String tipoServico;
    private String descricaoProblema;
    private String diagnosticoServico;
    private BigDecimal valorMaoDeObra;
    private StatusOS status;

}