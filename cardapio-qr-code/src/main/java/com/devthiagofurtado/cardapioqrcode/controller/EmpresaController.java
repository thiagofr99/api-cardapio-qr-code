package com.devthiagofurtado.cardapioqrcode.controller;


import com.devthiagofurtado.cardapioqrcode.data.vo.EmpresaDetalharVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.EmpresaVO;
import com.devthiagofurtado.cardapioqrcode.security.jwt.JwtTokenProvider;
import com.devthiagofurtado.cardapioqrcode.service.EmpresaService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@Api(tags = "EmpresaEndPoint")
@RestController
@RequestMapping("api/empresa/v1")
public class EmpresaController {

    @Autowired
    EmpresaService empresaService;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private PagedResourcesAssembler<EmpresaVO> assembler;


    @ApiOperation(value = "Saves a Empresa and returns a VO")
    @PostMapping(value = "/salvar", produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<EmpresaVO> salvarEmpresa(@RequestBody EmpresaVO empresaVO) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
        var empresaVOCrated = empresaService.salvar(empresaVO, userAdmin);
        empresaVOCrated.add(linkTo(methodOn(EmpresaController.class).buscarPorId(empresaVOCrated.getKey())).withSelfRel());
        return new ResponseEntity<>(empresaVOCrated, HttpStatus.CREATED);

    }

    @ApiOperation(value = "Update a Empresa and return a VO.")
    @PutMapping(value = "/atualizar", produces = {"application/json", "application/xml", "application/x-yaml"}
            , consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<EmpresaVO> atualizar(@RequestBody EmpresaVO empresaVO) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));

        EmpresaVO empresaVOUpdate = empresaService.atualizar(empresaVO, userAdmin);
        empresaVOUpdate.add(linkTo(methodOn(EmpresaController.class).buscarPorId(empresaVOUpdate.getKey())).withSelfRel());
        return new ResponseEntity<>(empresaVOUpdate, HttpStatus.OK);
    }

    @ApiOperation(value = "Find Empresa by Id.")
    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<EmpresaDetalharVO> buscarPorId(@PathVariable(value = "id") Long id) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
        EmpresaDetalharVO empresaVO = empresaService.findById(id, userAdmin);
        empresaVO.add(linkTo(methodOn(EmpresaController.class).buscarPorId(id)).withSelfRel());
        return new ResponseEntity<>(empresaVO, HttpStatus.OK);
    }

    @ApiOperation(value = "Find Empresas by Gerente.")
    @GetMapping(value = "/empresas-gerente", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<List<EmpresaVO>> buscarPorUserGerente() {
        String token = HeaderUtil.obterToken();
        String user = tokenProvider.getUsername(token.substring(7, token.length()));
        var empresaVOS = empresaService.findByGerente(user);
        empresaVOS.forEach(p ->
                p.add(linkTo(methodOn(EmpresaController.class).buscarPorId(p.getKey())).withSelfRel())
        );
        return new ResponseEntity<>(empresaVOS, HttpStatus.OK);
    }

    @ApiOperation(value = "Disabled a company.")
    @PatchMapping("/desabilitar/{id}")
    public ResponseEntity<MensagemCustom> desabilitar(@PathVariable(value = "id") Long id) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
        return new ResponseEntity<>(empresaService.desabilitarEmpresa(id, userAdmin), HttpStatus.OK);
    }

    @ApiOperation(value = "Enables a manager for a company.")
    @PatchMapping("/{idEmpresa}/gerente/{userGerente}")
    public ResponseEntity<EmpresaDetalharVO> habilitarGerente(@PathVariable(value = "idEmpresa") Long idEmpresa,
                                                              @PathVariable(value = "userGerente") String userGerente) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
        empresaService.habilitarGerenteEmpresa(idEmpresa, userAdmin, userGerente);
        EmpresaDetalharVO vo = empresaService.findById(idEmpresa, userAdmin);
        vo.add(linkTo(methodOn(EmpresaController.class).buscarPorId(vo.getKey())).withSelfRel());
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @ApiOperation(value = "Find all by Enterpryse Name")
    @GetMapping(value = {"/findAllByEmpresaName"}, produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<?> buscarTodosPorEmpresaNome(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "limit", defaultValue = "12") int limit,
                                                       @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                                       @RequestParam(value = "empresaName", defaultValue = "") String empresaName

    ) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "empresaNome"));
        Page<EmpresaVO> empresaVOS = empresaService.findAllByEmpresaName(empresaName, pageable, userAdmin);
        empresaVOS.forEach(p ->
                p.add(linkTo(methodOn(EmpresaController.class).buscarPorId(p.getKey())).withSelfRel())
        );
        return new ResponseEntity<>(assembler.toResource(empresaVOS), HttpStatus.OK);
    }


    @ApiOperation(value = "Deleta a company for Id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensagemCustom> deletar(@PathVariable(value = "id") Long id) {
        String token = HeaderUtil.obterToken();
        String userAdmin = tokenProvider.getUsername(token.substring(7, token.length()));

        return new ResponseEntity<>(empresaService.deletar(id, userAdmin), HttpStatus.OK);
    }
}

