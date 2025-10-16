package br.com.danferri.luthierflow.service;

import br.com.danferri.luthierflow.domain.Instrumento;
import br.com.danferri.luthierflow.domain.OrdemDeServico;
import br.com.danferri.luthierflow.domain.ProjetoPortfolio;
import br.com.danferri.luthierflow.domain.enums.StatusOS;
import br.com.danferri.luthierflow.repository.OrdemServicoRepository;
import br.com.danferri.luthierflow.repository.ProjetoPortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para ProjetoPortfolioService")
class ProjetoPortfolioServiceTest {

    @Mock
    private ProjetoPortfolioRepository projetoPortfolioRepository;

    @Mock
    private OrdemServicoRepository ordemServicoRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ProjetoPortfolioService projetoPortfolioService;

    private OrdemDeServico osFinalizada;
    private OrdemDeServico osEmAndamento;
    private ProjetoPortfolio projeto;

    @BeforeEach
    void setUp() {
        Instrumento instrumento = new Instrumento();
        instrumento.setMarca("Fender");

        osFinalizada = new OrdemDeServico();
        osFinalizada.setId(1L);
        osFinalizada.setStatus(StatusOS.FINALIZADO);
        osFinalizada.setTipoServico("Regulagem");
        osFinalizada.setInstrumento(instrumento);

        osEmAndamento = new OrdemDeServico();
        osEmAndamento.setId(2L);
        osEmAndamento.setStatus(StatusOS.EM_ANDAMENTO);

        projeto = new ProjetoPortfolio();
        projeto.setId(1L);
        projeto.setOrdemDeServico(osFinalizada);
        projeto.setStatusPublicacao("RASCUNHO");
    }

    @Nested
    @DisplayName("Testes para o método promoverParaPortfolio")
    class TestesDoMetodoPromover {

        @Test
        @DisplayName("Deve promover uma OS finalizada com sucesso")
        void quandoPromoverOsFinalizada_deveCriarProjeto() {
            when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(osFinalizada));
            when(projetoPortfolioRepository.findByOrdemDeServicoId(1L)).thenReturn(Optional.empty());
            when(projetoPortfolioRepository.save(any(ProjetoPortfolio.class))).thenAnswer(i -> i.getArgument(0));

            ProjetoPortfolio resultado = projetoPortfolioService.promoverParaPortfolio(1L);

