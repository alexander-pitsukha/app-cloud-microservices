package com.alexander.services.identity.service.impl;

import com.alexander.services.identity.exception.SendEmailException;
import com.alexander.services.identity.service.EmailService;
import com.alexander.services.identity.util.Constants;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    @Value("${email.from}")
    private String emailFrom;
    @Value("${frontend.host}")
    private String frontendHost;

    @Override
    public void sendEmailForCreateUser(final String email, final UUID token) {
        var message = javaMailSender.createMimeMessage();
        try {
            var helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(emailFrom);
            helper.setTo(email);
            helper.setSubject(messageSource.getMessage("msg.auth.mail.verify.email.subject", new Object[]{Constants.APPLICATION}, Locale.getDefault()));
            var resetUrl = UriComponentsBuilder.fromUriString(frontendHost).pathSegment("#/verify-email").queryParam(Constants.TOKEN, token.toString()).build().toUriString();
            helper.setText(messageSource.getMessage("msg.auth.mail.verify.email.body", new Object[]{resetUrl, resetUrl, Constants.APPLICATION}, Locale.getDefault()));
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error(e.toString(), e);
            throw new SendEmailException(messageSource.getMessage(Constants.ERROR_MSG_USER_EMAIL_VERIFICATION_RESET_OR_EXPIRED, null, Locale.getDefault()));
        }
    }

    @Override
    public void sendEmailForUpdateUserPassword(final String email, final UUID token, String password) {
        var message = javaMailSender.createMimeMessage();
        try {
            var helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(emailFrom);
            helper.setTo(email);
            helper.setSubject(messageSource.getMessage("msg.auth.mail.reset.password.subject", new Object[]{Constants.APPLICATION}, Locale.getDefault()));
            var resetUrl = UriComponentsBuilder.fromUriString(frontendHost).pathSegment("#/password-reset").queryParam(Constants.TOKEN, token.toString()).build().toUriString();
            helper.setText(messageSource.getMessage("msg.auth.mail.reset.password.body", new Object[]{Constants.APPLICATION, email, resetUrl, resetUrl, password, Constants.APPLICATION}, Locale.getDefault()));
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error(e.toString(), e);
            throw new SendEmailException(messageSource.getMessage("error.msg.user.password.reset.send.message", null, Locale.getDefault()));
        }
    }

}
