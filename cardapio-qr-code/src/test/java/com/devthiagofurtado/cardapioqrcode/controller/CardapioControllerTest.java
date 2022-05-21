package com.devthiagofurtado.cardapioqrcode.controller;

import com.devthiagofurtado.cardapioqrcode.data.model.Cardapio;
import com.devthiagofurtado.cardapioqrcode.data.model.Empresa;
import com.devthiagofurtado.cardapioqrcode.data.model.Permission;
import com.devthiagofurtado.cardapioqrcode.data.model.User;
import com.devthiagofurtado.cardapioqrcode.data.vo.CardapioVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.EmpresaVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.UsuarioVO;
import com.devthiagofurtado.cardapioqrcode.modelCreator.EmpresaModelCreator;
import com.devthiagofurtado.cardapioqrcode.modelCreator.UserModelCreator;
import com.devthiagofurtado.cardapioqrcode.modelCreator.jsonTest.CardapioModelCreator;
import com.devthiagofurtado.cardapioqrcode.security.jwt.JwtTokenProvider;
import com.devthiagofurtado.cardapioqrcode.service.CardapioService;
import com.devthiagofurtado.cardapioqrcode.service.EmpresaService;
import com.devthiagofurtado.cardapioqrcode.service.JasperService;
import com.devthiagofurtado.cardapioqrcode.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CardapioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private CardapioController cardapioController;

    @Mock
    private UserService userService;

    @Mock
    private JasperService jasperService;

    @Mock
    private CardapioService cardapioService;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper mapper;

    private final String BASE_URL = "/api/cardapio/v1";

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardapioController).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Authorization", "blablabla");

        var user = UserModelCreator.vo(UserModelCreator.permissionVOS(PermissionVO.ADMIN), true, true);

        CardapioVO cardapioVO = CardapioModelCreator.vo(1L,true,2L);

        BDDMockito.when(userService.findById(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString()))
                .thenReturn(user);

        BDDMockito.when(userService.findAllByUserName(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class), ArgumentMatchers.anyString()))
                .thenReturn(new PageImpl<>(Collections.singletonList(user)));

        BDDMockito.when(userService.salvar(ArgumentMatchers.any(UsuarioVO.class), ArgumentMatchers.anyString()))
                .thenReturn(user);

        BDDMockito.when(userService.findByUserName(ArgumentMatchers.anyString()))
                .thenReturn(UserModelCreator.cadastrado(null, UserModelCreator.permissions(PermissionVO.ADMIN), true));

        BDDMockito.when(userService.loadUserByUsername(ArgumentMatchers.anyString()))
                .thenReturn(UserModelCreator.userDetails(UserModelCreator.cadastrado(null, UserModelCreator.permissions(PermissionVO.ADMIN), true)));

        BDDMockito.when(jwtTokenProvider.getUsername(ArgumentMatchers.anyString()))
                .thenReturn("teste");

        BDDMockito.when(cardapioService.salvar(ArgumentMatchers.any(CardapioVO.class),ArgumentMatchers.anyString()))
                .thenReturn(cardapioVO);

        BDDMockito.when(jasperService.exportarPDF())
                .thenReturn(new byte[123]);

    }


    @Test
    void salvarCardapio() throws Exception {
        CardapioVO cardapioVO = CardapioModelCreator.vo(1L,true,2L);
        String jsonRequest = mapper.writeValueAsString(cardapioVO);

        mockMvc.perform(post(BASE_URL + "/salvar").headers(headers).contentType(APPLICATION_JSON_UTF8).content(jsonRequest)).andExpect(status().isCreated());
    }

    @Test
    void imprimirQrCode() throws Exception {
        LinkedMultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("acao", "v");
        mockMvc.perform(get(BASE_URL + "/report-qrcode/1").params(param).headers(headers)).andExpect(status().isOk());
    }

    @Test
    void deletar() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1").headers(headers)).andExpect(status().isOk());
    }
}