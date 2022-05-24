package com.devthiagofurtado.cardapioqrcode.service;

import com.devthiagofurtado.cardapioqrcode.data.model.Cardapio;
import com.devthiagofurtado.cardapioqrcode.data.model.Produto;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;
import com.devthiagofurtado.cardapioqrcode.exception.ResourceBadRequestException;
import com.devthiagofurtado.cardapioqrcode.exception.ResourceNotFoundException;
import com.devthiagofurtado.cardapioqrcode.modelCreator.CardapioModelCreator;
import com.devthiagofurtado.cardapioqrcode.modelCreator.EmpresaModelCreator;
import com.devthiagofurtado.cardapioqrcode.modelCreator.ProdutoModelCreator;
import com.devthiagofurtado.cardapioqrcode.modelCreator.UserModelCreator;
import com.devthiagofurtado.cardapioqrcode.repository.CardapioRepository;
import com.devthiagofurtado.cardapioqrcode.repository.ProdutoRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private UserService userService;

    @Mock
    private CardapioService cardapioService;

    @BeforeEach
    void setUp() {
        Cardapio cardapio = CardapioModelCreator.cadastrado(EmpresaModelCreator.cadastrado(UserModelCreator.cadastrado(LocalDate.now().plusDays(20), UserModelCreator.permissions(PermissionVO.MANAGER), true), true), false);

        BDDMockito.when(produtoRepository.save(ArgumentMatchers.any(Produto.class)))
                .thenReturn(ProdutoModelCreator.cadastrado(true));

        BDDMockito.when(produtoRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(ProdutoModelCreator.cadastrado(true)));

        BDDMockito.doNothing().when(produtoRepository).delete(ArgumentMatchers.any(Produto.class));

        BDDMockito.when(produtoRepository.findAllByCardapio(ArgumentMatchers.any(Cardapio.class)))
                .thenReturn(Collections.singletonList(ProdutoModelCreator.cadastrado(true)));

        BDDMockito.when(cardapioService.findByIdEntity(ArgumentMatchers.anyLong()))
                .thenReturn(cardapio);

    }

    @Test
    void salvar() {


        var vo = ProdutoModelCreator.vo(null,true);

        var teste = produtoService.salvar(vo,"teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getKey()).isNotNull();
        Assertions.assertThat(teste.getProdutoNome()).isNotNull();

    }

    @Test
    void salvar_returnVo_sucessoAtualizar() {


        var vo = ProdutoModelCreator.vo(1L,true);

        var teste = produtoService.salvar(vo,"teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getKey()).isNotNull();
        Assertions.assertThat(teste.getProdutoNome()).isNotNull();

    }

    @Test
    void salvar_retornaResourceBadRequestExceptional_erro() {
        Cardapio cardapio = CardapioModelCreator.cadastrado(EmpresaModelCreator.cadastrado(UserModelCreator.cadastrado(LocalDate.now().plusDays(20), UserModelCreator.permissions(PermissionVO.MANAGER), true), true), true);

        BDDMockito.when(cardapioService.findByIdEntity(ArgumentMatchers.anyLong()))
                .thenReturn(cardapio);

        var vo = ProdutoModelCreator.vo(null,true);

         Assertions.assertThatThrownBy(()-> produtoService.salvar(vo,"teste"))
                 .isInstanceOf(ResourceBadRequestException.class);

    }

    @Test
    void findById_retornaVO_sucesso() {

        var teste = produtoService.findById(1L,"teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getKey()).isNotNull();
        Assertions.assertThat(teste.getProdutoNome()).isNotNull();

    }

    @Test
    void findById_retornaResourceNotFoundException_erro() {
        BDDMockito.when(produtoRepository.findById(ArgumentMatchers.anyLong()))
                        .thenReturn(Optional.empty());

       Assertions.assertThatThrownBy(()-> produtoService.findById(1L,"teste"))
               .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void deletar_retornaMensagemCustom_sucesso() {

        var teste = produtoService.deletar(1L, "teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getMensagem()).isEqualTo("Registro de produto excluído com sucesso.");
        Assertions.assertThat(teste.getDateMensagem()).isNotNull();

    }

    @Test
    void findAllByCardapi_retornaListVO_sucesso() {

        var teste = produtoService.findAllByCardapio(1L,"teste");

        Assertions.assertThat(teste).isNotEmpty();
        Assertions.assertThat(teste.get(0).getKey()).isNotNull();
    }

    @Test
    void disponibilidade() {
        BDDMockito.when(produtoRepository.save(ArgumentMatchers.any(Produto.class)))
                .thenReturn(ProdutoModelCreator.cadastrado(false));

        var teste = produtoService.disponibilidade(1L,"teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getKey()).isNotNull();
        Assertions.assertThat(teste.getDisponivel()).isFalse();

    }

    @Test
    void disponibilidade_isTrue() {
        BDDMockito.when(produtoRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(ProdutoModelCreator.cadastrado(false)));
        BDDMockito.when(produtoRepository.save(ArgumentMatchers.any(Produto.class)))
                .thenReturn(ProdutoModelCreator.cadastrado(true));

        var teste = produtoService.disponibilidade(1L,"teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getKey()).isNotNull();
        Assertions.assertThat(teste.getDisponivel()).isTrue();

    }
}