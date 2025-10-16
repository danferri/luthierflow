package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Peca;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para PecaService")
class PecaServiceTest {

    @Mock
    private PecaRepository pecaRepository;

    @InjectMocks
    private PecaService pecaService;

    private Peca peca;

    @BeforeEach
    void setUp() {
        peca = new Peca();
        peca.setId(1L);
        peca.setNomePeca("Potenciômetro");
        peca.setModelo("500K");
        peca.setFabricante("CTS");
        peca.setQtdEstoque(10);
        peca.setPrecoVenda(new BigDecimal("90.00"));
    }

    @Nested
    @DisplayName("Testes para o método salvar")
    class TestesDoMetodoSalvar {

        @Test
        @DisplayName("Deve salvar uma peça com sucesso quando os dados forem únicos")
        void quandoSalvarPecaComDadosUnicos_deveRetornarPecaSalva() {
            when(pecaRepository.findByNomePecaAndFabricanteAndModelo(
                    peca.getNomePeca(), peca.getFabricante(), peca.getModelo()))
                    .thenReturn(Optional.empty());

            when(pecaRepository.save(any(Peca.class))).thenReturn(peca);

            Peca resultado = pecaService.salvar(peca);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            assertEquals("Potenciômetro", resultado.getNomePeca());
        }

        @Test
        @DisplayName("Deve lançar uma exceção ao tentar salvar uma peça duplicada")
        void quandoSalvarPecaDuplicada_deveLancarExcecao() {
            when(pecaRepository.findByNomePecaAndFabricanteAndModelo(
                    peca.getNomePeca(), peca.getFabricante(), peca.getModelo()))
                    .thenReturn(Optional.of(peca));

            IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
                pecaService.salvar(peca);
            });

            assertEquals("Peça com mesmo nome, fabricante e modelo já cadastrada", excecao.getMessage());
        }
    }

    @Nested
    @DisplayName("Testes para o método atualizar")
    class TestesDoMetodoAtualizar {

        @Test
        @DisplayName("Deve atualizar os dados de uma peça existente")
        void quandoAtualizarPecaExistente_deveRetornarPecaAtualizada() {
            Peca dadosAtualizados = new Peca();
            dadosAtualizados.setPrecoVenda(new BigDecimal("95.50"));
            dadosAtualizados.setQtdEstoque(5);

            when(pecaRepository.findById(1L)).thenReturn(Optional.of(peca));
            when(pecaRepository.save(any(Peca.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Optional<Peca> resultado = pecaService.atualizar(1L, dadosAtualizados);

            assertTrue(resultado.isPresent());
            assertEquals(new BigDecimal("95.50"), resultado.get().getPrecoVenda());
            assertEquals(5, resultado.get().getQtdEstoque());
            assertEquals("Potenciômetro", resultado.get().getNomePeca());
        }
    }

    @Nested
    @DisplayName("Testes para os métodos de busca")
    class TestesDeBusca {

        @Test
        @DisplayName("Deve retornar uma peça quando o ID existir")
        void buscarPorId_quandoIdExiste_deveRetornarPeca() {
            when(pecaRepository.findById(1L)).thenReturn(Optional.of(peca));

            Optional<Peca> resultado = pecaService.buscarPorId(1L);

            assertTrue(resultado.isPresent());
            assertEquals(1L, resultado.get().getId());
        }

        @Test
        @DisplayName("Deve retornar vazio quando o ID não existir")
        void buscarPorId_quandoIdNaoExiste_deveRetornarOptionalVazio() {
            when(pecaRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Peca> resultado = pecaService.buscarPorId(99L);

            assertTrue(resultado.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar uma lista de todas as peças")
        void listarTodas_deveRetornarListaDePecas() {
            when(pecaRepository.findAll()).thenReturn(List.of(peca));

            List<Peca> resultado = pecaService.listarTodas();

            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Deve retornar uma lista vazia quando não houver peças")
        void listarTodas_quandoNaoHaPecas_deveRetornarListaVazia() {
            when(pecaRepository.findAll()).thenReturn(Collections.emptyList());

            List<Peca> resultado = pecaService.listarTodas();

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para o método deletar")
    class TestesDoMetodoDeletar {

        @Test
        @DisplayName("Deve deletar uma peça com sucesso quando o ID existir")
        void deletar_quandoIdExiste_deveRetornarTrue() {
            when(pecaRepository.existsById(1L)).thenReturn(true);
            doNothing().when(pecaRepository).deleteById(1L);

            boolean resultado = pecaService.deletar(1L);

            assertTrue(resultado);
            verify(pecaRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Não deve fazer nada e retornar false quando o ID não existir")
        void deletar_quandoIdNaoExiste_deveRetornarFalse() {
            when(pecaRepository.existsById(99L)).thenReturn(false);

            boolean resultado = pecaService.deletar(99L);

            assertFalse(resultado);
            verify(pecaRepository, never()).deleteById(99L);
        }
    }
}