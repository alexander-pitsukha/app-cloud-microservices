package com.alexander.identityservice.controller;

import com.alexander.identityservice.controller.request.AuthRequest;
import com.alexander.identityservice.controller.request.UserBody;
import com.alexander.identityservice.controller.response.TokenResponse;
import com.alexander.identityservice.dto.UserDto;
import com.alexander.identityservice.service.TokenService;
import com.alexander.identityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("signin")
    public ResponseEntity<TokenResponse> signIn(@RequestBody @Validated AuthRequest authRequest) {
        log.info("Authentication sign in");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDto userDto = userService.findUserByEmail(authRequest.getEmail());
        return ResponseEntity.ok(tokenService.createTokenResponse(userDto.getId()));
    }

    @PostMapping("signup")
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Validated UserBody userBody) {
        log.info("Authentication sign up");
        userService.save(userBody);
        return ResponseEntity.ok().build();
    }

    @GetMapping("refresh-token")
    public ResponseEntity<TokenResponse> refreshTokens(HttpServletRequest request) {
        log.info("Authentication refresh tokens");
        String username = (String) request.getAttribute("email");
        String refreshToken = (String) request.getAttribute("token");
        return ResponseEntity.ok(tokenService.refreshTokens(username, refreshToken));
    }

    @GetMapping("me")
    public ResponseEntity<UserDto> getMe(Authentication authentication) {
        log.info("Authentication get me");
        return ResponseEntity.ok(userService.findUserByEmail(authentication.getName()));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleRuntimeExceptions(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
