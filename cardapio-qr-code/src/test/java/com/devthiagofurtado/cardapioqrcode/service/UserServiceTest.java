package com.devthiagofurtado.cardapioqrcode.service;

import com.devthiagofurtado.cardapioqrcode.data.model.User;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;
import com.devthiagofurtado.cardapioqrcode.modelCreator.UserModelCreator;
import com.devthiagofurtado.cardapioqrcode.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsService userDetailsService;

    UserServiceTest() {
    }

    @BeforeEach
    void setUp() {

        BDDMockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(UserModelCreator.cadastrado(null, UserModelCreator.permissions(PermissionVO.ADMIN), true));

        BDDMockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(UserModelCreator.cadastrado(null, UserModelCreator.permissions(PermissionVO.ADMIN), true));

        BDDMockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(UserModelCreator.cadastrado(null, UserModelCreator.permissions(PermissionVO.ADMIN), true)));

        BDDMockito.when(userDetailsService.loadUserByUsername(ArgumentMatchers.anyString()))
                .thenReturn(UserModelCreator.userDetails(UserModelCreator.cadastrado(null, UserModelCreator.permissions(PermissionVO.ADMIN), true)));

    }

    @Test
    void loadUserByUsername_retornaUserDetails_sucesso() {

        var teste = userService.loadUserByUsername("teste");
        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getAuthorities()).isNotNull().isNotEmpty();
    }

    @Test
    void salvar_retornaUsuarioVO_sucesso() {
        var permissions = Collections.singletonList(PermissionVO.ADMIN);
        var teste = userService.salvar(UserModelCreator.vo(permissions, true, false), "teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getKey()).isNotNull();

    }

    @Test
    void findById() {

        var permissions = Collections.singletonList(PermissionVO.ADMIN);
        var teste = userService.findById(1L, "teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getKey()).isNotNull();
    }

    @Test
    void habilitarLicensaTrintaDias_sucesso() {

        userService.habilitarLicencaTrintaDias(1L, "teste");

    }
}