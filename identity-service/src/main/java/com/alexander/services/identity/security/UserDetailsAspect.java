package com.alexander.services.identity.security;

import com.alexander.services.identity.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class UserDetailsAspect {

    private final UserCache userCache;

    @AfterReturning(pointcut = "execution(* com.alexander.services.identity.service.UserService.update(..)) " +
            "|| execution(* com.alexander.services.identity.service.UserService.updatePasswordAndSendTokenForReset(..)) " +
            "|| execution(* com.alexander.services.identity.service.UserService.updateUserPasswordByPasswordResetToken(..)) " +
            "|| execution(* com.alexander.services.identity.service.UserService.updatePassword(..))", returning = "retVal")
    public void removeUserFromCache(final Object retVal) {
        if (retVal instanceof UserDto user) {
            userCache.removeUserFromCache(user.getEmail());
        }
    }

}
