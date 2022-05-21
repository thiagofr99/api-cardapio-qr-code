package com.devthiagofurtado.cardapioqrcode.service;

import com.devthiagofurtado.cardapioqrcode.converter.DozerConverter;
import com.devthiagofurtado.cardapioqrcode.data.model.Cardapio;
import com.devthiagofurtado.cardapioqrcode.data.vo.CardapioVO;
import com.devthiagofurtado.cardapioqrcode.exception.FileStorageException;
import com.devthiagofurtado.cardapioqrcode.exception.ResourceNotFoundException;
import com.devthiagofurtado.cardapioqrcode.repository.CardapioRepository;
import com.devthiagofurtado.cardapioqrcode.util.MensagemCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        userService.validarUsuarioGerente(userName, empresa);
        cardapioVO.setDataCadastro(LocalDate.now());
        cardapioVO.setUpload(StringUtils.hasText(cardapioVO.getUrlCardapio()));

        Path path = Path.of(cardapioVO.getUrlQrcode());

        if (Files.notExists(path)) {
            throw new FileStorageException("Não foi localizado arquivo através da URL.");
        }

        var cardapio = DozerConverter.parseObject(cardapioVO, Cardapio.class);
        cardapio.setEmpresa(empresa);
        var cardapioSave = cardapioRepository.save(cardapio);
        return DozerConverter.parseObject(cardapioSave, CardapioVO.class);

    }

    @Transactional
    public MensagemCustom deletar(Long id, String userAdmin) throws IOException {
        var cardapio = findByIdEntity(id);
        userService.validarUsuarioAdminGerente(userAdmin, cardapio.getEmpresa());

        Path path = Path.of(cardapio.getUrlQrcode());

        if (Files.notExists(path)) {
            throw new FileStorageException("Arquivo para excluir não existe.");
        }

        Files.delete(path.toAbsolutePath());

//        if(Files.exists(path)){
//            throw new FileStorageException("Arquivo não foi excluido.");
//        }

        cardapioRepository.delete(cardapio);

        return new MensagemCustom("Registro de cardapio excluído com sucesso.", LocalDate.now());
    }

    private Cardapio findByIdEntity(Long id) {
        return cardapioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cardapio não localizado por Id."));
    }
}
