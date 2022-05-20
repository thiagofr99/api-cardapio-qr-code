package com.devthiagofurtado.cardapioqrcode.service;

import com.devthiagofurtado.cardapioqrcode.converter.DozerConverter;
import com.devthiagofurtado.cardapioqrcode.data.model.Cardapio;
import com.devthiagofurtado.cardapioqrcode.data.model.Empresa;
import com.devthiagofurtado.cardapioqrcode.data.vo.CardapioVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.EmpresaDetalharVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.EmpresaVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;
import com.devthiagofurtado.cardapioqrcode.exception.ResourceBadRequestException;
import com.devthiagofurtado.cardapioqrcode.exception.ResourceNotFoundException;
import com.devthiagofurtado.cardapioqrcode.repository.CardapioRepository;
import com.devthiagofurtado.cardapioqrcode.repository.EmpresaRepository;
import com.devthiagofurtado.cardapioqrcode.util.MensagemCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Service
public class CardapioService {

    @Autowired
    private CardapioRepository cardapioRepository;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private UserService userService;


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public CardapioVO salvar(CardapioVO cardapioVO, String userName) {

        var empresa = empresaService.findByIdEntity(cardapioVO.getEmpresaId());
        userService.validarUsuarioGerente(userName,empresa);
        cardapioVO.setDataCadastro(LocalDate.now());
        cardapioVO.setUpload(StringUtils.hasText(cardapioVO.getUrlCardapio()));

        var cardapio = DozerConverter.parseObject(cardapioVO, Cardapio.class);
        cardapio.setEmpresa(empresa);
        var cardapioSave = cardapioRepository.save(cardapio);
        return DozerConverter.parseObject(cardapioSave, CardapioVO.class);

    }

//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
//    public EmpresaVO atualizar(EmpresaVO empresaVO, String userName) {
//
//        userService.validarUsuarioAdmin(userName);
//        var empresaParaAtualizar = this.verificaEmpresa(empresaVO.getKey());
//
//        this.atualizarEmpresa(empresaParaAtualizar, empresaVO);
//        return DozerConverter.parseObject(empresaRepository.save(empresaParaAtualizar), EmpresaVO.class);
//
//    }
//
//    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
//    public Page<EmpresaVO> findAllByEmpresaName(String empresaName, Pageable pageable, String userAdmin) {
//        userService.validarUsuarioAdmin(userAdmin);
//        var page = empresaRepository.findAllByEmpresaName(empresaName, pageable);
//
//        return page.map(this::convertToEmpresaVO);
//    }
//
//    private EmpresaVO convertToEmpresaVO(Empresa empresa) {
//        return DozerConverter.parseObject(empresa, EmpresaVO.class);
//    }
//
//    private void atualizarEmpresa(Empresa empresa, EmpresaVO empresaVO) {
//        empresa.setDataAtualizacao(LocalDate.now());
//        empresa.setEmpresaNome(empresaVO.getEmpresaNome());
//        empresa.setCep(empresaVO.getCep());
//        empresa.setNumero(empresaVO.getNumero());
//        empresa.setComplemento(empresaVO.getComplemento());
//    }
//
//    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
//    public EmpresaDetalharVO findById(Long id, String userAdmin) {
//        var empresa = empresaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Não foi localizado nenhuma empresa com esse Id."));
//        this.verificaEmpresa(id);
//        userService.validarUsuarioAdminGerente(userAdmin, empresa);
//
//        return DozerConverter.empresaToDetalharVO(empresa);
//    }
//
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void habilitarGerenteEmpresa(Long idEmpresa, String userAdmin, String userGerente) {
//        userService.validarUsuarioAdmin(userAdmin);
//        var empresa = this.verificaEmpresa(idEmpresa);
//
//        var user = userService.findByUserName(userGerente);
//
//        if (user.getPermissions().stream().anyMatch(p -> p.getDescription().equals(PermissionVO.MANAGER.name()))) {
//            empresa.setUser(user);
//            empresaRepository.save(empresa);
//        } else {
//            throw new ResourceBadRequestException("Usuário não possuí permissão de Gerente.");
//        }
//    }
//
//
//    @Transactional
//    public MensagemCustom deletar(Long id, String userAdmin) {
//        userService.validarUsuarioAdmin(userAdmin);
//        var empresa = findByIdEntity(id);
//
//        empresaRepository.delete(empresa);
//        return new MensagemCustom("Registro de empresa excluído com sucesso.", LocalDate.now());
//    }
//
//    @Transactional(propagation = Propagation.REQUIRED)
//    public MensagemCustom desabilitarEmpresa(Long id, String userAdmin) {
//        userService.validarUsuarioAdmin(userAdmin);
//        var empresa = findByIdEntity(id);
//        empresa.setEnabled(false);
//        empresa.setDataAtualizacao(LocalDate.now());
//        empresaRepository.save(empresa);
//
//        return new MensagemCustom("Empresa " + empresa.getEmpresaNome() + " foi desabilitada com sucesso.", LocalDate.now());
//    }
//
//    private Empresa verificaEmpresa(Long idEmpresa) {
//        var empresa = findByIdEntity(idEmpresa);
//        if (empresa.getEmpresaNome()!=null && !empresa.getEnabled()) {
//            throw new ResourceBadRequestException("Empresa desativada, não é possível concluir operação.");
//        }
//
//        return empresa;
//    }
//
//    private Empresa findByIdEntity(Long id) {
//        return empresaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Empresa não localizada por Id."));
//    }
}
