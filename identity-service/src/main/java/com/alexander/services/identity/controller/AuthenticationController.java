package com.alexander.services.identity.controller;

import com.alexander.services.identity.controller.request.AuthRequest;
import com.alexander.services.identity.controller.request.ResetPasswordRequest;
import com.alexander.services.identity.controller.request.UpdatePasswordRequest;
import com.alexander.services.identity.controller.request.UserRequest;
import com.alexander.services.identity.controller.response.TokenResponse;
import com.alexander.services.identity.dto.UserDto;
import com.alexander.services.identity.exception.PasswordAlreadyUsedException;
import com.alexander.services.identity.exception.ValidationException;
import com.alexander.services.identity.service.TokenService;
import com.alexander.services.identity.service.UserService;
import com.alexander.services.identity.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;
    private final UserCache userCache;

    @PostMapping("signin")
    public ResponseEntity<TokenResponse> signIn(@RequestBody @Validated AuthRequest authRequest) {
        log.info("Authentication sign in");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDto userDto = userService.userSignIn(authRequest.getEmail());
        userCache.removeUserFromCache(userDto.getEmail());
        return ResponseEntity.ok(tokenService.createTokenResponse(userDto.getId()));
    }

    @PostMapping("signup")
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Validated UserRequest userRequest) {
        log.info("Authentication sign up");
        userService.save(userRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("signout")
    public ResponseEntity<HttpStatus> signOut(Authentication authentication) {
        userCache.removeUserFromCache(authentication.getName());
        userService.userLogOut(authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("refresh-token")
    public ResponseEntity<TokenResponse> refreshTokens(HttpServletRequest request) {
        log.info("Authentication refresh tokens");
        String username = (String) request.getAttribute(Constants.EMAIL);
        String refreshToken = (String) request.getAttribute(Constants.TOKEN);
        return ResponseEntity.ok(tokenService.refreshTokens(username, refreshToken));
    }

    @GetMapping("me")
    public ResponseEntity<UserDto> getMe(Authentication authentication) {
        log.info("Authentication get me");
        return ResponseEntity.ok(userService.findUserByEmail(authentication.getName()));
    }

    @PutMapping("verify-email")
    public ResponseEntity<HttpStatus> verifyEmail(@RequestBody @NotBlank String token) {
        log.info("Authentication verify email.");
        userService.updateEmailVerification(token);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("{id}/password-reset")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HttpStatus> resetPassword(@PathVariable Long id, @RequestBody @NotBlank String password) {
        log.info("Authentication reset user password.");
        userService.updatePasswordAndSendTokenForReset(id, password);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("password-reset")
    public ResponseEntity<HttpStatus> resetPassword(@RequestBody @Validated ResetPasswordRequest resetPasswordRequest) {
        log.info("Authentication reset password.");
        userService.updateUserPasswordByPasswordResetToken(resetPasswordRequest.getToken(), resetPasswordRequest.getCurrentPassword(), resetPasswordRequest.getNewPassword());
        return ResponseEntity.accepted().build();
    }

    @PutMapping("{id}/password-update")
    @PreAuthorize("@userSecurity.hasUserId(authentication, #id)")
    public ResponseEntity<HttpStatus> updatePassword(@PathVariable Long id, @RequestBody @Validated UpdatePasswordRequest passwordRequest) {
        log.info("update password.");
        userService.updatePassword(id, passwordRequest.getCurrentPassword(), passwordRequest.getNewPassword());
        return ResponseEntity.accepted().build();
    }

    @ExceptionHandler({ValidationException.class, PasswordAlreadyUsedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleRuntimeExceptions(RuntimeException e) {
        log.info(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
