package com.alexander.services.identity.service;

import com.alexander.services.identity.controller.response.TokenResponse;

public interface TokenService {

    TokenResponse createTokenResponse(Long userId);

    TokenResponse refreshTokens(String username, String refreshToken);

}
