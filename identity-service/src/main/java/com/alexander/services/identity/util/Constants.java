package com.alexander.services.identity.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final String EMAIL = "email";
    public static final String TOKEN = "token";
    public static final String APPLICATION = "Application";
    public static final String ERROR_MSG_AUTH_WRONG_PASSWORD = "error.msg.auth.wrong.password";
    public static final String ERROR_MSG_USER_PASSWORD_UPDATE = "error.msg.user.password.update";
    public static final String ERROR_MSG_USER_EMAIL_VERIFICATION_RESET_OR_EXPIRED = "error.msg.user.email.verification.reset.or.expired";
    public static final String ERROR_MSG_USER_PASSWORD_RESET_OR_EXPIRED = "error.msg.user.password.reset.or.expired";

}
