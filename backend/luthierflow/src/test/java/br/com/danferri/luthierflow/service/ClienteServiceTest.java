package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Cliente;
import br.com.danferri.luthierflow.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para ClienteService")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João da Silva");
        cliente.setCpf("111.222.333-44");
        cliente.setEmail("joao.silva@email.com");
    }

    @Nested
    @DisplayName("Testes para o método salvar")
    class TestesDoMetodoSalvar {

        @Test
        @DisplayName("Deve salvar um cliente com sucesso quando o CPF for único")
        void quandoSalvarClienteComCpfUnico_deveRetornarClienteSalvo() {
            when(clienteRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.empty());
            when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

            Cliente resultado = clienteService.salvar(cliente);

            assertNotNull(resultado);
            assertEquals("João da Silva", resultado.getNome());
        }

        @Test
        @DisplayName("Deve lançar uma exceção ao tentar salvar um cliente com CPF duplicado")
        void quandoSalvarClienteComCpfDuplicado_deveLancarExcecao() {
            when(clienteRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.of(cliente));

            IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
                clienteService.salvar(cliente);
            });

            assertEquals("CPF já cadastrado no sistema", excecao.getMessage());
        }
    }

    @Nested
    @DisplayName("Testes para o método atualizar")
    class TestesDoMetodoAtualizar {

        @Test
        @DisplayName("Deve atualizar os dados de um cliente existente")
        void quandoAtualizarClienteExistente_deveRetornarClienteAtualizado() {
            Cliente dadosAtualizados = new Cliente();
            dadosAtualizados.setNome("João da Silva Santos");
            dadosAtualizados.setEmail("joao.santos@email.com");

            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Optional<Cliente> resultado = clienteService.atualizar(1L, dadosAtualizados);

            assertTrue(resultado.isPresent());
            assertEquals("João da Silva Santos", resultado.get().getNome());
            assertEquals("joao.santos@email.com", resultado.get().getEmail());
            assertEquals("111.222.333-44", resultado.get().getCpf());
        }

        @Test
        @DisplayName("Não deve fazer nada ao tentar atualizar um cliente inexistente")
        void quandoAtualizarClienteInexistente_deveRetornarOptionalVazio() {
            when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Cliente> resultado = clienteService.atualizar(99L, new Cliente());

            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para os métodos de busca")
    class TestesDeBusca {

        @Test
        @DisplayName("Deve retornar um cliente quando o ID existir")
        void buscarPorId_quandoIdExiste_deveRetornarCliente() {
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

            Optional<Cliente> resultado = clienteService.buscarPorId(1L);

            assertTrue(resultado.isPresent());
            assertEquals(1L, resultado.get().getId());
        }

        @Test
        @DisplayName("Deve retornar vazio quando o ID não existir")
        void buscarPorId_quandoIdNaoExiste_deveRetornarOptionalVazio() {
            when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Cliente> resultado = clienteService.buscarPorId(99L);

            assertTrue(resultado.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar uma lista de todos os clientes")
        void listarTodosClientes_deveRetornarListaDeClientes() {
            when(clienteRepository.findAll()).thenReturn(List.of(cliente));

            List<Cliente> resultado = clienteService.listarTodosClientes();

            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Deve retornar uma lista vazia quando não houver clientes")
        void listarTodosClientes_quandoNaoHaClientes_deveRetornarListaVazia() {
            when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

            List<Cliente> resultado = clienteService.listarTodosClientes();

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para o método deletar")
    class TestesDoMetodoDeletar {

        @Test
        @DisplayName("Deve deletar um cliente com sucesso quando o ID existir")
        void deletar_quandoIdExiste_deveChamarDeleteById() {
            when(clienteRepository.existsById(1L)).thenReturn(true);
            doNothing().when(clienteRepository).deleteById(1L);

            clienteService.deletar(1L);

            verify(clienteRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Não deve chamar o delete quando o ID não existir")
        void deletar_quandoIdNaoExiste_naoDeveChamarDeleteById() {
            when(clienteRepository.existsById(99L)).thenReturn(false);

            clienteService.deletar(99L);

            verify(clienteRepository, never()).deleteById(99L);
        }
    }
}