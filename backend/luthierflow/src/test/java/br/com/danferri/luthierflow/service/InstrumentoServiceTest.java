package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Cliente;
import br.com.danferri.luthierflow.domain.Instrumento;
import br.com.danferri.luthierflow.repository.ClienteRepository;
import br.com.danferri.luthierflow.repository.InstrumentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para InstrumentoService")
class InstrumentoServiceTest {

    @Mock
    private InstrumentoRepository instrumentoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private InstrumentoService instrumentoService;

    private Cliente cliente;
    private Instrumento instrumento;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");

        instrumento = new Instrumento();
        instrumento.setId(1L);
        instrumento.setMarca("Fender");
        instrumento.setModelo("Stratocaster");
        instrumento.setNumeroSerie("123456");
        instrumento.setCliente(cliente);
    }

    @Nested
    @DisplayName("Testes para o método salvar")
    class TestesDoMetodoSalvar {

        @Test
        @DisplayName("Deve salvar um instrumento com sucesso")
        void quandoSalvarInstrumento_deveRetornarInstrumentoSalvo() {
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(instrumentoRepository.findByNumeroSerieAndClienteId(instrumento.getNumeroSerie(), 1L))
                    .thenReturn(Optional.empty());
            when(instrumentoRepository.save(any(Instrumento.class))).thenReturn(instrumento);

            Optional<Instrumento> resultado = instrumentoService.salvar(1L, instrumento);

            assertTrue(resultado.isPresent());
            assertEquals(1L, resultado.get().getId());
            assertEquals(1L, resultado.get().getCliente().getId());
        }

        @Test
        @DisplayName("Deve retornar vazio ao tentar salvar para um cliente inexistente")
        void quandoSalvarInstrumento_eClienteNaoExiste_deveRetornarOptionalVazio() {
            when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Instrumento> resultado = instrumentoService.salvar(99L, instrumento);

            assertTrue(resultado.isEmpty());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar salvar instrumento com número de série duplicado para o mesmo cliente")
        void quandoSalvarInstrumento_comNumeroSerieDuplicado_deveLancarExcecao() {
            when(instrumentoRepository.findByNumeroSerieAndClienteId(instrumento.getNumeroSerie(), 1L))
                    .thenReturn(Optional.of(instrumento));

            IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
                instrumentoService.salvar(1L, instrumento);
            });
            assertEquals("Instrumento com o mesmo número de série já cadastrado para este cliente.", excecao.getMessage());
        }
    }

    @Nested
    @DisplayName("Testes para o método atualizar")
    class TestesDoMetodoAtualizar {

        @Test
        @DisplayName("Deve atualizar um instrumento com sucesso")
        void quandoAtualizarInstrumento_deveRetornarInstrumentoAtualizado() {
            Instrumento dadosAtualizados = new Instrumento();
            dadosAtualizados.setMarca("Gibson");

            when(instrumentoRepository.findById(1L)).thenReturn(Optional.of(instrumento));
            when(instrumentoRepository.save(any(Instrumento.class))).thenAnswer(i -> i.getArgument(0));

            Optional<Instrumento> resultado = instrumentoService.atualizar(1L, 1L, dadosAtualizados);

            assertTrue(resultado.isPresent());
            assertEquals("Gibson", resultado.get().getMarca());
        }

        @Test
        @DisplayName("Não deve atualizar se o instrumento não pertence ao cliente informado")
        void quandoAtualizarInstrumento_eNaoPertenceAoCliente_deveRetornarOptionalVazio() {
            when(instrumentoRepository.findById(1L)).thenReturn(Optional.of(instrumento));

            Optional<Instrumento> resultado = instrumentoService.atualizar(99L, 1L, new Instrumento());

            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para o método deletar")
    class TestesDoMetodoDeletar {

        @Test
        @DisplayName("Deve deletar um instrumento com sucesso")
        void quandoDeletarInstrumento_deveRetornarTrue() {
            when(instrumentoRepository.findById(1L)).thenReturn(Optional.of(instrumento));
            doNothing().when(instrumentoRepository).deleteById(1L);

            boolean resultado = instrumentoService.deletar(1L, 1L);

            assertTrue(resultado);
            verify(instrumentoRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Não deve deletar e retornar false se o instrumento não pertence ao cliente")
        void quandoDeletarInstrumento_eNaoPertenceAoCliente_deveRetornarFalse() {
            when(instrumentoRepository.findById(1L)).thenReturn(Optional.of(instrumento));

            boolean resultado = instrumentoService.deletar(99L, 1L);

            assertFalse(resultado);
            verify(instrumentoRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Deve retornar false se o instrumento não for encontrado")
        void quandoDeletarInstrumento_eNaoExiste_deveRetornarFalse() {
            when(instrumentoRepository.findById(99L)).thenReturn(Optional.empty());

            boolean resultado = instrumentoService.deletar(1L, 99L);

            assertFalse(resultado);
        }
    }

    @Nested
    @DisplayName("Testes para os métodos de busca")
    class TestesDeBusca {

        @Test
        @DisplayName("Deve listar os instrumentos de um cliente existente")
        void quandoListarPorCliente_eClienteExiste_deveRetornarLista() {
            when(clienteRepository.existsById(1L)).thenReturn(true);
            when(instrumentoRepository.findByClienteId(1L)).thenReturn(List.of(instrumento));

            List<Instrumento> resultado = instrumentoService.listarPorCliente(1L);

            assertFalse(resultado.isEmpty());
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Deve retornar lista vazia ao listar instrumentos de um cliente inexistente")
        void quandoListarPorCliente_eClienteNaoExiste_deveRetornarListaVazia() {
            when(clienteRepository.existsById(99L)).thenReturn(false);

            List<Instrumento> resultado = instrumentoService.listarPorCliente(99L);

            assertTrue(resultado.isEmpty());
        }
    }
}