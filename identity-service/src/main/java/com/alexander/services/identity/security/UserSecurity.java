package com.alexander.services.identity.security;

import com.alexander.services.identity.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserSecurity {

    public boolean hasUserId(Authentication authentication, Long userId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        return Objects.equals(user.getId(), userId);
    }

}
