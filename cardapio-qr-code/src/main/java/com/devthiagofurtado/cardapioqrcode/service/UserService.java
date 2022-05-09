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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public UsuarioVO salvar(UsuarioVO user, String token) {
        List<Permission> permissions = buscarUserPorToken(token).getPermissions();
        if(permissions.stream().anyMatch(p-> p.getDescription().equals(PermissionVO.ADMIN.name()))){
            var userSave = userRepository.save(DozerConverter.parseUsuarioVOtoUser(user));
            return DozerConverter.parseUsertoVO(userSave);
        } else {
            throw new ResourceBadRequestException("Apenas usuario Admin pode salvar.");
        }

    }

    private User buscarUserPorToken(String token) {
         return userRepository.findByUsername(jwtTokenProvider.getUsername(token.substring(7, token.length())));
    }
}
