package br.com.danferri.luthierflow.dto;

import br.com.danferri.luthierflow.domain.OrdemDeServico;
import br.com.danferri.luthierflow.domain.enums.StatusOS;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrdemServicoDTO {

    private Long id;
    private String tipoServico;
    private LocalDate dataEntrada;
    private LocalDate dataFinalizacao;
    private StatusOS status;
    private String descricaoProblema;
    private String diagnosticoServico;
    private BigDecimal valorMaoDeObra;
    private ClienteDTO cliente;
    private InstrumentoDTO instrumento;

    public OrdemServicoDTO(OrdemDeServico os) {
        this.id = os.getId();
        this.tipoServico = os.getTipoServico();
        this.dataEntrada = os.getDataEntrada();
        this.dataFinalizacao = os.getDataFinalizacao();
        this.status = os.getStatus();
        this.descricaoProblema = os.getDescricaoProblema();
        this.diagnosticoServico = os.getDiagnosticoServico();
        this.valorMaoDeObra = os.getValorMaoDeObra();
        this.cliente = new ClienteDTO(os.getCliente());
        if (os.getInstrumento() != null) {
            this.instrumento = new InstrumentoDTO(os.getInstrumento());
        }
    }
}
