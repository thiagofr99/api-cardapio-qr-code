package com.devthiagofurtado.cardapioqrcode.controller;


import com.devthiagofurtado.cardapioqrcode.data.enums.TipoProdutoVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.ProdutoVO;
import com.devthiagofurtado.cardapioqrcode.security.jwt.JwtTokenProvider;
import com.devthiagofurtado.cardapioqrcode.service.ProdutoService;
import com.devthiagofurtado.cardapioqrcode.util.HeaderUtil;
import com.devthiagofurtado.cardapioqrcode.util.MensagemCustom;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@Api(tags = "ProdutoEndPoint")
@RestController
@RequestMapping("api/produto/v1")
public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private PagedResourcesAssembler<ProdutoVO> assembler;


    @ApiOperation(value = "Saves a Produto and returns a VO")
    @PostMapping(value = "/salvar", produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<ProdutoVO> salvarProduto(@RequestBody ProdutoVO produtoVO) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
        var produtoVOCrated = produtoService.salvar(produtoVO, userAdmin);
        produtoVOCrated.add(linkTo(methodOn(ProdutoController.class).buscarPorId(produtoVOCrated.getKey())).withSelfRel());
        return new ResponseEntity<>(produtoVOCrated, HttpStatus.CREATED);

    }

    @ApiOperation(value = "Update a Produto and returns a VO")
    @PutMapping(value = "/atualizar", produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<ProdutoVO> atualizarProduto(@RequestBody ProdutoVO produtoVO) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
        var produtoVOCrated = produtoService.salvar(produtoVO, userAdmin);
        produtoVOCrated.add(linkTo(methodOn(ProdutoController.class).buscarPorId(produtoVOCrated.getKey())).withSelfRel());
        return new ResponseEntity<>(produtoVOCrated, HttpStatus.OK);

    }

    @ApiOperation(value = "Deleta a product for Id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensagemCustom> deletar(@PathVariable(value = "id") Long id) throws IOException {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
        return new ResponseEntity<>(produtoService.deletar(id, userAdmin), HttpStatus.OK);
    }

    @ApiOperation(value = "Find produto by Id")
    @GetMapping(value = {"/{id}"}, produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<ProdutoVO> buscarPorId(@PathVariable(value = "id") Long id) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
        var vo = produtoService.findById(id, userAdmin);
        vo.add(linkTo(methodOn(ProdutoController.class).buscarPorId(id)).withSelfRel());
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @ApiOperation(value = "Find all Produtos by Cardapio")
    @GetMapping(value = {"/findAllByCardapio/{idCardapio}"}, produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<List<ProdutoVO>> buscarTodosPorCardapio(
            @PathVariable(value = "idCardapio") Long id) {
        String token = HeaderUtil.obterToken();
        String user = tokenProvider.getUsername(token.substring(7, token.length()));

        var produtosVO = produtoService.findAllByCardapio(id, user);
        produtosVO.forEach(p ->
                p.add(linkTo(methodOn(ProdutoController.class).buscarPorId(p.getKey())).withSelfRel())
        );
        return new ResponseEntity<>(produtosVO, HttpStatus.OK);
    }

    @ApiOperation(value = "Find Tipo Produto.")
    @GetMapping(value = "/tipo-produto", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<List<TipoProdutoVO>> listarTipoProdutos() {
        return new ResponseEntity<>(TipoProdutoVO.listar(), HttpStatus.OK);
    }
}

