package br.com.danferri.luthierflow.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para FileStorageService")
class FileStorageServiceTest {

    @TempDir
    Path tempDir;

    @Nested
    @DisplayName("Testes de Sucesso")
    class TestesDeSucesso {

        @Test
        @DisplayName("Deve salvar um arquivo com extensão com sucesso")
        void storeFile_quandoArquivoValidoComExtensao_deveSalvarArquivo() throws IOException {
            FileStorageService fileStorageService = new FileStorageService(tempDir.toString());
            MockMultipartFile mockFile = new MockMultipartFile(
                    "file", "minha-imagem.jpg", "image/jpeg", "conteudo-da-imagem".getBytes()
            );

            String fileName = fileStorageService.storeFile(mockFile);

            assertNotNull(fileName);
            assertTrue(fileName.endsWith(".jpg"));
            assertTrue(Files.exists(tempDir.resolve(fileName)));
            assertEquals("conteudo-da-imagem", Files.readString(tempDir.resolve(fileName)));
        }

        @Test
        @DisplayName("Deve salvar um arquivo sem extensão com sucesso")
        void storeFile_quandoArquivoSemExtensao_deveSalvarArquivo() throws IOException {
            FileStorageService fileStorageService = new FileStorageService(tempDir.toString());
            MockMultipartFile mockFile = new MockMultipartFile(
                    "file", "arquivo-sem-extensao", "application/octet-stream", "conteudo".getBytes()
            );

            String fileName = fileStorageService.storeFile(mockFile);

            assertNotNull(fileName);
            assertFalse(fileName.endsWith("."));
            assertTrue(Files.exists(tempDir.resolve(fileName)));
        }
    }

    @Nested
    @DisplayName("Testes de Falha")
    class TestesDeFalha {

        @Test
        @DisplayName("Deve lançar uma exceção para nomes de arquivo inválidos (path traversal)")
        void storeFile_quandoNomeContemPathTraversal_deveLancarExcecao() {
            FileStorageService fileStorageService = new FileStorageService(tempDir.toString());
            MockMultipartFile mockFile = new MockMultipartFile("file", "../invalid.txt", "text/plain", "conteudo".getBytes());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                fileStorageService.storeFile(mockFile);
            });

            assertTrue(exception.getMessage().contains("Nome de arquivo inválido"));
        }

        @Test
        @DisplayName("Deve lançar uma exceção se o arquivo estiver vazio")
        void storeFile_quandoArquivoVazio_deveLancarExcecao() {
            FileStorageService fileStorageService = new FileStorageService(tempDir.toString());
            MockMultipartFile mockFile = new MockMultipartFile("file", "empty.txt", "text/plain", new byte[0]);

            assertThrows(RuntimeException.class, () -> {
                fileStorageService.storeFile(mockFile);
            });
        }
    }
}