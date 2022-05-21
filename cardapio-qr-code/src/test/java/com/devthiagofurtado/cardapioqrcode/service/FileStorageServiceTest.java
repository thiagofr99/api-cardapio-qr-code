package com.devthiagofurtado.cardapioqrcode.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class FileStorageServiceTest {

//
//
//    @InjectMocks
//    private FileStorageService fileStorageService;
//
//    @Mock
//    private FileStorageConfig fileStorageConfig;
//
//
//    @BeforeEach
//    void setUp() {
//        final Path fileStorageLocation;
//
//    }
//
//    @Test
//    void storeFile() {
//        MultipartFile file = new MultipartFile() {
//            @Override
//            public String getName() {
//                return "teste";
//            }
//
//            @Override
//            public String getOriginalFilename() {
//                return "teste";
//            }
//
//            @Override
//            public String getContentType() {
//                return "txt";
//            }
//
//            @Override
//            public boolean isEmpty() {
//                return false;
//            }
//
//            @Override
//            public long getSize() {
//                return 20;
//            }
//
//            @Override
//            public byte[] getBytes() throws IOException {
//                return new byte[122453];
//            }
//
//            @Override
//            public InputStream getInputStream() throws IOException {
//                return null;
//            }
//
//            @Override
//            public void transferTo(File dest) throws IOException, IllegalStateException {
//
//            }
//        };
//        String teste = fileStorageService.storeFile(file);
//        Assertions.assertThat(teste).isNotNull();
//
//    }
//
//    @Test
//    void loadFileAsResource() {
//
//    }
}