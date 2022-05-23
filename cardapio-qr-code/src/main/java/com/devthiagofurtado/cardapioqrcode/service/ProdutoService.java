package com.devthiagofurtado.cardapioqrcode.service;

import com.devthiagofurtado.cardapioqrcode.converter.DozerConverter;
import com.devthiagofurtado.cardapioqrcode.data.enums.TipoProdutoVO;
import com.devthiagofurtado.cardapioqrcode.data.model.Produto;
import com.devthiagofurtado.cardapioqrcode.data.vo.ProdutoVO;
import com.devthiagofurtado.cardapioqrcode.exception.ResourceBadRequestException;
import com.devthiagofurtado.cardapioqrcode.exception.ResourceNotFoundException;
import com.devthiagofurtado.cardapioqrcode.repository.ProdutoRepository;
import com.devthiagofurtado.cardapioqrcode.util.MensagemCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CardapioService cardapioService;

    @Autowired
    private UserService userService;


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ProdutoVO salvar(ProdutoVO produtoVO, String userName) {

        var cardapio = cardapioService.findByIdEntity(produtoVO.getCardapioId());

        if (StringUtils.hasText(cardapio.getUrlQrcode()))
            throw new ResourceBadRequestException("Inclusão de Produtos disponível apenas para cardápios gerados na plataforma.");

        userService.validarUsuarioGerente(userName, cardapio.getEmpresa());

        var produto = new Produto();

        if (produtoVO.getKey() != null) {
            produto = produtoRepository.findById(produtoVO.getKey()).orElseThrow(() -> new ResourceNotFoundException("Produto não foi localizado por ID."));
            this.atualizarProduto(produtoVO, produto);

        } else {
            produtoVO.setDataCadastro(LocalDate.now());
            produtoVO.setDisponivel(true); //true por padrão ao cadastrar

            produto = DozerConverter.parseObject(produtoVO, Produto.class);
        }

        produto.setCardapio(cardapio);
        var produtoSave = produtoRepository.save(produto);
        return DozerConverter.parseObject(produtoSave, ProdutoVO.class);

    }

    private void atualizarProduto(ProdutoVO vo, Produto produto) {
        produto.setProdutoNome(vo.getProdutoNome());
        produto.setValorProduto(vo.getValorProduto());
        produto.setTipoProdutoVO(TipoProdutoVO.buscarPorValorEnum(vo.getTipoProdutoVO()));
        produto.setDisponivel(vo.getDisponivel());
        produto.setObservacao(vo.getObservacao());
        produto.setDataAtualizacao(LocalDate.now());

    }

    public ProdutoVO findById(Long id, String user) {
        var produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não localizado por Id."));

        userService.validarUsuarioGerente(user, produto.getCardapio().getEmpresa());

        return DozerConverter.parseObject(produto, ProdutoVO.class);
    }

    private Produto findByIdEntity(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não localizado por Id."));
    }

    @Transactional
    public MensagemCustom deletar(Long id, String user) throws IOException {
        var produto = findByIdEntity(id);
        userService.validarUsuarioAdminGerente(user, produto.getCardapio().getEmpresa());

        produtoRepository.delete(produto);

        return new MensagemCustom("Registro de produto excluído com sucesso.", LocalDate.now());
    }
//
//    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
//    public Page<CardapioVO> findAllByEmpresa(Long idEmpresa, Pageable pageable, String userAdmin) {
//        var empresa = empresaService.findByIdEntity(idEmpresa);
//        userService.validarUsuarioGerente(userAdmin, empresa);
//        var page = cardapioRepository.findAllByEmpresa(empresa, pageable);
//
//        return page.map(this::convertToCardapioVO);
//    }
//
//
//    private CardapioVO convertToCardapioVO(Cardapio cardapio) {
//        return DozerConverter.parseObject(cardapio, CardapioVO.class);
//    }
//
//    private Cardapio findByIdEntity(Long id) {
//        return cardapioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cardapio não localizado por Id."));
//    }
}
