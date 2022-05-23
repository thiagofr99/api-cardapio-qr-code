package com.devthiagofurtado.cardapioqrcode.controller;


import com.devthiagofurtado.cardapioqrcode.data.vo.ProdutoVO;
import com.devthiagofurtado.cardapioqrcode.security.jwt.JwtTokenProvider;
import com.devthiagofurtado.cardapioqrcode.service.ProdutoService;
import com.devthiagofurtado.cardapioqrcode.util.HeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        //empresaVOCrated.add(linkTo(methodOn(CardapioController.class).buscarPorId(empresaVOCrated.getKey())).withSelfRel());
        return new ResponseEntity<>(produtoVOCrated, HttpStatus.CREATED);

    }

    @ApiOperation(value = "Update a Produto and returns a VO")
    @PutMapping(value = "/atualizar", produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<ProdutoVO> atualizarProduto(@RequestBody ProdutoVO produtoVO) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
        var produtoVOCrated = produtoService.salvar(produtoVO, userAdmin);
        //empresaVOCrated.add(linkTo(methodOn(CardapioController.class).buscarPorId(empresaVOCrated.getKey())).withSelfRel());
        return new ResponseEntity<>(produtoVOCrated, HttpStatus.OK);

    }

//    @ApiOperation(value = "Print a QR Code to report")
//    @GetMapping(value = "/report-qrcode/{id}")
//    public void imprimirQrCode(@PathVariable(value = "id") Long id,
//                               @RequestParam("acao") String acao,
//                               HttpServletResponse response) throws IOException {
//
//        jasperService.addParams("ID_CARDAPIO", id);
//        byte[] bytes = jasperService.exportarPDF();
//
//        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
//        if (acao.equals("v")) {
//            response.setHeader("Content-disposition", "inline; filename=QR-code-cardapio.pdf");
//        } else {
//            response.setHeader("Content-disposition", "attachment; filename=QR-code-cardapio.pdf");
//        }
//
//        response.getOutputStream().write(bytes);
//
//    }
//
//    @ApiOperation(value = "Deleta a company for Id.")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<MensagemCustom> deletar(@PathVariable(value = "id") Long id) throws IOException {
//        String token = HeaderUtil.obterToken();
//        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
//
//        return new ResponseEntity<>(cardapioService.deletar(id, userAdmin), HttpStatus.OK);
//    }
//
//    @ApiOperation(value = "Find all Cardapios by Enterpryse")
//    @GetMapping(value = {"/findAllByEmpresa/{idEmpresa}"}, produces = {"application/json", "application/xml", "application/x-yaml"})
//    public ResponseEntity<?> buscarTodosPorEmpresa(@RequestParam(value = "page", defaultValue = "0") int page,
//                                                   @RequestParam(value = "limit", defaultValue = "12") int limit,
//                                                   @RequestParam(value = "direction", defaultValue = "ASC") String direction,
//                                                   @PathVariable(value = "idEmpresa") Long id) {
//        String token = HeaderUtil.obterToken();
//        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
//        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
//
//        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "cardapioNome"));
//        var cardapioVOS = cardapioService.findAllByEmpresa(id, pageable, userAdmin);
////        cardapioVOS.forEach(p ->
////                p.add(linkTo(methodOn(EmpresaController.class).buscarPorId(p.getKey())).withSelfRel())
////        );
//        return new ResponseEntity<>(assembler.toResource(cardapioVOS), HttpStatus.OK);
//    }
}

