package com.alexander.identityservice.security;

import com.alexander.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username).map(user -> new User(user.getEmail(), user.getPassword(), Collections.singleton(user.getRole())))
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

}
