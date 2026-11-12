package com.alexander.identityservice.converter;

import com.alexander.identityservice.controller.request.UserBody;
import com.alexander.identityservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBodyToUserConverter implements Converter<UserBody, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User convert(UserBody source) {
        User user = new User();
        user.setName(source.getName());
        user.setEmail(source.getEmail());
        user.setPassword(passwordEncoder.encode(source.getPassword()));
        user.setRole(source.getRole());
        return user;
    }

}