            assertNotNull(resultado);
            assertEquals("RASCUNHO", resultado.getStatusPublicacao());
            assertEquals(osFinalizada, resultado.getOrdemDeServico());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar promover uma OS não finalizada")
        void quandoPromoverOsNaoFinalizada_deveLancarExcecao() {
            when(ordemServicoRepository.findById(2L)).thenReturn(Optional.of(osEmAndamento));
            when(projetoPortfolioRepository.findByOrdemDeServicoId(2L)).thenReturn(Optional.empty());

            IllegalStateException excecao = assertThrows(IllegalStateException.class, () -> {
                projetoPortfolioService.promoverParaPortfolio(2L);
            });
            assertEquals("Apenas Ordens de Serviço com status FINALIZADO podem ser adicionadas ao portfólio.", excecao.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar promover uma OS já promovida")
        void quandoPromoverOsJaPromovida_deveLancarExcecao() {
            when(projetoPortfolioRepository.findByOrdemDeServicoId(1L)).thenReturn(Optional.of(new ProjetoPortfolio()));

            assertThrows(IllegalStateException.class, () -> {
                projetoPortfolioService.promoverParaPortfolio(1L);
            });
        }
    }

    @Nested
    @DisplayName("Testes para o método atualizar")
    class TestesDoMetodoAtualizar {

        @Test
        @DisplayName("Deve atualizar o título e a descrição de um projeto existente")
        void quandoAtualizar_deveAlterarDados() {
            ProjetoPortfolio dadosAtualizados = new ProjetoPortfolio();
            dadosAtualizados.setTituloPublico("Novo Título");
            dadosAtualizados.setDescricaoPublica("Nova Descrição");

            when(projetoPortfolioRepository.findById(1L)).thenReturn(Optional.of(projeto));
            when(projetoPortfolioRepository.save(any(ProjetoPortfolio.class))).thenAnswer(i -> i.getArgument(0));

            Optional<ProjetoPortfolio> resultado = projetoPortfolioService.atualizar(1L, dadosAtualizados);

            assertTrue(resultado.isPresent());
            assertEquals("Novo Título", resultado.get().getTituloPublico());
            assertEquals("Nova Descrição", resultado.get().getDescricaoPublica());
        }

        @Test
        @DisplayName("Deve setar a data de publicação ao mudar o status para PUBLICADO")
        void quandoPublicarProjeto_deveSetarDataPublicacao() {
            ProjetoPortfolio dadosAtualizados = new ProjetoPortfolio();
            dadosAtualizados.setStatusPublicacao("PUBLICADO");

            when(projetoPortfolioRepository.findById(1L)).thenReturn(Optional.of(projeto));
            when(projetoPortfolioRepository.save(any(ProjetoPortfolio.class))).thenAnswer(i -> i.getArgument(0));

            Optional<ProjetoPortfolio> resultado = projetoPortfolioService.atualizar(1L, dadosAtualizados);

            assertTrue(resultado.isPresent());
            assertEquals("PUBLICADO", resultado.get().getStatusPublicacao());
            assertEquals(LocalDate.now(), resultado.get().getDataPublicacao());
        }
    }

    @Nested
    @DisplayName("Testes para o método adicionarFoto")
    class TestesDoMetodoAdicionarFoto {

        @Test
        @DisplayName("Deve adicionar uma foto a um projeto existente")
        void quandoAdicionarFoto_deveSalvarEAssociarFoto() {
            MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "some-image-content".getBytes());
            String nomeArquivoSalvo = "uuid-test.jpg";

            when(projetoPortfolioRepository.findById(1L)).thenReturn(Optional.of(projeto));
            when(fileStorageService.storeFile(mockFile)).thenReturn(nomeArquivoSalvo);
            when(projetoPortfolioRepository.save(any(ProjetoPortfolio.class))).thenReturn(projeto);

            ProjetoPortfolio resultado = projetoPortfolioService.adicionarFoto(1L, mockFile, "Legenda Teste", 1);

            assertNotNull(resultado);
            assertEquals(1, resultado.getFotos().size());
            assertEquals(nomeArquivoSalvo, resultado.getFotos().get(0).getUrlImagem());
        }
    }

    @Nested
    @DisplayName("Testes para o método deletar")
    class TestesDoMetodoDeletar {

        @Test
        @DisplayName("Deve deletar um projeto com sucesso quando o ID existir")
        void deletar_quandoIdExiste_deveChamarDelete() {
            when(projetoPortfolioRepository.existsById(1L)).thenReturn(true);
            doNothing().when(projetoPortfolioRepository).deleteById(1L);

            assertDoesNotThrow(() -> {
                projetoPortfolioService.deletar(1L);
            });

            verify(projetoPortfolioRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar um projeto inexistente")
        void deletar_quandoIdNaoExiste_deveLancarExcecao() {
            when(projetoPortfolioRepository.existsById(99L)).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () -> {
                projetoPortfolioService.deletar(99L);
            });
        }
    }

    @Nested
    @DisplayName("Testes para os métodos de busca")
    class TestesDeBusca {

        @Test
        @DisplayName("Deve retornar uma lista de projetos publicados")
        void listarPublicados_deveRetornarApenasProjetosPublicados() {
            ProjetoPortfolio projetoPublicado = new ProjetoPortfolio();
            projetoPublicado.setStatusPublicacao("PUBLICADO");

            when(projetoPortfolioRepository.findByStatusPublicacao("PUBLICADO")).thenReturn(List.of(projetoPublicado));

            List<ProjetoPortfolio> resultado = projetoPortfolioService.listarPublicados();

            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertEquals("PUBLICADO", resultado.get(0).getStatusPublicacao());
        }
    }
}