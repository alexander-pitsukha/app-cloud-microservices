package com.alexander.services.identity.service.impl;

import com.alexander.services.identity.authencation.jwt.TokenProvider;
import com.alexander.services.identity.controller.response.TokenResponse;
import com.alexander.services.identity.entity.RefreshToken;
import com.alexander.services.identity.entity.User;
import com.alexander.services.identity.repository.RefreshTokenRepository;
import com.alexander.services.identity.repository.UserRepository;
import com.alexander.services.identity.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteRefreshTokensByExpireDate() {
        var now = LocalDateTime.now(ZoneId.systemDefault());
        log.info("Delete refreshTokens by expired date {}", now);
        refreshTokenRepository.deleteByExpireDate(now);
    }

    @Transactional
    @Override
    public TokenResponse createTokenResponse(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        String refreshToken = tokenProvider.generateRefreshToken(user);
        saveToken(refreshToken, user);
        return createTokenResponse(user, refreshToken);
    }

    @Transactional
    @Override
    public TokenResponse refreshTokens(String username, String refreshToken) {
        User user = userRepository.findByEmail(username).orElseThrow();
        RefreshToken entity = refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new NoSuchElementException("Token expired"));
        String newRefreshToken = tokenProvider.generateRefreshToken(user);
        setFields(entity, newRefreshToken, user);
        return createTokenResponse(user, newRefreshToken);
    }

    private TokenResponse createTokenResponse(User user, String refreshToken) {
        var tokenResponse = new TokenResponse();
        tokenResponse.setTokenType("Bearer");
        tokenResponse.setAccessToken(tokenProvider.generateToken(user));
        tokenResponse.setExpiresIn(tokenProvider.extractExpiration(refreshToken).getTime());
        tokenResponse.setRefreshToken(refreshToken);
        return tokenResponse;
    }

    private void saveToken(String refreshToken, User user) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserId(user.getId());
        RefreshToken entity = optionalRefreshToken.orElseGet(RefreshToken::new);
        setFields(entity, refreshToken, user);
        refreshTokenRepository.saveAndFlush(entity);
    }

    private void setFields(RefreshToken entity, String refreshToken, User user) {
        entity.setToken(refreshToken);
        entity.setExpireDate(extractExpiration(refreshToken));
        entity.setUser(user);
    }

    private LocalDateTime extractExpiration(String token) {
        return tokenProvider.extractExpiration(token).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
