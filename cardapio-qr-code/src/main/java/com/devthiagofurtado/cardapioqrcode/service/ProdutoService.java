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

import java.time.LocalDate;
import java.util.List;

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


        Produto produto;

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

        var vo = DozerConverter.parseObject(produto, ProdutoVO.class);
        vo.setCardapioId(produto.getCardapio().getId());
        return vo;
    }

    private Produto findByIdEntity(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não localizado por Id."));
    }

    @Transactional
    public MensagemCustom deletar(Long id, String user) {
        var produto = findByIdEntity(id);
        userService.validarUsuarioAdminGerente(user, produto.getCardapio().getEmpresa());

        produtoRepository.delete(produto);

        return new MensagemCustom("Registro de produto excluído com sucesso.", LocalDate.now());
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ProdutoVO> findAllByCardapio(Long idCardapio, String user) {
        var cardapio = cardapioService.findByIdEntity(idCardapio);
        userService.validarUsuarioGerente(user, cardapio.getEmpresa());
        return DozerConverter.parseListObjects(produtoRepository.findAllByCardapio(cardapio), ProdutoVO.class);
    }

    public ProdutoVO disponibilidade(Long id, String user) {
        var produto = findByIdEntity(id);
        var cardapio = cardapioService.findByIdEntity(produto.getCardapio().getId());
        userService.validarUsuarioGerente(user, cardapio.getEmpresa());
        produto.setDisponivel(!produto.getDisponivel());
        return DozerConverter.parseObject(produtoRepository.save(produto), ProdutoVO.class);
    }
}
