package com.devthiagofurtado.cardapioqrcode.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class FileControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private FileController fileController;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private JasperService jasperService;
//
//    @Mock
//    private CardapioService cardapioService;
//
//    @Mock
//    JwtTokenProvider jwtTokenProvider;
//
//    @Mock
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    private ObjectMapper mapper;
//
//    private final String BASE_URL = "/api/cardapio/v1";
//
//    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
//            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
//
//    private HttpHeaders headers;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
//        mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
//        headers = new HttpHeaders();
//        headers.add("Accept", "application/json");
//        headers.add("Authorization", "blablabla");
//
//        var user = UserModelCreator.vo(UserModelCreator.permissionVOS(PermissionVO.ADMIN), true, true);
//
//        CardapioVO cardapioVO = CardapioModelCreator.vo(1L,true,2L);
//
//        BDDMockito.when(userService.findById(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString()))
//                .thenReturn(user);
//
//        BDDMockito.when(userService.findAllByUserName(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class), ArgumentMatchers.anyString()))
//                .thenReturn(new PageImpl<>(Collections.singletonList(user)));
//
//        BDDMockito.when(userService.salvar(ArgumentMatchers.any(UsuarioVO.class), ArgumentMatchers.anyString()))
//                .thenReturn(user);
//
//        BDDMockito.when(userService.findByUserName(ArgumentMatchers.anyString()))
//                .thenReturn(UserModelCreator.cadastrado(null, UserModelCreator.permissions(PermissionVO.ADMIN), true));
//
//        BDDMockito.when(userService.loadUserByUsername(ArgumentMatchers.anyString()))
//                .thenReturn(UserModelCreator.userDetails(UserModelCreator.cadastrado(null, UserModelCreator.permissions(PermissionVO.ADMIN), true)));
//
//        BDDMockito.when(jwtTokenProvider.getUsername(ArgumentMatchers.anyString()))
//                .thenReturn("teste");
//
//        BDDMockito.when(cardapioService.salvar(ArgumentMatchers.any(CardapioVO.class),ArgumentMatchers.anyString()))
//                .thenReturn(cardapioVO);
//
//        BDDMockito.when(jasperService.exportarPDF())
//                .thenReturn(new byte[123]);
//
//    }
//
//
//    @Test
//    void uploadFile() throws Exception {
//            MockMultipartFile file
//                    = new MockMultipartFile(
//                    "file",
//                    "hello.txt",
//                    MediaType.TEXT_PLAIN_VALUE,
//                    "Hello, World!".getBytes()
//            );
//
//            MockMvc mockMvc
//                    = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//            mockMvc.perform(multipart("/upload").file(file))
//                    .andExpect(status().isOk());
//    }
//
//    @Test
//    void imprimirQrCode() throws Exception {
//        LinkedMultiValueMap<String, String> param = new LinkedMultiValueMap<>();
//        param.add("acao", "v");
//        mockMvc.perform(get(BASE_URL + "/report-qrcode/1").params(param).headers(headers)).andExpect(status().isOk());
//    }
//
//    @Test
//    void deletar() throws Exception {
//        mockMvc.perform(delete(BASE_URL + "/1").headers(headers)).andExpect(status().isOk());
//    }
}