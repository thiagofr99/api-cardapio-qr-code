package com.devthiagofurtado.cardapioqrcode.service;

import com.devthiagofurtado.cardapioqrcode.data.model.Cardapio;
import com.devthiagofurtado.cardapioqrcode.data.model.Empresa;
import com.devthiagofurtado.cardapioqrcode.data.model.Permission;
import com.devthiagofurtado.cardapioqrcode.data.model.User;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;
import com.devthiagofurtado.cardapioqrcode.exception.FileStorageException;
import com.devthiagofurtado.cardapioqrcode.modelCreator.EmpresaModelCreator;
import com.devthiagofurtado.cardapioqrcode.modelCreator.UserModelCreator;
import com.devthiagofurtado.cardapioqrcode.modelCreator.jsonTest.CardapioModelCreator;
import com.devthiagofurtado.cardapioqrcode.repository.CardapioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CardapioServiceTest {

    @InjectMocks
    private CardapioService cardapioService;

    @Mock
    private CardapioRepository cardapioRepository;

    @Mock
    private EmpresaService empresaService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        List<Permission> permission = UserModelCreator.permissions(PermissionVO.MANAGER);
        User user = UserModelCreator.cadastrado(LocalDate.now().plusDays(30),permission,true);
        Empresa empresa = EmpresaModelCreator.cadastrado(user,true);
        Cardapio cardapio = CardapioModelCreator.cadastrado(empresa,true);

        BDDMockito.when(cardapioRepository.save(ArgumentMatchers.any(Cardapio.class)))
                .thenReturn(cardapio);

        BDDMockito.when(cardapioRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(cardapio));

        BDDMockito.doNothing().when(cardapioRepository).delete(ArgumentMatchers.any(Cardapio.class));

        BDDMockito.doNothing().when(userService).validarUsuarioAdmin(ArgumentMatchers.anyString());

        BDDMockito.doNothing().when(userService).validarUsuarioAdminGerente(ArgumentMatchers.anyString(), ArgumentMatchers.any(Empresa.class));
    }

    @Test
    void salvar() throws IOException {
        Path path = Path.of("C:\\Users\\thiag\\Documents\\Meu Projeto\\upload\\test.txt");
        Files.createDirectories(path.toAbsolutePath());

        var teste = cardapioService.salvar(CardapioModelCreator.vo(1L,true,null),"teste");

        Files.delete(path.toAbsolutePath());

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getKey()).isNotNull();
    }

    @Test
    void salvar_retornaFileStorageException_erro() {

        var cardapio = CardapioModelCreator.voErro(1L,true,null);

        Assertions.assertThatThrownBy(()-> cardapioService.salvar(cardapio,"teste"))
                .isInstanceOf(FileStorageException.class);

    }

    @Test
    void deletar() throws IOException {
        Path path = Path.of("C:\\Users\\thiag\\Documents\\Meu Projeto\\upload\\test.txt");
        Files.createDirectories(path.toAbsolutePath());

        var teste = cardapioService.deletar(1L, "teste");
        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getMensagem()).isEqualTo("Registro de cardapio excluído com sucesso.");
        Assertions.assertThat(teste.getDateMensagem()).isNotNull();


    }

    @Test
    void deletar_retornaFileStorageException_erro() throws IOException {

        Assertions.assertThatThrownBy(()-> cardapioService.deletar(1L, "teste"))
                .isInstanceOf(FileStorageException.class);


    }

}