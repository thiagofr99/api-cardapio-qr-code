package com.devthiagofurtado.cardapioqrcode.controller;


import com.devthiagofurtado.cardapioqrcode.data.vo.CardapioVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.EmpresaVO;
import com.devthiagofurtado.cardapioqrcode.security.jwt.JwtTokenProvider;
import com.devthiagofurtado.cardapioqrcode.service.CardapioService;
import com.devthiagofurtado.cardapioqrcode.service.JasperService;
import com.devthiagofurtado.cardapioqrcode.util.HeaderUtil;
import com.devthiagofurtado.cardapioqrcode.util.MensagemCustom;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@Api(tags = "CardapioEndPoint")
@RestController
@RequestMapping("api/cardapio/v1")
public class CardapioController {

    @Autowired
    CardapioService cardapioService;

    @Autowired
    JasperService jasperService;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private PagedResourcesAssembler<CardapioVO> assembler;


    @ApiOperation(value = "Saves a Cardapio and returns a VO")
    @PostMapping(value = "/salvar", produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<CardapioVO> salvarCardapio(@RequestBody CardapioVO cardapioVO) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
        var empresaVOCrated = cardapioService.salvar(cardapioVO, userAdmin);
        //empresaVOCrated.add(linkTo(methodOn(CardapioController.class).buscarPorId(empresaVOCrated.getKey())).withSelfRel());
        return new ResponseEntity<>(empresaVOCrated, HttpStatus.CREATED);

    }

    @ApiOperation(value = "Print a QR Code to report")
    @GetMapping(value = "/report-qrcode/{id}")
    public void imprimirQrCode(@PathVariable(value = "id") Long id,
                               @RequestParam("acao") String acao,
                               HttpServletResponse response) throws IOException {

        jasperService.addParams("ID_CARDAPIO", id);
        byte[] bytes = jasperService.exportarPDF();

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        if (acao.equals("v")) {
            response.setHeader("Content-disposition", "inline; filename=QR-code-cardapio.pdf");
        } else {
            response.setHeader("Content-disposition", "attachment; filename=QR-code-cardapio.pdf");
        }

        response.getOutputStream().write(bytes);

    }

    @ApiOperation(value = "Deleta a company for Id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensagemCustom> deletar(@PathVariable(value = "id") Long id) throws IOException {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));

        return new ResponseEntity<>(cardapioService.deletar(id, userAdmin), HttpStatus.OK);
    }

    @ApiOperation(value = "Find all Cardapios by Enterpryse")
    @GetMapping(value = {"/findAllByEmpresa/{idEmpresa}"}, produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<?> buscarTodosPorEmpresa(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "limit", defaultValue = "12") int limit,
                                                   @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                                   @PathVariable(value = "idEmpresa") Long id) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "cardapioNome"));
        var cardapioVOS = cardapioService.findAllByEmpresa(id, pageable, userAdmin);
//        cardapioVOS.forEach(p ->
//                p.add(linkTo(methodOn(EmpresaController.class).buscarPorId(p.getKey())).withSelfRel())
//        );
        return new ResponseEntity<>(assembler.toResource(cardapioVOS), HttpStatus.OK);
    }
}

