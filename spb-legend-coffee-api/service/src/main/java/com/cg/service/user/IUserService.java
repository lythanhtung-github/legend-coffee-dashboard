package com.cg.service.user;

import com.cg.domain.entity.User;
import com.cg.service.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;


public interface IUserService extends IGeneralService<User>, UserDetailsService {

    User saveNoPassWord(User user);

    User getByUsername(String username);

    Optional<User> findByUserName(String username);

    Optional<User> findByCodeFirstLogin(String codeFirstLogin);

    Boolean existsByUsername(String username);

    void softDelete(Long userId);

    void recovery(Long userId);

//    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
