package com.devthiagofurtado.cardapioqrcode.modelCreator;

import com.devthiagofurtado.cardapioqrcode.data.enums.TipoProdutoVO;
import com.devthiagofurtado.cardapioqrcode.data.model.Cardapio;
import com.devthiagofurtado.cardapioqrcode.data.model.Produto;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.ProdutoVO;

import java.time.LocalDate;

public class ProdutoModelCreator {

    private static final Cardapio cardapio = CardapioModelCreator.cadastrado(EmpresaModelCreator.cadastrado(UserModelCreator.cadastrado(LocalDate.now().plusDays(20), UserModelCreator.permissions(PermissionVO.MANAGER), true), true), false);

    public static Produto cadastrado(boolean disponivel) {

        return Produto.builder()
                .produtoNome("teste")
                .id(1L)
                .dataCadastro(LocalDate.now())
                .tipoProdutoVO(TipoProdutoVO.PROMOCAO)
                .valorProduto(1.99)
                .cardapio(cardapio)
                .disponivel(disponivel)
                .build();
    }

    public static ProdutoVO vo(Long key, boolean disponivel) {
        return ProdutoVO.builder()
                .cardapioId(2L)
                .dataCadastro(LocalDate.now())
                .produtoNome("teste")
                .tipoProdutoVO("PROMOCAO")
                .valorProduto(1.99)
                .key(key)
                .disponivel(disponivel)
                .build();
    }

}
