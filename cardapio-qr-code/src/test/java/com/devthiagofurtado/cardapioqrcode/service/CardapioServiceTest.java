package com.devthiagofurtado.cardapioqrcode.service;

import com.devthiagofurtado.cardapioqrcode.data.model.Cardapio;
import com.devthiagofurtado.cardapioqrcode.data.model.Empresa;
import com.devthiagofurtado.cardapioqrcode.data.model.Permission;
import com.devthiagofurtado.cardapioqrcode.data.model.User;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;
import com.devthiagofurtado.cardapioqrcode.exception.FileStorageException;
import com.devthiagofurtado.cardapioqrcode.modelCreator.EmpresaModelCreator;
import com.devthiagofurtado.cardapioqrcode.modelCreator.UserModelCreator;
import com.devthiagofurtado.cardapioqrcode.modelCreator.CardapioModelCreator;
import com.devthiagofurtado.cardapioqrcode.repository.CardapioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

        BDDMockito.when(cardapioRepository.findAllByEmpresa(ArgumentMatchers.any(Empresa.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(cardapio)));

        BDDMockito.when(empresaService.findByIdEntity(ArgumentMatchers.anyLong()))
                .thenReturn(empresa);
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
        List<Permission> permission = UserModelCreator.permissions(PermissionVO.MANAGER);
        User user = UserModelCreator.cadastrado(LocalDate.now().plusDays(30),permission,true);
        Empresa empresa = EmpresaModelCreator.cadastrado(user,true);
        Cardapio cardapio = CardapioModelCreator.cadastrado(empresa,true,"erro.txt");

        BDDMockito.when(cardapioRepository.findById(ArgumentMatchers.anyLong()))
                        .thenReturn(Optional.of(cardapio));

        Assertions.assertThatThrownBy(()-> cardapioService.deletar(1L, "teste"))
                .isInstanceOf(FileStorageException.class);


    }

    @Test
    void findAllByEmpresa(){
        Pageable pageable = PageRequest.of(1, 12);

        var teste = cardapioService.findAllByEmpresa(1L,pageable,"teste");

        Assertions.assertThat(teste).isNotEmpty().isNotNull();
        Assertions.assertThat(teste.get().collect(Collectors.toList()).get(0).getKey()).isNotNull();

    }

    @Test
    void findById() {
        var teste = cardapioService.findById(1L,"teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getKey()).isNotNull();
        Assertions.assertThat(teste.getCardapioNome()).isNotNull();


    }

}