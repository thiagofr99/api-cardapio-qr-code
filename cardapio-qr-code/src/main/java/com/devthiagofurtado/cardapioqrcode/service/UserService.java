package com.devthiagofurtado.cardapioqrcode.service;

import com.devthiagofurtado.cardapioqrcode.converter.DozerConverter;
import com.devthiagofurtado.cardapioqrcode.data.model.Permission;
import com.devthiagofurtado.cardapioqrcode.data.model.User;
import com.devthiagofurtado.cardapioqrcode.data.vo.PermissionVO;
import com.devthiagofurtado.cardapioqrcode.data.vo.UsuarioVO;
import com.devthiagofurtado.cardapioqrcode.exception.ResourceBadRequestException;
import com.devthiagofurtado.cardapioqrcode.repository.UserRepository;
import com.devthiagofurtado.cardapioqrcode.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UsuarioVO salvar(UsuarioVO user, String userName) {

        validarUsuarioAdmin(userName);

        var userSave = userRepository.save(DozerConverter.parseUsuarioVOtoUser(user));
        return DozerConverter.parseUsertoVO(userSave);


    }

    private void validarUsuarioAdmin(String userName) {
        List<Permission> permissions = findByUserName(userName).getPermissions();
        if (permissions.stream().noneMatch(p -> p.getDescription().equals(PermissionVO.ADMIN.name()))) {
            throw new ResourceBadRequestException("Apenas usuário Admin pode executar a ação.");
        }
    }

    public User findByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    public UsuarioVO findById(Long id, String userName) {
        validarUsuarioAdmin(userName);
        return DozerConverter.parseUsertoVO(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não localizado!")));
    }

    public void habilitarLicencaTrintaDias(Long id, String token) {
        validarUsuarioAdmin(token);
        var user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não localizado!"));
        user.setDateLicense(LocalDate.now().plusDays(30));
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }

}
