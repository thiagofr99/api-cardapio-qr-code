package com.devthiagofurtado.cardapioqrcode.modelCreator;

import com.devthiagofurtado.cardapioqrcode.data.model.Cardapio;
import com.devthiagofurtado.cardapioqrcode.data.model.Empresa;
import com.devthiagofurtado.cardapioqrcode.data.model.User;
import com.devthiagofurtado.cardapioqrcode.data.vo.CardapioVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.EmpresaDetalharVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.EmpresaVO;

import java.time.LocalDate;

public class CardapioModelCreator {

     private static String PATH_TEST_ARQUIVO =  "C:\\Users\\thiag\\Documents\\Meu Projeto\\upload\\test.txt";
    private static String PATH_TEST_BASE =  "C:\\Users\\thiag\\Documents\\Meu Projeto\\upload\\";

    public static Cardapio cadastrado(Empresa empresa, boolean upload){

        return Cardapio.builder()
                .cardapioNome("teste")
                .dataCadastro(LocalDate.now())
                .urlCardapio(upload? "teste":"")
                .urlQrcode(upload?PATH_TEST_ARQUIVO:"")
                .id(1L)
                .empresa(empresa)
                .upload(upload)
                .build();
    }

    public static CardapioVO vo(Long empresaID, boolean upload, Long id){

        return CardapioVO.builder()
                .cardapioNome("teste")
                .dataCadastro(LocalDate.now())
                .urlCardapio("teste")
                .urlQrcode(PATH_TEST_ARQUIVO)
                .key(id)
                .empresaId(empresaID)
                .upload(upload)
                .build();
    }

    public static CardapioVO voErro(Long empresaID, boolean upload, Long id){

        return CardapioVO.builder()
                .cardapioNome("teste")
                .dataCadastro(LocalDate.now())
                .urlCardapio("teste")
                .urlQrcode("test")
                .key(id)
                .empresaId(empresaID)
                .upload(upload)
                .build();
    }

    public static Cardapio cadastrado(Empresa empresa, boolean upload,String nomeArquivo){

        return Cardapio.builder()
                .cardapioNome("teste")
                .dataCadastro(LocalDate.now())
                .urlCardapio(upload? "teste":"")
                .urlQrcode(PATH_TEST_BASE+nomeArquivo)
                .id(1L)
                .empresa(empresa)
                .upload(upload)
                .build();
    }


}
