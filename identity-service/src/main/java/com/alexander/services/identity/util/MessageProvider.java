package com.alexander.services.identity.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageProvider {

    private final Locale locale = LocaleContextHolder.getLocale();

    private final MessageSource messageSource;

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, locale);
    }

    public String getMessage(String code, Object... params) {
        return messageSource.getMessage(code, params, locale);
    }

}
