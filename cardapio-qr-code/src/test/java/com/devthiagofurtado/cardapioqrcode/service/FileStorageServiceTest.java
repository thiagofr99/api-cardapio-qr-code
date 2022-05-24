package com.devthiagofurtado.cardapioqrcode.service;

import com.devthiagofurtado.cardapioqrcode.config.FileStorageConfig;
import com.devthiagofurtado.cardapioqrcode.exception.FileStorageException;
import com.devthiagofurtado.cardapioqrcode.exception.MyFileNotFoundException;
import com.devthiagofurtado.cardapioqrcode.modelCreator.FileStorageModelCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ExtendWith(SpringExtension.class)
class FileStorageServiceTest {

    private Path fileStorageLocation;

    @InjectMocks
    FileStorageService fileStorageService = new FileStorageService(new FileStorageConfig("C:\\Users\\thiag\\Documents\\Meu Projeto\\upload"));


    @BeforeEach
    void setUp() {

    }

    @Test
    void storeFile() {

        var teste = fileStorageService.storeFile(new MockMultipartFile("foo", "bar/../foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()));

        Assertions.assertThat(teste).isNotNull();

    }

    @Test
    void storeFile_erro_invalidPathSequence() {

        var mult = FileStorageModelCreator.arquivo("teste..pdf");

        Assertions.assertThatThrownBy(() -> fileStorageService.storeFile(mult))
                .isInstanceOf(FileStorageException.class);

    }

    @Test
    void storeFile_erro() {

        var mult = FileStorageModelCreator.arquivo("teste.pdf");

        Assertions.assertThatThrownBy(() -> fileStorageService.storeFile(mult))
                .isInstanceOf(FileStorageException.class);

    }


    @Test
    void loadFileAsResource() throws IOException {
        Path path = Path.of("C:\\Users\\thiag\\Documents\\Meu Projeto\\upload\\test.txt");
        Files.createDirectories(path.toAbsolutePath());

        var teste = fileStorageService.loadFileAsResource("test.txt");

        Assertions.assertThat(teste).isNotNull();

    }

    @Test
    void loadFileAsResource_retornaMyFileNotFoundException() {

        Assertions.assertThatThrownBy(() -> fileStorageService.loadFileAsResource("tesasaft.txt"))
                .isInstanceOf(MyFileNotFoundException.class);

    }
}