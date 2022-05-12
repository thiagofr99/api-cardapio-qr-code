package com.devthiagofurtado.cardapioqrcode.service;

import com.devthiagofurtado.cardapioqrcode.data.model.Empresa;
import com.devthiagofurtado.cardapioqrcode.data.vo.EmpresaVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;
import com.devthiagofurtado.cardapioqrcode.exception.ResourceBadRequestException;
import com.devthiagofurtado.cardapioqrcode.exception.ResourceNotFoundException;
import com.devthiagofurtado.cardapioqrcode.modelCreator.EmpresaModelCreator;
import com.devthiagofurtado.cardapioqrcode.modelCreator.UserModelCreator;
import com.devthiagofurtado.cardapioqrcode.repository.EmpresaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
class EmpresaServiceTest {

    @InjectMocks
    private EmpresaService empresaService;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {

        BDDMockito.when(empresaRepository.save(ArgumentMatchers.any(Empresa.class)))
                .thenReturn(EmpresaModelCreator.cadastrado(null, true));

        BDDMockito.when(empresaRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(EmpresaModelCreator.cadastrado(null, true)));

        BDDMockito.when(empresaRepository.findAllByEmpresaName(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(EmpresaModelCreator.cadastrado(null, true))));

        BDDMockito.doNothing().when(userService).validarUsuarioAdmin(ArgumentMatchers.anyString());

        BDDMockito.doNothing().when(userService).validarUsuarioAdminGerente(ArgumentMatchers.anyString(), ArgumentMatchers.any(Empresa.class));

    }

    @Test
    void salvar_retornaEmpresaVO_sucesso() {

        var teste = empresaService.salvar(EmpresaModelCreator.vo(null), "teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getKey()).isNotNull();

    }

    @Test
    void atualizar_retornaEmpresaVO_sucesso() {

        var teste = empresaService.atualizar(EmpresaModelCreator.vo(1L), "teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getKey()).isNotNull();
    }

    @Test
    void atualizar_retornaResourceNotFoundException_erro() {
        BDDMockito.when(empresaRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> empresaService.atualizar(EmpresaModelCreator.vo(1L), "teste"))
                .isInstanceOf(ResourceNotFoundException.class);


    }

    @Test
    void atualizar_retornaResourceBadRequestException_erro() {
        BDDMockito.when(empresaRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(EmpresaModelCreator.cadastrado(null, false)));

        Assertions.assertThatThrownBy(() -> empresaService.atualizar(EmpresaModelCreator.vo(1L), "teste"))
                .isInstanceOf(ResourceBadRequestException.class);


    }

    @Test
    void findAllByEmpresaName() {

        Pageable pageable = PageRequest.of(1, 12);

        Page<EmpresaVO> teste = empresaService.findAllByEmpresaName("", pageable, "teste");

        Assertions.assertThat(teste).isNotNull().isNotEmpty();
        Assertions.assertThat(teste.get().collect(Collectors.toList()).get(0).getKey()).isNotNull();

    }

    @Test
    void findById_retornaEmpresaVO_sucesso() {

        var teste = empresaService.findById(1L, "teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getKey()).isNotNull();
    }

    @Test
    void findById_retornaResourceNotFoundException_erro() {
        BDDMockito.when(empresaRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> empresaService.findById(1L, "teste"))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void habilitarGerenteEmpresa_sucesso() {
        BDDMockito.when(userService.findByUserName(ArgumentMatchers.anyString()))
                .thenReturn(UserModelCreator.cadastrado(LocalDate.now().plusDays(10), UserModelCreator.permissions(PermissionVO.MANAGER), true));

        empresaService.habilitarGerenteEmpresa(1L, "teste", "gerente");

    }

    @Test
    void habilitarGerenteEmpresa_retornaResourceBadRequestException_erro() {
        BDDMockito.when(userService.findByUserName(ArgumentMatchers.anyString()))
                .thenReturn(UserModelCreator.cadastrado(LocalDate.now().plusDays(10), UserModelCreator.permissions(PermissionVO.COMMON_USER), true));

        Assertions.assertThatThrownBy(() -> empresaService.habilitarGerenteEmpresa(1L, "teste", "gerente"))
                .isInstanceOf(ResourceBadRequestException.class);

    }

    @Test
    void deletar_retornaMensagemCustom_sucesso() {

        var teste = empresaService.deletar(1L, "teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getMensagem()).isEqualTo("Registro de empresa excluído com sucesso.");
        Assertions.assertThat(teste.getDateMensagem()).isNotNull();

    }

    @Test
    void deletar_retornaResourceNotFoundException_erro() {
        BDDMockito.when(empresaRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> empresaService.deletar(1L, "teste"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void desabilitarEmpresa() {
        var teste = empresaService.desabilitarEmpresa(1L, "teste");

        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getMensagem()).isEqualTo("Empresa " + EmpresaModelCreator.cadastrado(null, true).getEmpresaNome() + " foi desabilitada com sucesso.");
        Assertions.assertThat(teste.getDateMensagem()).isNotNull();
    }
}