package com.devthiagofurtado.cardapioqrcode.modelCreator;

import com.devthiagofurtado.cardapioqrcode.data.model.Empresa;
import com.devthiagofurtado.cardapioqrcode.data.model.Permission;
import com.devthiagofurtado.cardapioqrcode.data.model.User;
import com.devthiagofurtado.cardapioqrcode.data.vo.EmpresaVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.UsuarioVO;
import com.devthiagofurtado.cardapioqrcode.modelCreator.jsonTest.UsuarioVOJsonTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

}
