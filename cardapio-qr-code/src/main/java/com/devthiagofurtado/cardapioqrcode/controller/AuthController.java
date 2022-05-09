package com.devthiagofurtado.cardapioqrcode.controller;


import com.devthiagofurtado.cardapioqrcode.data.vo.UsuarioVO;
import com.devthiagofurtado.cardapioqrcode.repository.UserRepository;
import com.devthiagofurtado.cardapioqrcode.security.AccountCredentialsVO;
import com.devthiagofurtado.cardapioqrcode.security.jwt.JwtTokenProvider;
import com.devthiagofurtado.cardapioqrcode.service.UserService;
import com.devthiagofurtado.cardapioqrcode.util.HeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;


@Api(tags = "AuthenticationEndpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider tokenProvider;

    @ApiOperation(value = "Authenticates a user and returns a token")
    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/signin", produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {
        try {
            var username = data.getUsername();
            var pasword = data.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, pasword));

            var user = userService.findByUserName(username);

            var token = "";

            if (user != null) {
                token = tokenProvider.createToken(username, user.getRoles());
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

    @ApiOperation(value = "Saves a user and returns a VO")
    @PostMapping(value = "/salvar", produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<UsuarioVO> salvarUsuario(@RequestBody UsuarioVO user) {
        String token =  HeaderUtil.obterToken();
        String userName = tokenProvider.getUsername(token);
        return new ResponseEntity<>(userService.salvar(user, userName), HttpStatus.CREATED);

    }

    @ApiOperation(value = "Buscar User por Id.")
    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public UsuarioVO buscarPorId(@PathVariable(value = "id") Long id) {
        String token =  HeaderUtil.obterToken();
        String userName = tokenProvider.getUsername(token);
        UsuarioVO personVO = userService.findById(id, userName);
        personVO.add(linkTo(methodOn(AuthController.class).buscarPorId(id)).withSelfRel());
        return personVO;
    }

    @ApiOperation(value = "Habilita um usuário por 30 dias.")
    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioVO> habilitarLicencaTrintaDias(@PathVariable(value = "id") Long id) {
        String token = HeaderUtil.obterToken();
        String userName = tokenProvider.getUsername(token);
        userService.habilitarLicencaTrintaDias(id, userName);
        UsuarioVO vo = userService.findById(id, token);
        vo.add(linkTo(methodOn(AuthController.class).buscarPorId(vo.getKey())).withSelfRel());
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }
}

