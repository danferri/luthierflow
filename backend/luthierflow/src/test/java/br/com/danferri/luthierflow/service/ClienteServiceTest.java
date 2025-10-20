package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Cliente;
import br.com.danferri.luthierflow.dto.ClienteRequestDTO;
import br.com.danferri.luthierflow.dto.ClienteResponseDTO;
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

    private ClienteRequestDTO clienteRequestDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João da Silva");
        cliente.setCpf("11122233344");
        cliente.setEmail("joao.silva@email.com");
        cliente.setCep("13560-000");

        clienteRequestDTO = new ClienteRequestDTO();
        clienteRequestDTO.setNome("João da Silva");
        clienteRequestDTO.setCpf("11122233344");
        clienteRequestDTO.setEmail("joao.silva@email.com");
        clienteRequestDTO.setCep("13560-000");
    }

    @Nested
    @DisplayName("Testes para o método salvar")
    class TestesDoMetodoSalvar {

        @Test
        @DisplayName("Deve salvar um cliente com sucesso")
        void quandoSalvarCliente_deveRetornarClienteResponseDTO() {
            when(clienteRepository.findByCpf(clienteRequestDTO.getCpf())).thenReturn(Optional.empty());
            when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

            ClienteResponseDTO resultado = clienteService.salvar(clienteRequestDTO);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            assertEquals("João da Silva", resultado.getNome());
        }

        @Test
        @DisplayName("Deve lançar uma exceção ao tentar salvar um cliente com CPF duplicado")
        void quandoSalvarClienteComCpfDuplicado_deveLancarExcecao() {
            when(clienteRepository.findByCpf(clienteRequestDTO.getCpf())).thenReturn(Optional.of(cliente));

            IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
                clienteService.salvar(clienteRequestDTO);
            });

            assertEquals("CPF já cadastrado no sistema", excecao.getMessage());
            verify(clienteRepository, never()).save(any(Cliente.class));
        }
    }

    @Nested
    @DisplayName("Testes para o método atualizar")
    class TestesDoMetodoAtualizar {

        @Test
        @DisplayName("Deve atualizar os dados de um cliente existente")
        void quandoAtualizarClienteExistente_deveRetornarClienteResponseDTO() {
            ClienteRequestDTO dadosAtualizadosDTO = new ClienteRequestDTO();
            dadosAtualizadosDTO.setNome("João da Silva Santos");
            dadosAtualizadosDTO.setEmail("joao.santos@email.com");

            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Optional<ClienteResponseDTO> resultado = clienteService.atualizar(1L, dadosAtualizadosDTO);

            assertTrue(resultado.isPresent());
            assertEquals("João da Silva Santos", resultado.get().getNome());
            assertEquals("joao.santos@email.com", resultado.get().getEmail());
        }

        @Test
        @DisplayName("Não deve fazer nada ao tentar atualizar um cliente inexistente")
        void quandoAtualizarClienteInexistente_deveRetornarOptionalVazio() {
            when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<ClienteResponseDTO> resultado = clienteService.atualizar(99L, new ClienteRequestDTO());

            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para os métodos de busca")
    class TestesDeBusca {

        @Test
        @DisplayName("Deve retornar um DTO de cliente quando o ID existir")
        void buscarPorId_quandoIdExiste_deveRetornarClienteResponseDTO() {
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

            Optional<ClienteResponseDTO> resultado = clienteService.buscarPorId(1L);

            assertTrue(resultado.isPresent());
            assertEquals(1L, resultado.get().getId());
        }

        @Test
        @DisplayName("Deve retornar vazio quando o ID não existir")
        void buscarPorId_quandoIdNaoExiste_deveRetornarOptionalVazio() {
            when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<ClienteResponseDTO> resultado = clienteService.buscarPorId(99L);

            assertTrue(resultado.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar uma lista de DTOs de todos os clientes")
        void listarTodosClientes_deveRetornarListaDeDTOs() {
            when(clienteRepository.findAll()).thenReturn(List.of(cliente));

            List<ClienteResponseDTO> resultado = clienteService.listarTodosClientes();

            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Deve retornar uma lista vazia quando não houver clientes")
        void listarTodosClientes_quandoNaoHaClientes_deveRetornarListaVazia() {
            when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

            List<ClienteResponseDTO> resultado = clienteService.listarTodosClientes();

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para o método deletar")
    class TestesDoMetodoDeletar {

        @Test
        @DisplayName("Deve chamar o método deleteById do repositório")
        void deletar_quandoChamado_deveChamarDeleteById() {
            clienteService.deletar(1L);
            verify(clienteRepository, times(1)).deleteById(1L);
        }
    }
}