package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.*;
import br.com.danferri.luthierflow.domain.enums.StatusOS;
import br.com.danferri.luthierflow.repository.ClienteRepository;
import br.com.danferri.luthierflow.repository.InstrumentoRepository;
import br.com.danferri.luthierflow.repository.OrdemServicoRepository;
import br.com.danferri.luthierflow.repository.PecaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para OrdemServicoService")
class OrdemServicoServiceTest {

    @Mock
    private OrdemServicoRepository ordemServicoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private InstrumentoRepository instrumentoRepository;
    @Mock
    private PecaRepository pecaRepository;

    @InjectMocks
    private OrdemServicoService ordemServicoService;

    private Cliente cliente;
    private Instrumento instrumento;
    private OrdemDeServico os;
    private Peca peca;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");

        instrumento = new Instrumento();
        instrumento.setId(1L);
        instrumento.setMarca("Fender");
        instrumento.setCliente(cliente);

        os = new OrdemDeServico();
        os.setId(1L);
        os.setCliente(cliente);
        os.setInstrumento(instrumento);
        os.setStatus(StatusOS.ORCAMENTO);
        os.setDataEntrada(LocalDate.now());

        peca = new Peca();
        peca.setId(1L);
        peca.setNomePeca("Cordas");
        peca.setQtdEstoque(10);
    }

    @Nested
    @DisplayName("Testes para o método salvar")
    class TestesDoMetodoSalvar {

        @Test
        @DisplayName("Deve salvar uma OS com sucesso com dados válidos")
        void quandoSalvarOsDeveRetornarOsSalva() {
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(instrumentoRepository.findById(1L)).thenReturn(Optional.of(instrumento));
            when(ordemServicoRepository.save(any(OrdemDeServico.class))).thenAnswer(i -> i.getArgument(0));

            OrdemDeServico resultado = ordemServicoService.salvar(new OrdemDeServico(), 1L, 1L);

            assertNotNull(resultado);
            assertEquals(StatusOS.ORCAMENTO, resultado.getStatus());
            assertEquals(LocalDate.now(), resultado.getDataEntrada());
            assertEquals(cliente, resultado.getCliente());
            assertEquals(instrumento, resultado.getInstrumento());
        }

        @Test
        @DisplayName("Deve lançar exceção ao salvar OS para cliente inexistente")
        void quandoSalvarOsEClienteNaoExisteDeveLancarExcecao() {
            when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> {
                ordemServicoService.salvar(new OrdemDeServico(), 99L, 1L);
            });
        }

        @Test
        @DisplayName("Deve lançar exceção se o instrumento não pertence ao cliente")
        void quandoSalvarOsEInstrumentoNaoPertenceAoClienteDeveLancarExcecao() {
            Cliente outroCliente = new Cliente();
            outroCliente.setId(2L);
            instrumento.setCliente(outroCliente);

            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(instrumentoRepository.findById(1L)).thenReturn(Optional.of(instrumento));

            assertThrows(IllegalArgumentException.class, () -> {
                ordemServicoService.salvar(new OrdemDeServico(), 1L, 1L);
            }, "Deveria lançar exceção pois o instrumento não pertence ao cliente informado.");
        }
    }

    @Nested
    @DisplayName("Testes para o método atualizar")
    class TestesDoMetodoAtualizar {
        @Test
        @DisplayName("Deve atualizar o status de uma OS existente")
        void quandoAtualizarStatusDeveRetornarOsAtualizada() {
            OrdemDeServico dadosAtualizados = new OrdemDeServico();
            dadosAtualizados.setStatus(StatusOS.EM_ANDAMENTO);

            when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(os));
            when(ordemServicoRepository.save(any(OrdemDeServico.class))).thenAnswer(i -> i.getArgument(0));

            Optional<OrdemDeServico> resultado = ordemServicoService.atualizar(1L, dadosAtualizados);

            assertTrue(resultado.isPresent());
            assertEquals(StatusOS.EM_ANDAMENTO, resultado.get().getStatus());
            assertNull(resultado.get().getDataFinalizacao());
        }

        @Test
        @DisplayName("Deve preencher a data de finalização ao atualizar status para FINALIZADO")
        void quandoAtualizarStatusParaFinalizadoDeveSetarDataFinalizacao() {
            OrdemDeServico dadosAtualizados = new OrdemDeServico();
            dadosAtualizados.setStatus(StatusOS.FINALIZADO);

            when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(os));
            when(ordemServicoRepository.save(any(OrdemDeServico.class))).thenAnswer(i -> i.getArgument(0));

            Optional<OrdemDeServico> resultado = ordemServicoService.atualizar(1L, dadosAtualizados);

            assertTrue(resultado.isPresent());
            assertEquals(StatusOS.FINALIZADO, resultado.get().getStatus());
            assertEquals(LocalDate.now(), resultado.get().getDataFinalizacao());
        }
    }

    @Nested
    @DisplayName("Testes para gestão de peças (adicionar/remover)")
    class TestesDeGestaoDePecas {

        @Test
        @DisplayName("Deve adicionar uma peça a uma OS e abater o estoque")
        void quandoAdicionarPecaDeveRetornarOsAtualizadaEAbaterEstoque() {
            when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(os));
            when(pecaRepository.findById(1L)).thenReturn(Optional.of(peca));
            when(ordemServicoRepository.save(any(OrdemDeServico.class))).thenReturn(os);

            ordemServicoService.adicionarPeca(1L, 1L, 1);

            assertEquals(1, os.getItens().size());
            assertEquals(9, peca.getQtdEstoque()); // Estoque inicial era 10
        }

        @Test
        @DisplayName("Deve lançar exceção ao adicionar peça com estoque insuficiente")
        void quandoAdicionarPecaEEstoqueInsuficienteDeveLancarExcecao() {
            when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(os));
            when(pecaRepository.findById(1L)).thenReturn(Optional.of(peca));

            assertThrows(IllegalStateException.class, () -> {
                ordemServicoService.adicionarPeca(1L, 1L, 11); // Tentando usar 11, mas só temos 10
            });
        }

        @Test
        @DisplayName("Deve remover uma peça de uma OS e devolver ao estoque")
        void quandoRemoverPecaDeveRetornarOsAtualizadaEDevolverEstoque() {
            ItemServico item = new ItemServico(os, peca, 2);
            os.getItens().add(item);
            peca.setQtdEstoque(8);

            when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(os));
            when(ordemServicoRepository.save(any(OrdemDeServico.class))).thenReturn(os);

            ordemServicoService.removerPeca(1L, 1L);

            assertTrue(os.getItens().isEmpty());
            assertEquals(10, peca.getQtdEstoque());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar remover peça que não está na OS")
        void quandoRemoverPecaEPecaNaoEstaNaOsDeveLancarExcecao() {
            when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(os));

            assertThrows(IllegalArgumentException.class, () -> {
                ordemServicoService.removerPeca(1L, 1L);
            });
        }
    }

    @Nested
    @DisplayName("Testes para os métodos de busca e deleção")
    class TestesDeBuscaEDelecao {
        @Test
        @DisplayName("Deve retornar uma lista de todas as OSs")
        void listarTodasDeveRetornarListaDeOss() {
            when(ordemServicoRepository.findAll()).thenReturn(List.of(os));
            List<OrdemDeServico> resultado = ordemServicoService.listarTodas();

            assertFalse(resultado.isEmpty());
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Deve deletar uma OS com sucesso")
        void deletar_quandoIdExisteDeveRetornarTrue() {
            when(ordemServicoRepository.existsById(1L)).thenReturn(true);
            doNothing().when(ordemServicoRepository).deleteById(1L);
            boolean resultado = ordemServicoService.deletar(1L);
            assertTrue(resultado);
            verify(ordemServicoRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Não deve deletar e retornar false se a OS não existe")
        void deletar_quandoIdNaoExisteDeveRetornarFalse() {
            when(ordemServicoRepository.existsById(99L)).thenReturn(false);
            boolean resultado = ordemServicoService.deletar(99L);
            assertFalse(resultado);
            verify(ordemServicoRepository, never()).deleteById(99L);
        }
    }
}