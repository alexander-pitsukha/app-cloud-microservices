package com.alexander.services.identity.security;

import com.alexander.services.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserCache userCache;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userDetails = userCache.getUserFromCache(username);
        if (userDetails == null) {
            userDetails = userRepository.findByEmailAndEmailVerifiedIsTrue(username)
                    .map(user -> new UserDetailsImpl(user, username, user.getPassword().getPasswordHash(), Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName())))).orElseThrow(() -> new UsernameNotFoundException("User not found."));
            userCache.putUserInCache(userDetails);
        }
        return userDetails;
    }

}
