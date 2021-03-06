package com.devthiagofurtado.cardapioqrcode.modelCreator;

import com.devthiagofurtado.cardapioqrcode.data.model.Empresa;
import com.devthiagofurtado.cardapioqrcode.data.model.User;
import com.devthiagofurtado.cardapioqrcode.data.vo.EmpresaDetalharVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.EmpresaVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;

public class EmpresaModelCreator {

    public static Empresa cadastrado(User user, boolean enabled){

        return Empresa.builder()
                .cep("60000000")
                .complemento("teste")
                .empresaNome("teste")
                .id(1L)
                .user(user)
                .enabled(enabled)
                .build();
    }

    public static EmpresaVO vo(Long key){
        return EmpresaVO.builder()
                .cep("60000000")
                .complemento("teste")
                .empresaNome("teste")
                .key(key)
                .build();
    }

    public static EmpresaDetalharVO detalharVo(Long key){
        return EmpresaDetalharVO.builder()
                .cep("60000000")
                .complemento("teste")
                .empresaNome("teste")
                .key(key)
                .build();
    }

}
