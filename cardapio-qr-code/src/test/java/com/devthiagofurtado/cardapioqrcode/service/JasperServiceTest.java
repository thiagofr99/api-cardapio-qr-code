package com.devthiagofurtado.cardapioqrcode.service;



import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
class JasperServiceTest {

    @InjectMocks
    private JasperService jasperService;

    @Mock
    private Connection connection;


    @Mock
    private Map<String, Object> params = new HashMap<>();

    @BeforeEach
    void setUp() {

    }

    @Test
    void addParams() {
        String testeA = "teste";
        Long testeB = 2L;
        jasperService.addParams(testeA, testeB);

        Assertions.assertThat(testeA).isNotBlank();
        Assertions.assertThat(testeB).isNotNull();
    }

}