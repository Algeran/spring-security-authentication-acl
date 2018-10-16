package com.springfreamwork.springsecurity.app.configurations;

import com.springfreamwork.springsecurity.domain.dao.UserRepository;
import com.springfreamwork.springsecurity.domain.model.security.CustomUserPrincipal;
import com.springfreamwork.springsecurity.domain.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public CustomUserDetailsService(
            UserRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserPrincipal(user);
    }
}
