package com.devthiagofurtado.cardapioqrcode.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensagemCustom {

    private String mensagem;

    private LocalDate dateMensagem;

}
