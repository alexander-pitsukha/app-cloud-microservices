package com.alexander.identityservice.converter;

import com.alexander.identityservice.dto.UserDto;
import com.alexander.identityservice.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {

    @Override
    public User convert(UserDto source) {
        User user = new User();
        user.setId(source.getId());
        user.setName(source.getName());
        user.setEmail(source.getEmail());
        user.setRole(source.getRole());
        return user;
    }

}
